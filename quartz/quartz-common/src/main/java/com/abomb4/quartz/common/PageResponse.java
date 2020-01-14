package com.abomb4.quartz.common;

import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 带有响应数据的分页基础接口
 *
 * @author abomb4
 */
@Data
public class PageResponse<T> implements Pagination {

    private int pageSize;
    private int pageNo;
    private long total;
    private List<T> items;

    public PageResponse() {
    }

    public PageResponse(int pageSize, int pageNo, long total, List<T> items) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = total;
        this.items = items;
    }

    /**
     * 转换 items 里面的东西为另一种形态
     *
     * @param mapper 转换器
     * @param <R>    转换结果类型
     * @return 新的 PageResponse
     */
    public <R> PageResponse<R> map(Function<T, R> mapper) {
        if (this.items == null) {
            return new PageResponse<>(pageSize, pageNo, total, null);
        } else {
            return new PageResponse<>(pageSize, pageNo, total, items.stream().map(mapper).collect(Collectors.toList()));
        }
    }

    @Override
    public long getOffset() {
        return pageSize * (pageNo - 1);
    }
}
