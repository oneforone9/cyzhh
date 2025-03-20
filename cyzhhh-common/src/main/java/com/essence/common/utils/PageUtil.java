package com.essence.common.utils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @author： xxxx
 * @date： 2022-02-07 14:31
 *  手动实现分页
 */
public class PageUtil<T> {
    /**
     * 在线达标占比
     */
    private BigDecimal percent;
    /**
     * 达标数量
     */
    private Integer checkedNum;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer pages;

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
     * @param current 当前页
     * @param pageSize  当前页显示多少条数据
     */
    public PageUtil(List<T> list, Integer current, Integer pageSize,BigDecimal  percent,Integer checkedNum) {
        if (percent != null){
            this.percent = percent;
        }
        if (checkedNum != null){
            this.checkedNum = checkedNum;
        }
        this.current = current;
        this.pageSize = pageSize;
        this.total = list.size();
        //总记录数和每页显示的记录数据之间是否可以凑成整数(pages)
        boolean full = total % pageSize == 0;
        //分页 == 根据pageSize(每页显示的记录数) 计算pages
        if (!full) {
            this.pages = total / pageSize + 1;
        }else {
            //如果凑成整数
            this.pages = total / pageSize;
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = current * pageSize - pageSize;
        if (current == 0) {
            throw  new ArithmeticException("第0页无法展示");
        }else if (current > pages) {
            //如果查询的页码数大于总的页码数，list设置为[];
            list = new ArrayList<>();
        }else if (Objects.equals(current, pages)) {
            //如果查询的当前页等于总页数，直接索引到total处;
            toIndex = total;
        }else {
            // 如果查询的页码数小于总数，不用担心切割List的时候toIndex索引会越界，这里就直接等.
            toIndex = current * pageSize;
        }
        if (list.size() == 0) {
            this.records = list;
        }else {
            this.records = list.subList(fromIndex,toIndex);
        }

    }
    /**
     * @param list  查询的数据
     * @param current 当前页
     * @param pageSize  当前页显示多少条数据
     */
    public PageUtil(List<T> list, Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = list.size();
        //总记录数和每页显示的记录数据之间是否可以凑成整数(pages)
        boolean full = total % pageSize == 0;
        //分页 == 根据pageSize(每页显示的记录数) 计算pages
        if (!full) {
            this.pages = total / pageSize + 1;
        }else {
            //如果凑成整数
            this.pages = total / pageSize;
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = current * pageSize - pageSize;
        if (current == 0) {
            throw  new ArithmeticException("第0页无法展示");
        }else if (current > pages) {
            //如果查询的页码数大于总的页码数，list设置为[];
            list = new ArrayList<>();
        }else if (Objects.equals(current, pages)) {
            //如果查询的当前页等于总页数，直接索引到total处;
            toIndex = total;
        }else {
            // 如果查询的页码数小于总数，不用担心切割List的时候toIndex索引会越界，这里就直接等.
            toIndex = current * pageSize;
        }
        if (list.size() == 0) {
            this.records = list;
        }else {
            this.records = list.subList(fromIndex,toIndex);
        }

    }
    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
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

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public Integer getCheckedNum() {
        return checkedNum;
    }

    public void setCheckedNum(Integer checkedNum) {
        this.checkedNum = checkedNum;
    }
}


