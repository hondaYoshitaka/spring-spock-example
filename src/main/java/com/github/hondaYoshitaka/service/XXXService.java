package com.github.hondaYoshitaka.service;

import com.github.hondaYoshitaka.component.exception.ResourceNotFoundException;
import com.github.hondaYoshitaka.model.entity.XXX;
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXPostResponseDto;
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto.XXXDto;
import com.github.hondaYoshitaka.repository.XXXRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class XXXService {

    private final XXXRepository xxxRepository;

    public XXXService(final XXXRepository xxxRepository) {
        this.xxxRepository = xxxRepository;
    }

    public XXXFetchResponseDto fetchOne(final Long id) {
        final var entity = xxxRepository.findOne(id);

        if (entity == null) {
            throw new ResourceNotFoundException("Not found resource xxx.");
        }
        return XXXFetchResponseDto.builder()
                                  .id(entity.getId())
                                  .name(entity.getName())
                                  .price(entity.getPrice())
                                  .build();
    }

    public XXXsFetchResponseDto fetchAll(final Long categoryId,
                                         final Integer price,
                                         final Pageable pageable) {
        final var entities = xxxRepository.findAllByCategory(categoryId,
                                                             price,
                                                             pageable);
        final var dtos = entities.stream()
                                 .map(XXXService::mapXxxDto)
                                 .collect(Collectors.toList());

        return XXXsFetchResponseDto.builder()
                                   .xxxs(dtos)
                                   .build();
    }

    public XXXPostResponseDto postOne(final String name,
                                      final Long categoryId,
                                      final Integer price) {
        final var entity = new XXX() {{
            this.setName(name);
            this.setCategoryId(categoryId);
            this.setPrice(price);
            this.setActive(true);
        }};
        xxxRepository.insertOne(entity);

        return XXXPostResponseDto.builder()
                                 .id(entity.getId())
                                 .name(entity.getName())
                                 .price(entity.getPrice())
                                 .build();
    }

    private static XXXDto mapXxxDto(final XXX entity) {
        return XXXDto.builder()
                     .id(entity.getId())
                     .name(entity.getName())
                     .price(entity.getPrice())
                     .build();
    }
}
