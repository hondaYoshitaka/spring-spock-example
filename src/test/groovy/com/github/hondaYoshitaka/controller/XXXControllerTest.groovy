package com.github.hondaYoshitaka.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto
import com.github.hondaYoshitaka.service.XXXService
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Pageable
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification
import spock.lang.Unroll


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Unroll
class XXXControllerTest extends Specification {
    @InjectMocks
    XXXController xxxController

    @MockBean
    XXXService xxxService

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "xxx１件取得_idに対応するエンティティが存在する場合、レスポンスのフィールド値が取得できること"() {
        given:
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

        where:
        id || _
        0L || _
    }

    def "xxx１件取得_idに対応するエンティティが存在しすべての項目が空の場合、レスポンスのフィールド値が取得できること"() {
        given:
        def responseDto = XXXFetchResponseDto.builder().build()
        Mockito.when(xxxService.fetchOne(id)).thenReturn(responseDto)

        when:
        def actual = mockMvc.perform(get("/api/xxx/${id}"))
                .andExpect(status().isOk())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        assert contents.path('id').isNull()
        assert contents.path('name').isNull()
        assert contents.path('price').isNull()

        where:
        id || _
        0L || _
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

    def "xxx検索_リクエストが[#name, #categoryId, #price]の場合、レスポンスのフィールド値が取得できること"() {
        given:
        def params = new LinkedMultiValueMap<String, String>()
        name?.with { params.add('name', it) }
        categoryId?.with { params.add('categoryId', it) }
        price?.with { params.add('price', it) }

        def xxxs = [XXXsFetchResponseDto.XXXDto.builder().id(1).name('name 1').price(1).build()]
        def responseDto = XXXsFetchResponseDto.builder().xxxs(xxxs).build()

        Mockito.when(xxxService.fetchAll(Mockito.eq(categoryId.toLong()), Mockito.eq(price.toInteger()), Mockito.any(Pageable)))
                .thenReturn(responseDto)

        when:
        def actual = mockMvc.perform(get("/api/xxx").params(params))
                .andExpect(status().isOk())
                .andReturn()

        then:
        def contents = objectMapper.readTree(actual.response.contentAsByteArray)

        def errors = contents.path('xxxs').asList()
        assert errors.size() == 1
        assert errors[0].path('id').asLong() == 1L
        assert errors[0].path('name').asText() == 'name 1'
        assert errors[0].path('price').asInt() == 1

        where:
        name      | categoryId          | price     || _
        // name
        ''        | '2'                 | '2'       || _
        'a'       | '2'                 | '2'       || _
        'a' * 255 | '2'                 | '2'       || _
        // category id
        'name'    | '' + Long.MIN_VALUE | '2'       || _
        'name'    | '-1'                | '2'       || _
        'name'    | '0'                 | '2'       || _
        'name'    | '1'                 | '2'       || _
        'name'    | '' + Long.MAX_VALUE | '2'       || _
        // price
        'name'    | '2'                 | '0'       || _
        'name'    | '2'                 | '1'       || _
        'name'    | '2'                 | '999999'  || _
        'name'    | '2'                 | '1000000' || _
    }

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
        name      | categoryId | price     || field        | code
        null      | '1'        | '2'       || 'name'       | 'NotNull'
        'a' * 256 | '1'        | '2'       || 'name'       | 'Length'
        'name'    | null       | '2'       || 'categoryId' | 'NotNull'
        'name'    | ''         | '2'       || 'categoryId' | 'NotNull'
        'name'    | '1'        | null      || 'price'      | 'NotNull'
        'name'    | '1'        | ''        || 'price'      | 'NotNull'
        'name'    | '1'        | '-2'      || 'price'      | 'Range'
        'name'    | '1'        | '-1'      || 'price'      | 'Range'
        'name'    | '1'        | '1000001' || 'price'      | 'Range'
        'name'    | '1'        | '1000002' || 'price'      | 'Range'
    }
}
