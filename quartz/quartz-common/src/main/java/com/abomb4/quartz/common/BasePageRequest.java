package com.abomb4.quartz.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 带分页的请求的基类
 *
 * @author abomb4
 */
@Data
public abstract class BasePageRequest implements Pagination, Serializable {
    private static final int DEFAULT_PAGESIZE = 10;
    private static final int DEFAULT_PAGE_NO = 1;

    private Integer pageSize;
    private Integer pageNo;

    @Override
    public int getPageSize() {
        return pageSize == null ? DEFAULT_PAGESIZE : pageSize;
    }

    @Override
    public int getPageNo() {
        return pageNo == null ? DEFAULT_PAGE_NO : pageNo;
    }

    @Override
    public long getOffset() {
        return getPageSize() * (getPageNo() - 1);
    }

    @Override
    public long getTotal() {
        return 0;
    }
}
