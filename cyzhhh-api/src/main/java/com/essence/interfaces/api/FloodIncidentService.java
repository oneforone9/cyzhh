package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.FloodIncidentEsr;
import com.essence.interfaces.model.FloodIncidentEsu;
import com.essence.interfaces.param.FloodIncidentEsp;

/**
 * 积水事件处置服务层
 *
 * @author zhy
 * @since 2024-07-17 19:32:42
 */
public interface FloodIncidentService extends BaseApi<FloodIncidentEsu, FloodIncidentEsp, FloodIncidentEsr> {


    /**
     * 防御科审核或驳回
     *
     * @param esu
     */
    void auditOrReject(FloodIncidentEsu esu);

    /**
     * 抢险队伍接受或拒绝
     *
     * @param esu
     */
    void acceptOrRefuse(FloodIncidentEsu esu);

    /**
     * 抢险队伍，更新到达时间
     *
     * @param esu
     */
    void updateRescueTime(FloodIncidentEsu esu);

    /**
     * 抢险队伍，更新完成时间
     *
     * @param esu
     */
    void finishRescueTime(FloodIncidentEsu esu);

}
