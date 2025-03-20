package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位信息表返回实体
 *
 * @author zhy
 * @since 2022-10-20 14:16:36
 */

@Data
public class UnitBaseEsrEx extends Esr {


    private static final long serialVersionUID = -783971612561240564L;
    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String unitName;

    /**
     * 班组集合
     */
    private List<DepartmentBaseEsr> departList;

}
