package com.essence.interfaces.api;


import com.essence.dao.entity.*;
import com.essence.dao.entity.water.StWaterRiskForecastDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.WaterInfoDto;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.model.StWaterGateEsr;
import com.essence.interfaces.model.StWaterGateEsu;
import com.essence.interfaces.param.StWaterGateEsp;

import java.util.List;
import java.util.Map;


/**
 * 服务层
 * @author majunjie
 * @since 2023-04-20 15:36:37
 */
public interface StWaterGateService extends BaseApi<StWaterGateEsu, StWaterGateEsp, StWaterGateEsr> {
    List<RiverRiskPoint> selectRiverRiskPoint();

    List<Map<String, Object>>  selectForecastWaterLevel(String caseId);

    WaterSituationCountVo getWaterSituation(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    List<RiverRiskPoint> getRiverRiskPoints(Map<String, List<StSnConvertEntity>> stSnMap, Map<String, List<StSectionModelDto>> stSectionModelDtoMap, List<StStbprpBEntity> stStbprpBEntities, Map<String, List<StWaterRiskForecastDto>> stWaterRiskForecastMap);

    List<RainSituationVo> getRainSituation(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    List<RiverRiskPointPC> selectRiverRiskPointPC();

    List<GateCameraVo> getCameraByGateName(String name);

    Object selectForecastWaterLevelByRiverId(String caseId, String riverId);

    List<RiskByRiver> selectGateRiskByRiverId(String riverId);

    List<RiverRiskPointGate> selectRiverRiskPointGate(String caseId);

    Object gateRiskModule(String caseId);

    List<WaterFlowStationVo> getStationByPumpName(String name);

    List<StationWaterFlowDataVo> getWaterFlowByStation(String stcd);



    WaterInfoDto getWaterInfoByGateName(String name);

    WaterInfoDto getWaterInfoByPumpName(String name);
}
