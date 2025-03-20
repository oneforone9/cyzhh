package com.essence.interfaces.api;

import com.alibaba.fastjson.JSONObject;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.model.StSideGateEsr;
import com.essence.interfaces.model.StSideGateEsrRes;
import com.essence.interfaces.model.StSideGateEsu;
import com.essence.interfaces.model.StSideGateEsuParam;
import com.essence.interfaces.param.StSideGateEsp;

import java.util.Map;

import java.util.List;

/**
 * 边闸基础表服务层
 * @author BINX
 * @since 2023-01-17 11:05:23
 */
public interface StSideGateService extends BaseApi<StSideGateEsu, StSideGateEsp, StSideGateEsr> {
    ResponseResult getStationInfoList(StStbprpBEntityDTO stStbprpBEntityDTO);

    void updateData(StStbprpBEntityDTO e);

    /**
     * 查询闸坝负责人信息
     * @param stSideGateEsuParam
     * @return
     */
    Paginator<StSideGateEsrRes> getStSideGateRelation(StSideGateEsuParam stSideGateEsuParam);

    /**
     * 统计水闸泵站的总数、远控、正常以及故障的数量
     * @param sttp
     * @return
     */
    Map<String, Object> getStSideGate(String sttp);

    List<StSideGateEsr> getPump(String caseId);

    /**
     * 闸坝实时工况
     * @param sttp
     */
    List getStSideGateNow(String sttp, String stnm, String rvnm, String p1Hitch, String p2Hitch, String m00,String m01,String m02);

    ResponseResult dealRemoteSoltPunp(String deviceAddr, Integer pNum, Integer status, JSONObject paramJson);

}
