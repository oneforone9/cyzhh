package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.RosteringInfoEsr;
import com.essence.interfaces.model.RosteringInfoEsu;
import com.essence.interfaces.param.RosteringInfoEsp;

/**
 * 人员巡河排班信息表服务层
 *
 * @author zhy
 * @since 2022-10-25 11:22:53
 */
public interface RosteringInfoService extends BaseApi<RosteringInfoEsu, RosteringInfoEsp, RosteringInfoEsr> {
    /**
     * 根据人员id修改人员名称
     * @param personId
     * @param personName
     * @return
     */
    int updateByPersonId(String personId, String personName);

    /**
     * 根据河道id修改河道名称
     * @param reaId
     * @param reaName
     * @return
     */
    int updateByReaId(String reaId, String reaName);

}
