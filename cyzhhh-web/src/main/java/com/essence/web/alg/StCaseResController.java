package com.essence.web.alg;


import com.essence.common.cache.service.RedisService;
import com.essence.common.utils.FileCacheUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StCaseResService;
import com.essence.interfaces.dot.*;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StCaseResEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 方案执行结果表管理
 *
 * @author BINX
 * @since 2023-04-18 14:39:11
 */
@RestController
@RequestMapping("/stCaseRes")
public class StCaseResController extends BaseController<String, StCaseResEsu, StCaseResEsp, StCaseResEsr> {
    @Autowired
    private StCaseResService stCaseResService;
    @Autowired
    private RedisService redisService;

    public StCaseResController(StCaseResService stCaseResService) {
        super(stCaseResService);
    }


    /**
     * 预报预演
     *
     * @return
     */
    @PostMapping("/rain/tendency")
    public ResponseResult<List<ForecastPerformanceDto>> getCaseTypeStatistic(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) throws IOException {
        List<ForecastPerformanceDto> caseTypeStatistic = stCaseResService.getRainTendency(stCaseBaseInfoEsu);
        return ResponseResult.success("查询成功", caseTypeStatistic);
    }

    /**
     * 预警统计
     *
     * @param stCaseBaseInfoEsu
     * @return
     */
    @PostMapping("/water/warning")
    public ResponseResult<WaringStatisticDto> getWaterWarning(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        return ResponseResult.success("查询成功", stCaseResService.getWaterWarning(stCaseBaseInfoEsu));
    }

    /**
     * 预警统计 - 河道溢流
     *
     * @param stCaseBaseInfoEsu
     * @return
     */
    @PostMapping("/water/overFlowList")
    public ResponseResult<OverFlowListDto> getOverFlowList(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        return ResponseResult.success("查询成功", stCaseResService.getOverFlowList(stCaseBaseInfoEsu));
    }

    /**
     * 预报水位
     *
     * @param stCaseBaseInfoEsu
     * @return
     */
    @PostMapping("/water/forecast")
    public ResponseResult<List<WaterForcastDto>> getWaterForecast(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        return ResponseResult.success("查询成功", stCaseResService.getWaterForecast(stCaseBaseInfoEsu));
    }


    /**
     * 洪峰信息
     *
     * @return
     */
    @PostMapping("/water/floodPeak")
    public ResponseResult<Object> getFloodPeak(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stCaseResService.getFloodPeak(param));
    }

    @PostMapping("/flow/floodPeak")
    public ResponseResult<List<RiverGateMaxFlowViewEsr>> getFloodPeak(@RequestBody StCaseResEsu cs) {
        return ResponseResult.success("查询成功", stCaseResService.getFloodFlowPeak(cs));
    }

    /**
     * 河道断面
     *
     * @return
     */
    @PostMapping("/river/section")
    public ResponseResult<List<RiverStepSectionDto>> getRiverSection(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) throws ParseException {
        List<RiverStepSectionDto> riverSection;
        riverSection = redisService.getCacheObject("RiverSection@" + stCaseBaseInfoEsu.getId() + "-" + stCaseBaseInfoEsu.getRiverId());
        if (riverSection == null) {
            riverSection = stCaseResService.getRiverSection(stCaseBaseInfoEsu);
        }
        return ResponseResult.success("查询成功", riverSection);
    }

    /**
     * 河道断面实时水位
     *
     * @return
     */
    @PostMapping("/river/fact/section")
    public ResponseResult<List<RiverStepSectionDto>> getRiverFactSection(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        List<RiverStepSectionDto> riverStepSectionDtos = FileCacheUtils.get("quick_fact" + stCaseBaseInfoEsu.getId() + "_" + stCaseBaseInfoEsu.getRiverId(), RiverStepSectionDto.class);
        return ResponseResult.success("查询成功", riverStepSectionDtos);
    }

    /**
     * 方案实测降雨查询
     *
     * @param stCaseResRainQuery
     * @return
     */
    @PostMapping("/selectRain")
    public ResponseResult<StCaseResRainLists> selectRain(HttpServletRequest request, @RequestBody StCaseResRainQuery stCaseResRainQuery) {
        return ResponseResult.success("查询成功", stCaseResService.selectRain(stCaseResRainQuery));
    }

    /**
     * 方案实测流量查询
     *
     * @param stCaseResRainQuery
     * @return
     */
    @PostMapping("/selectFlow")
    public ResponseResult<List<StCaseResFlowList>> selectFlow(HttpServletRequest request, @RequestBody StCaseResRainQuery stCaseResRainQuery) {
        return ResponseResult.success("查询成功", stCaseResService.selectFlow(stCaseResRainQuery));
    }

    /**
     * 方案实测水位查询
     *
     * @param stCaseResRainQuery
     * @return
     */
    @PostMapping("/selectWater")
    public ResponseResult<List<StCaseResWaterList>> selectWater(HttpServletRequest request, @RequestBody StCaseResRainQuery stCaseResRainQuery) {
        return ResponseResult.success("查询成功", stCaseResService.selectWater(stCaseResRainQuery));
    }

    /**
     * 预报降雨雨型数据
     *
     * @param caseId
     * @return
     */

    @PostMapping("/selectRainFallPattern")
    public ResponseResult<StCaseResRainLists> selectRainFallPattern(HttpServletRequest request, @RequestParam String caseId) {
        return ResponseResult.success("查询成功", stCaseResService.selectRainFallPattern(caseId));

    }

    /**
     * 实时水位
     *
     * @param rvid
     * @param caseId
     * @return
     * @throws ParseException
     */
    @GetMapping("riverGateWaterLevel")
    public ResponseResult<List<RiverGateWaterDto>> getRiverGateWaterLevel(String rvid, @RequestParam(required = false) String caseId) throws ParseException {
        List<RiverGateWaterDto> list = stCaseResService.getRiverGateWaterLevel(rvid, caseId);
        return ResponseResult.success("查询成功", list);
    }

    /**
     * 修补曲线 作用：当模型运行的曲线达不到效果人为干预处理一下，从原有曲线 copy+数值 = 新曲线
     *
     * @param oldCaseId   原有曲线
     * @param newCaseName 新曲线名称
     */
    public void strategyFixCurve(String oldCaseId, String newCaseName) {
        stCaseResService.strategyFixCurve(oldCaseId, newCaseName);
    }
}
