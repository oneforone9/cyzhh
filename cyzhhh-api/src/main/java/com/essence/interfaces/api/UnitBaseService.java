package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.DepartmentBaseEsu;
import com.essence.interfaces.model.UnitBaseEsr;
import com.essence.interfaces.model.UnitBaseEsrEx;
import com.essence.interfaces.model.UnitBaseEsu;
import com.essence.interfaces.param.UnitBaseEsp;

/**
 * 单位信息表服务层
 *
 * @author zhy
 * @since 2022-10-20 14:16:37
 */
public interface UnitBaseService extends BaseApi<UnitBaseEsu, UnitBaseEsp, UnitBaseEsr> {

    /**
     * 根据条件分页查询(含班组)
     * @param param
     * @return
     */
    Paginator<UnitBaseEsrEx> searchDepart(PaginatorParam param);

    /**
     * 设置巡河组长
     * @param departmentBaseEsu
     * @return
     */
    Object setDepart(DepartmentBaseEsu departmentBaseEsu);

    /**
     * 判断是不是巡河组长
     * @param personBaseId
     * @return
     */
    Object selectDepart(String personBaseId);
}
