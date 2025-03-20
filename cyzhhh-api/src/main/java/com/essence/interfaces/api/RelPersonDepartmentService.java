package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RelPersonDepartmentEsr;
import com.essence.interfaces.model.RelPersonDepartmentEsu;
import com.essence.interfaces.param.RelPersonDepartmentEsp;

import java.util.List;

/**
 * 人员部门关系表服务层
 *
 * @author zhy
 * @since 2022-10-24 17:42:56
 */
public interface RelPersonDepartmentService extends BaseApi<RelPersonDepartmentEsu, RelPersonDepartmentEsp, RelPersonDepartmentEsr> {
    /**
     * 删除
     *
     * @param relPersonDepartmentEsu
     * @return
     */
    int delete(RelPersonDepartmentEsu relPersonDepartmentEsu);

    /**
     * 根据条件查询
     * @return
     */
    List<RelPersonDepartmentEsr>  findBycondition(PaginatorParam param);
}
