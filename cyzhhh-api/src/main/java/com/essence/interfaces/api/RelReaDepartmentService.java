package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RelReaDepartmentEsr;
import com.essence.interfaces.model.RelReaDepartmentEsu;
import com.essence.interfaces.param.RelReaDepartmentEsp;

import java.util.List;

/**
 * 河道部门关系表服务层
 *
 * @author zhy
 * @since 2022-10-24 17:43:25
 */
public interface RelReaDepartmentService extends BaseApi<RelReaDepartmentEsu, RelReaDepartmentEsp, RelReaDepartmentEsr> {
    /**
     * 删除
     *
     * @param relReaDepartmentEsu
     * @return
     */
    int delete(RelReaDepartmentEsu relReaDepartmentEsu);
    /**
     * 根据条件查询
     * @return
     */
    List<RelReaDepartmentEsr> findBycondition(PaginatorParam param);

}
