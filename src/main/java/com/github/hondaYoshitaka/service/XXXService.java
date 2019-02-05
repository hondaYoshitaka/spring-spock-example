package com.github.hondaYoshitaka.service;

import com.github.hondaYoshitaka.model.entity.XXX;
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXPostResponseDto;
import com.github.hondaYoshitaka.repository.XXXRepository;
import org.springframework.stereotype.Service;

@Service
public class XXXService {

    private final XXXRepository xxxRepository;

    public XXXService(final XXXRepository xxxRepository) {
        this.xxxRepository = xxxRepository;
    }

    public XXXFetchResponseDto fetchOne(final Long id) {
        final XXX entity = xxxRepository.findOne(id);

        return XXXFetchResponseDto.builder()
                                  .id(entity.getId())
                                  .name(entity.getName())
                                  .price(entity.getPrice())
                                  .build();
    }

    public XXXPostResponseDto postXXX(final String name,
                                      final Long categoryId,
                                      final Integer price) {
        final XXX entity = new XXX();
        entity.setName(name);
        entity.setCategoryId(categoryId);
        entity.setPrice(price);
        entity.setActive(true);

        xxxRepository.insertOne(entity);

        return XXXPostResponseDto.builder()
                                 .id(entity.getId())
                                 .name(entity.getName())
                                 .price(entity.getPrice())
                                 .build();
    }
}
