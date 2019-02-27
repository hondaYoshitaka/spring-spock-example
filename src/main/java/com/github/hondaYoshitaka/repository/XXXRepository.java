package com.github.hondaYoshitaka.repository;

import com.github.hondaYoshitaka.model.entity.XXX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface XXXRepository {

    XXX findOne(@Param("id") Long id);

    List<XXX> findAllByCategory(@Param("categoryId") Long categoryId,
                                @Param("price") Integer price,
                                RowBounds rowBounds);

    void insertOne(@Param("entity") XXX entity);
}
