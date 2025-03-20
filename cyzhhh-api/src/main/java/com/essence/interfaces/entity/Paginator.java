package com.essence.interfaces.entity;

/**
 * @author zhy
 * @since 2022/5/11 16:27
 */
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Paginator<T> implements Serializable {
    private static final long serialVersionUID = -7067617254114194303L;
    /**
     * 主体数据集合
     */
    private List<T> items = new ArrayList<>();
    /**
     * 总数
     * @mock 102
     */
    private int totalCount;
    /**
     * 总页码
     * @mock 11
     */
    private int pageCount;
    /**
     * size
     * @mock 10
     */
    private int pageSize;
    /**
     * 当前页
     * @mock 2
     */
    private int currentPage;

    /**
     * 空构造器
     */
    public Paginator(){

    }

    /**
     *
     * @author tanmu
     * @since 2022/5/17
     */
    public Paginator(List<T> list, int pageSize, int currentPage){
//        this.pageSize = pageSize;
//        this.currentPage = currentPage;
//        this.totalCount = list.size();
//        this.pageCount = this.totalCount / this.pageSize + (this.totalCount % this.pageSize & 1);
//        if (list.size() == 0){
//            this.items = null;
//        }else {
//            this.items = list.subList(
//                    (this.currentPage - 1) * this.pageSize,
//                    this.currentPage == this.totalCount ? this.pageSize : this.currentPage * this.pageSize
//            );
//        }


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
            this.items = list;
        }else {
            this.items = list.subList(fromIndex,toIndex);
        }

    }
}
