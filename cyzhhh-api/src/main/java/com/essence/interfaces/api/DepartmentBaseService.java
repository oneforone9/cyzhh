package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.DepartmentBaseEsr;
import com.essence.interfaces.model.DepartmentBaseEsu;
import com.essence.interfaces.param.DepartmentBaseEsp;

import java.util.List;

/**
 * 部门信息表服务层
 *
 * @author zhy
 * @since 2022-10-24 16:50:29
 */
public interface DepartmentBaseService extends BaseApi<DepartmentBaseEsu, DepartmentBaseEsp, DepartmentBaseEsr> {
    /**
     * 查询所有
     *
     * @return
     */
    List<DepartmentBaseEsr> findAll();

    /**
     * 设置巡河组长
     * @param departmentBaseEsu
     * @return
     */
    Integer setDepart(DepartmentBaseEsu departmentBaseEsu);
}
