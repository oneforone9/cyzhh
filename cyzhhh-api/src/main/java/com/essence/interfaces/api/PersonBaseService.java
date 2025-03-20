package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.PersonBaseEsr;
import com.essence.interfaces.model.PersonBaseEsu;
import com.essence.interfaces.param.PersonBaseEsp;

import java.util.List;

/**
 * 人员基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-20 15:07:22
 */
public interface PersonBaseService extends BaseApi<PersonBaseEsu, PersonBaseEsp, PersonBaseEsr> {

    /**
     * 查询所有
     *
     * @return
     */
    List<PersonBaseEsr> findInsideUsing();
}
