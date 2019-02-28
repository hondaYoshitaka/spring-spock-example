package com.github.hondaYoshitaka.service;

import com.github.hondaYoshitaka.component.exception.ResourceNotFoundException;
import com.github.hondaYoshitaka.model.entity.XXX;
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXPostResponseDto;
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto.XXXDto;
import com.github.hondaYoshitaka.repository.XXXRepository;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * xxxに関するserviceクラス.
 *
 * @author hondaYoshitaka
 */
@Service
@Transactional(readOnly = true)
public class XXXService {

    private final XXXRepository xxxRepository;

    public XXXService(final XXXRepository xxxRepository) {
        this.xxxRepository = xxxRepository;
    }

    /**
     * xxxを取得します.
     *
     * @param id xxxID
     * @return response dto
     */
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

    /**
     * 一致するxxxをすべて取得します.
     *
     * @param categoryId カテゴリID
     * @param price      価格
     * @param pageable   ページング
     * @return response dto
     */
    public XXXsFetchResponseDto fetchAll(final Long categoryId,
                                         final Integer price,
                                         final Pageable pageable) {
        final RowBounds rowBounds = new RowBounds(Math.toIntExact(pageable.getOffset()),
                                                  pageable.getPageSize());
        final var entities = xxxRepository.findAllByCategory(categoryId,
                                                             price,
                                                             rowBounds);
        final var dtos = entities.stream()
                                 .map(XXXService::createXXXDto)
                                 .collect(Collectors.toList());

        return XXXsFetchResponseDto.builder()
                                   .xxxs(dtos)
                                   .build();
    }

    /**
     * xxxを登録します.
     *
     * @param name       名称
     * @param categoryId カテゴリID
     * @param price      価格
     * @return response dto
     */
    @Transactional
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

    /**
     * xxx dtoを作成します.
     *
     * @param entity xxx
     * @return dto
     */
    private static XXXDto createXXXDto(final XXX entity) {
        return XXXDto.builder()
                     .id(entity.getId())
                     .name(entity.getName())
                     .price(entity.getPrice())
                     .build();
    }
}
