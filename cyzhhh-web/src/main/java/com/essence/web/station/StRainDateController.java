package com.essence.web.station;


import com.essence.common.dto.*;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StRainDateService;
import com.essence.interfaces.dot.HdyqDzmGisResp;
import com.essence.interfaces.entity.HourTimeAxisRainVo;
import com.essence.interfaces.model.StRainDateEsr;
import com.essence.interfaces.model.StRainDateEsu;
import com.essence.interfaces.param.StRainDateEsp;
import com.essence.web.basecontroller.BaseController;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 雨量
 *
 * @author BINX
 * @since 2023-02-20 14:33:06
 */
@RestController
@RequestMapping("/stRainDate")
public class StRainDateController extends BaseController<Long, StRainDateEsu, StRainDateEsp, StRainDateEsr> {
    @Autowired
    private StRainDateService stRainDateService;

    public StRainDateController(StRainDateService stRainDateService) {
        super(stRainDateService);
    }

    /**
     * 查询场站下具体的数据 需要传递 测站stcd sttp
     *
     * @param stStbprpBEntityDTO
     * @return
     */
    @PostMapping("data/list")
    public ResponseResult<List<StWaterRateEntityDTO>> getStationDataList(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateEntityDTO> stationInfoList = stRainDateService.getStationDataList(stStbprpBEntityDTO);
        return ResponseResult.success("获取成功", stationInfoList);
    }

    /**
     * 小时雨量
     *
     * @throws UnirestException
     */
    @GetMapping("/hour/list")
    public ResponseResult<List<RainDateHourDto>> getStationHourList(String stationID, @RequestParam(required = false) String date) throws UnirestException {
        List<RainDateHourDto> stationInfoList = stRainDateService.getRainHourData(stationID, date);
        return ResponseResult.success("获取成功", stationInfoList);
    }

    /**
     * 雨量统计
     *
     * @throws UnirestException
     */
    @GetMapping("/info/statistic")
    public ResponseResult<RainInfoStatisticDto> getRainInfoStatistic() throws UnirestException {
        RainInfoStatisticDto stationInfoList = stRainDateService.getRainInfoStatistic();
        return ResponseResult.success("获取成功", stationInfoList);
    }

    /**
     * 朝阳降雨等值面-旧
     *
     * @return
     */
    @GetMapping("/equals/hand")
    public ResponseResult getRainFallEqualsValueCase(Integer hour) throws Exception {
        HdyqDzmGisResp hdyqDzmGisResp = stRainDateService.getRainFallEqualsValueCase(hour);
        return ResponseResult.success("获取成功", hdyqDzmGisResp);
    }

    /**
     * 雨量等值面-新
     */
    @GetMapping("/yldzm")
    public ResponseResult<String> getYldzm(Integer hours) {
        return ResponseResult.success("查询成功", stRainDateService.getYldzm(hours));
    }

    /**
     * 小时时间轴降雨
     *
     * @param hour      前后多少小时
     * @param stationID 站点id,不传查全部
     */
    @GetMapping("/hourTimeAxis")
    public ResponseResult<List<HourTimeAxisRainVo>> getHourTimeAxis(Integer hour, @RequestParam(required = false) String stationID) {
        List<HourTimeAxisRainVo> list = stRainDateService.getHourTimeAxis(hour, stationID);
        return ResponseResult.success("获取成功", list);
    }


}
