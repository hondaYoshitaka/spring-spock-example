package com.github.hondaYoshitaka.repository;

import com.github.hondaYoshitaka.model.entity.XXX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * xxxに関するrepositoryクラス.
 *
 * @author hondaYoshitaka
 */
@Mapper
public interface XXXRepository {
    /**
     * xxxを取得します.
     *
     * @param id ID
     * @return xxx
     */
    XXX findOne(@Param("id") Long id);

    /**
     * 一致するxxxをすべて取得します.
     *
     * @param categoryId カテゴリID
     * @param price      価格
     * @param rowBounds  ページング
     * @return xxxリスト
     */
    List<XXX> findAllByCategory(@Param("categoryId") Long categoryId,
                                @Param("price") Integer price,
                                RowBounds rowBounds);

    /**
     * xxxを登録します.
     *
     * @param entity xxx
     */
    void insertOne(@Param("entity") XXX entity);
}
