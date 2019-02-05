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
import spock.lang.Specification

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

    def "sample"() {
        given:
        def id = 0L

        def responseDto = XXXFetchResponseDto.builder().id(999L).name('xxx name').price(888).build()
        Mockito.when(xxxService.fetchOne(id)).thenReturn(responseDto)

        when:
        def actual = mockMvc.perform(get("/api/xxx/${id}"))
                .andExpect(status().isOk())
                .andReturn()

        then:
        def tree = objectMapper.readTree(actual.response.contentAsByteArray)

        assert tree.get('id').asInt() == 999
        assert tree.get('name').asText() == 'xxx name'
        assert tree.get('price').asInt() == 888
    }
}
