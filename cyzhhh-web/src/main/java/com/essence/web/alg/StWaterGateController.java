package com.essence.web.alg;

import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.StWaterGateService;
import com.essence.interfaces.dot.WaterInfoDto;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.model.StWaterGateEsr;
import com.essence.interfaces.model.StWaterGateEsu;
import com.essence.interfaces.param.StWaterGateEsp;
import com.essence.web.basecontroller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型断面基础表管理
 *
 * @author BINX
 * @since 2023-04-19 18:15:39
 */
@RestController
@RequestMapping("/stWaterGate")
@Slf4j
public class StWaterGateController extends BaseController<String, StWaterGateEsu, StWaterGateEsp, StWaterGateEsr> {
    @Autowired
    private StWaterGateService stWaterGateService;

    public StWaterGateController(StWaterGateService stWaterGateService) {
        super(stWaterGateService);
    }

    /**
     * 河道风险点
     */
    @GetMapping("/selectRiverRiskPoint")
    public ResponseResult<List<RiverRiskPoint>> selectRiverRiskPoint() {
        return  ResponseResult.success("查询成功", stWaterGateService.selectRiverRiskPoint());
    }

    /**
     * 预报水位
     */
    @GetMapping("/selectForecastWaterLevel")
    public ResponseResult selectForecastWaterLevel(@RequestParam String caseId) {
        return  ResponseResult.success("查询成功", stWaterGateService.selectForecastWaterLevel(caseId));
    }

    /**
     * 查询具体河流的预报水位
     */
    @GetMapping("/selectForecastWaterLevelByRiverId")
    public ResponseResult selectForecastWaterLevelByRiverId(@RequestParam String caseId, @RequestParam String riverId) {
        return  ResponseResult.success("查询成功", stWaterGateService.selectForecastWaterLevelByRiverId(caseId, riverId));
    }

    /**
     * 雨情概况
     */
    @PostMapping("/getRainSituation")
    public ResponseResult<List<RainSituationVo>> getRainSituation(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        return  ResponseResult.success("查询成功", stWaterGateService.getRainSituation(stCaseBaseInfoEsu));
    }

    /**
     * 水情概况
     */
    @PostMapping("/getWaterSituation")
    public ResponseResult<WaterSituationCountVo> getWaterSituation(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        return  ResponseResult.success("查询成功", stWaterGateService.getWaterSituation(stCaseBaseInfoEsu));
    }

    /**
     * 河道风险点 PC - 闸坝数据
     */
    @GetMapping("/selectRiverRiskPointPC")
    public ResponseResult<List<RiverRiskPointPC>> selectRiverRiskPointPC() {
        return  ResponseResult.success("查询成功", stWaterGateService.selectRiverRiskPointPC());
    }

    /**
     * 河道风险点 - 闸坝
     * @param caseId 案件id
     * @return
     */
    @GetMapping("/selectRiverRiskPointGate")
    public ResponseResult<List<RiverRiskPointGate>> selectRiverRiskPointGate(@RequestParam("caseId") String caseId) {
        return  ResponseResult.success("查询成功", stWaterGateService.selectRiverRiskPointGate(caseId));
    }

    /**
     * 河道风险点 - 闸坝 - 模拟数据
     * @param caseId 案件id
     * @return
     */
    @GetMapping("/gateRiskModule")
    public ResponseResult gateRiskModule(@RequestParam String caseId) {
        return  ResponseResult.success("查询成功", stWaterGateService.gateRiskModule(caseId));
    }

    /**
     * 根据河流ID查询闸坝水位信息
     */
    @GetMapping("/selectGateRiskByRiverId")
    public ResponseResult<List<RiskByRiver>> selectGateRiskByRiverId(@RequestParam String riverId) {
        return  ResponseResult.success("查询成功", stWaterGateService.selectGateRiskByRiverId(riverId));
    }

    /**
     * 闸坝对应摄像头信息
     */
    @GetMapping("/getCameraByGateName")
    public ResponseResult<List<GateCameraVo>> getCameraByGateName(@RequestParam String name) {
        return  ResponseResult.success("查询成功", stWaterGateService.getCameraByGateName(name));
    }

    /**
     * 通过水闸名称 关联 水位或者 流量信息
     */
    @GetMapping("/getWaterInfoByGateName")
    public ResponseResult<WaterInfoDto> getWaterInfoByGateName(@RequestParam String name) {
        return  ResponseResult.success("查询成功", stWaterGateService.getWaterInfoByGateName(name));
    }


    /**
     * 通过泵站名称 关联 水位或者 流量信息
     */
    @GetMapping("/getWaterInfoByPumpName")
    public ResponseResult<WaterInfoDto> getWaterInfoByPumpName(@RequestParam String name) {
        return  ResponseResult.success("查询成功", stWaterGateService.getWaterInfoByPumpName(name));
    }



    /**
     * 泵站对应的水位流量站列表
     */
    @GetMapping("/getStationByPumpName")
    public ResponseResult<List<WaterFlowStationVo>> getStationByPumpName(@RequestParam String name) {
        return  ResponseResult.success("查询成功", stWaterGateService.getStationByPumpName(name));
    }

    /**
     * 水位流量站对应的实时水位(m)/流量(m³/s)/设计高水位(m)
     */
    @GetMapping("/getWaterFlowByStation")
    public ResponseResult<List<StationWaterFlowDataVo>> getWaterFlowByStation(@RequestParam String stcd) {
        return  ResponseResult.success("查询成功", stWaterGateService.getWaterFlowByStation(stcd));
    }
}
