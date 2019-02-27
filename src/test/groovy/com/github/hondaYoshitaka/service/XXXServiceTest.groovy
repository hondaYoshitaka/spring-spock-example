package com.github.hondaYoshitaka.service

import com.github.hondaYoshitaka.mockito.RowBoundsMatcher
import com.github.hondaYoshitaka.component.exception.ResourceNotFoundException
import com.github.hondaYoshitaka.model.entity.XXX
import com.github.hondaYoshitaka.repository.XXXRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.argThat
import static org.mockito.Mockito.eq

@Unroll
class XXXServiceTest extends Specification {
    @InjectMocks
    XXXService xxxService

    @Mock
    XXXRepository xxxRepository

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "xxx１件取得_idに対応するリソースが存在しない場合、例外が発生すること"() {
        given:
        def entity = null
        Mockito.when(xxxRepository.findOne(id)).thenReturn(entity)

        when:
        xxxService.fetchOne(id)

        then:
        thrown(expected)

        where:
        id || expected
        0L || ResourceNotFoundException
    }

    def "xxx１件取得_idに対応するリソースが存在する場合、レスポンスが取得できること"() {
        given:
        def entity = new XXX(id: 999L, name: 'name 1', price: 777)
        Mockito.when(xxxRepository.findOne(id)).thenReturn(entity)

        when:
        def actual = xxxService.fetchOne(id)

        then:
        verifyAll(actual) {
            it.id == 999L
            it.name == 'name 1'
            it.price == 777
        }

        where:
        id || _
        0L || _
    }

    def "xxx複数件取得_idに対応するリソースが存在しない場合、レスポンスと空配列が取得できること"() {
        given:
        def entities = []
        Mockito.when(xxxRepository.findAllByCategory(eq(categoryId), eq(price), argThat(RowBoundsMatcher.of(0, 10))))
                .thenReturn(entities)

        when:
        def actual = xxxService.fetchAll(categoryId, price, pageable)

        then:
        assert actual.xxxs.size() == 0

        where:
        categoryId | price | pageable              || _
        0L         | 100   | PageRequest.of(0, 10) || _
    }

    def "xxx複数件取得_idに対応するリソースが存在する場合、レスポンスが取得できること"() {
        given:
        def entities = [
                new XXX(id: 1L, name: 'name 1', price: 111),
                new XXX(id: 2L, name: 'name 2', price: 222)
        ]
        Mockito.when(xxxRepository.findAllByCategory(eq(categoryId), eq(price), argThat(RowBoundsMatcher.of(0, 10))))
                .thenReturn(entities)

        when:
        def actual = xxxService.fetchAll(categoryId, price, pageable)

        then:
        assert actual.xxxs.size() == 2

        verifyAll(actual.xxxs[0]) {
            it.id == 1
            it.name == 'name 1'
            it.price == 111
        }
        verifyAll(actual.xxxs[1]) {
            it.id == 2
            it.name == 'name 2'
            it.price == 222
        }

        where:
        categoryId | price | pageable              || _
        0L         | 100   | PageRequest.of(0, 10) || _
    }

    def "xxx登録_登録が呼ばれ、レスポンスが取得できること"() {
        when:
        def actual = xxxService.postOne(name, categoryId, price)

        then:
        Mockito.verify(xxxRepository, Mockito.times(1))
                .insertOne(new XXX(name: 'name 1', categoryId: 999L, price: 888, active: true)) || true

        verifyAll(actual) {
            it.id == null
            it.name == 'name 1'
            it.price == 888
        }

        where:
        name     | categoryId | price || _
        'name 1' | 999L       | 888   || _
    }

}
