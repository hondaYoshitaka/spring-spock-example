package com.github.hondaYoshitaka.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto
import com.github.hondaYoshitaka.service.XXXService
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class XXXControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @InjectMocks
    XXXController xxxController

    @MockBean
    XXXService xxxService

    def "xxx１件取得_idに対応するエンティティが存在する場合、レスポンスのフィールド値が取得できること"() {
        given:
        def id = 0L

        def responseDto = XXXFetchResponseDto.builder().id(999L).name('xxx name').price(888).build()
        Mockito.when(xxxService.fetchOne(id)).thenReturn(responseDto)

        when:
        def actual = mockMvc.perform(get("/api/xxx/${id}"))
                .andExpect(status().isOk())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        assert contents.path('id').asLong() == 999
        assert contents.path('name').asText() == 'xxx name'
        assert contents.path('price').asInt() == 888
    }

    def "xxx１件取得_idに対応するエンティティが存在しない場合、レスポンスnullが取得できること"() {
        given:
        def id = 0L

        def responseDto = null
        Mockito.when(xxxService.fetchOne(id)).thenReturn(responseDto)

        when:
        def actual = mockMvc.perform(get("/api/xxx/${id}"))
                .andExpect(status().isOk())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        assert contents == null
    }

    def "xxx１件取得_idの型が不一致の場合、バリデーションエラーになること"() {
        when:
        def actual = mockMvc.perform(get("/api/xxx/${id}"))
                .andExpect(status().isBadRequest())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        def errors = contents.path('errors').asList()
        assert errors.size() == 1
        assert errors[0].path('field').asText() == field
        assert errors[0].path('code').asText() == code

        where:
        id     || field | code
        'aaaa' || 'id'  | 'typeMismatch'
    }

    @Unroll
    def "xxx検索_リクエストが[#name, #categoryId, #price]の場合、バリデーションエラーとなること"() {
        given:
        def params = new LinkedMultiValueMap<String, String>()
        name?.with { params.add('name', it) }
        categoryId?.with { params.add('categoryId', it) }
        price?.with { params.add('price', it) }

        when:
        def actual = mockMvc.perform(get("/api/xxx").params(params))
                .andExpect(status().isBadRequest())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        def errors = contents.path('errors').asList()
        assert errors.size() == 1
        assert errors[0].path('field').asText() == field
        assert errors[0].path('code').asText() == code

        where:
        name   | categoryId | price || field        | code
        null   | '1'        | '2'   || 'name'       | 'NotNull'
        'name' | null       | '2'   || 'categoryId' | 'NotNull'
        'name' | '1'        | null  || 'price'      | 'NotNull'
    }
}
