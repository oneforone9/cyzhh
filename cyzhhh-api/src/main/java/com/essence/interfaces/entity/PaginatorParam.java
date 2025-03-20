package com.essence.interfaces.entity;

import lombok.Data;

import java.util.List;

/**
 * @author zhy
 * @since 2022/5/11 16:27
 */
@Data
public class PaginatorParam extends Esp {
    /**
     * 页码
     *
     * @mock 1
     */
    private int currentPage;
    /**
     * 个数
     *
     * @mock 10
     */
    private int pageSize;
    /**
     * 查询条件
     */
    private List<Criterion> conditions;
    /**
     * 排序条件
     */
    private List<Criterion> orders;

    /**
     * 共通查询条件
     * 加载or关键字前，建议后端追加参数使用此集合，不暴露给前端
     *
     * @ignore
     */
    private List<Criterion> currency;

}

