package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.TWorkorderRecordPointEsr;
import com.essence.interfaces.model.TWorkorderRecordPointEsu;
import com.essence.interfaces.param.TWorkorderRecordPointEsp;

/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息服务层
 * @author liwy
 * @since 2023-05-07 12:12:18
 */
public interface TWorkorderRecordPointService extends BaseApi<TWorkorderRecordPointEsu, TWorkorderRecordPointEsp, TWorkorderRecordPointEsr> {

    /**
     * 判断是否在打卡范围之内、是否是在必打卡点位
     * @param tWorkorderRecordPointEsu
     * @return
     */
    Object selectClockRange(TWorkorderRecordPointEsu tWorkorderRecordPointEsu);

    /**
     * 判断结束工单时是否再所有的必打卡点位完成打卡
     * @param orderId
     * @return
     */
    Object selectIsALLComplete(String orderId);
}
