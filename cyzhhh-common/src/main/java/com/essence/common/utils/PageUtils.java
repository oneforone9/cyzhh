package com.essence.common.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @author： xxxx
 * @date： 2022-02-07 14:31
 *  手动实现分页
 */
public class PageUtils<T> {
    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 当前页显示多少条数据
     */
    private Integer pageSize;

    /**
     * 数据集合
     */
    private List<T> records;


    /**
     * @param list  查询的数据
     * @param currentPage 当前页
     * @param pageSize  当前页显示多少条数据
     */
    public PageUtils(List<T> list, Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = list.size();
        //总记录数和每页显示的记录数据之间是否可以凑成整数(pageCount)
        boolean full = totalCount % pageSize == 0;
        //分页 == 根据pageSize(每页显示的记录数) 计算pages
        if (!full) {
            this.pageCount = totalCount / pageSize + 1;
        }else {
            //如果凑成整数
            this.pageCount = totalCount / pageSize;
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = currentPage * pageSize - pageSize;
        if (currentPage == 0) {
            throw  new ArithmeticException("第0页无法展示");
        }else if (currentPage > pageCount) {
            //如果查询的页码数大于总的页码数，list设置为[];
            list = new ArrayList<>();
        }else if (Objects.equals(currentPage, pageCount)) {
            //如果查询的当前页等于总页数，直接索引到totalCount处;
            toIndex = totalCount;
        }else {
            // 如果查询的页码数小于总数，不用担心切割List的时候toIndex索引会越界，这里就直接等.
            toIndex = currentPage * pageSize;
        }
        if (list.size() == 0) {
            this.records = list;
        }else {
            this.records = list.subList(fromIndex,toIndex);
        }

    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}


