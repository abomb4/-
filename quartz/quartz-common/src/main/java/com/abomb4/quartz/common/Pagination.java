package com.abomb4.quartz.common;

/**
 * 分页基础接口
 *
 * @author abomb4
 */
public interface Pagination {

    /**
     * 分页大小
     *
     * @return 页大小
     */
    int getPageSize();

    /**
     * 页码
     *
     * @return 页码
     */
    int getPageNo();

    /**
     * 偏移量
     *
     * @return 偏移量
     */
    long getOffset();

    /**
     * 总数，只在响应类中有意义
     *
     * @return 总数，只在响应类中有意义
     */
    long getTotal();
}
