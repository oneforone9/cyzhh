package com.essence.web.alg;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StCaseBaseInfoService;
import com.essence.interfaces.dot.EffectCaseStatisticDto;
import com.essence.interfaces.dot.EffectRequestDto;
import com.essence.interfaces.dot.ForecastPerformanceDto;
import com.essence.interfaces.model.StCaseBaseInfoEsr;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.param.StCaseBaseInfoEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 防汛调度-方案基础表管理
 * @author BINX
 * @since 2023-04-17 16:29:52
 */
@RestController
@RequestMapping("/stCaseBaseInfo")
public class StCaseBaseInfoController extends BaseController<String, StCaseBaseInfoEsu, StCaseBaseInfoEsp, StCaseBaseInfoEsr> {
    @Autowired
    private StCaseBaseInfoService stCaseBaseInfoService;

    public StCaseBaseInfoController(StCaseBaseInfoService stCaseBaseInfoService) {
        super(stCaseBaseInfoService);
    }

    /**
     * 新增
     *
     * @param e 实体
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(@RequestBody StCaseBaseInfoEsu e) {
        return ResponseResult.success("添加成功", stCaseBaseInfoService.insertCase(e));

    }

    /**
     *  方案模型计算
     *
     * @return
     */
    @PostMapping("/case/execute")
    public ResponseResult execute(@RequestBody StCaseBaseInfoEsu stCaseBaseInfoEsu) throws IOException {
        stCaseBaseInfoService.execute(stCaseBaseInfoEsu);
        return ResponseResult.success("模型算法调度成功",null);
    }



    /**
     *  获取水位站流量站 站点断面名称
     *
     * @return
     */
    @GetMapping("/section/name")
    public ResponseResult getCaseTypeStatistic() throws IOException {
        List<String> forecastSectionName = stCaseBaseInfoService.getForecastSectionName();
        return ResponseResult.success("查询成功",forecastSectionName);
    }
}
