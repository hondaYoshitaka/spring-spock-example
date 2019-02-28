package com.github.hondaYoshitaka.mockito;

import org.apache.ibatis.session.RowBounds;
import org.mockito.ArgumentMatcher;

/**
 * {@code RowBounds}のmatcherクラス.
 * ----------------------------------------
 * {@code RowBounds} はequalsメソッドを実装していないため、{@code Mockito#eq}ではoffset/limitの同一性を検証できない.
 * そのため、本クラスではフィールドの一致を検証するためのワークアラウンドを提供する.
 *
 * @author hondaYoshitaka
 */
public class RowBoundsMatcher implements ArgumentMatcher<RowBounds> {

    private final int offset;
    private final int limit;

    private RowBoundsMatcher(final int offset, final int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public boolean matches(final RowBounds bounds) {
        return (bounds.getOffset() == offset && bounds.getLimit() == limit);
    }

    public static RowBoundsMatcher of(final int offset, final int limit) {
        return new RowBoundsMatcher(offset, limit);
    }
}
