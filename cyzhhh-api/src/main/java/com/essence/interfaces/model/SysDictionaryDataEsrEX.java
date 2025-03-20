package com.essence.interfaces.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据表返回实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:26
 */

@Data
public class SysDictionaryDataEsrEX extends SysDictionaryDataEsr {

    private static final long serialVersionUID = 4038740068851394077L;
    /**
     * 子级
     */
    private List<SysDictionaryDataEsrEX> children = new ArrayList<>();
}
