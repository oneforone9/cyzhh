package com.essence.web.rest;

import com.essence.common.utils.DateDiff;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StationOutService;
import com.essence.interfaces.dot.GateStationOutVo;
import com.essence.interfaces.dot.StationOutVo;
import com.essence.interfaces.dot.TimeParam;
import com.essence.interfaces.model.CyWeatherForecastOut;
import com.essence.interfaces.model.StRainDateOutSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 对外水位流量站数据
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/11 10:40
 */
@RestController
@RequestMapping("/out")
public class StationOutController {

    @Autowired
    StationOutService stationOutService;

    /**
     * 根据时间段获取水位站流量站数据(时间段小于等于一小时)
     *
     * @param timeParam
     * @return
     */
    @PostMapping("/getStationData")
    public ResponseResult<List<StationOutVo>> getStationData(@RequestBody TimeParam timeParam) {
        return ResponseResult.success("查询成功!", stationOutService.getStationData(timeParam));
    }

    /**
     * 根据时间段获取雨量站雨量数据(时间段小于等于一小时)
     *
     * @param timeParam
     * @return
     */
    @PostMapping("/getRainData")
    public ResponseResult<List<StRainDateOutSelect>> getRainData(@RequestBody TimeParam timeParam) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long min = DateDiff.dateDiff(simpleDateFormat.format(timeParam.getStartTime()), simpleDateFormat.format(timeParam.getEndTime()), "yyyy-MM-dd HH:mm:ss", "min");
        if (min <= 240) {
            return ResponseResult.success("查询成功!", stationOutService.getRainData(timeParam));
        } else {
            return ResponseResult.error("参数有误,时间区间最大限度为4小时!");
        }
    }

    /**
     * 根据时间段获取降雨预警数据
     *
     * @param timeParam
     * @return
     */
    @PostMapping("/getCyWeatherForecast")
    public ResponseResult<List<CyWeatherForecastOut>> getCyWeatherForecast(@RequestBody TimeParam timeParam) {
        return ResponseResult.success("查询成功!", stationOutService.getCyWeatherForecast(timeParam));
    }


    /**
     * 根据时间段获取闸坝监测数据(时间段小于等于一小时)
     *
     * @param timeParam
     * @return
     */
    @PostMapping("/getGateStationData")
    public ResponseResult<List<GateStationOutVo>> getGateStationData(@RequestBody TimeParam timeParam) {
        return ResponseResult.success("查询成功!", stationOutService.getGateStationData(timeParam));
    }
    /**
     * 获取100W随机数
     *
     * @return
     */
    @GetMapping("/getNumber")
    public String  getNumber() {
        return stationOutService.getNumber();
    }

    /**
     * 对外提供泵站 硬件推送的回传数据 一般建议查询一小时范围内的数据
     * @param start
     * @param end
     */
    @GetMapping("/pump/data")
    public void getPumpOut(String start,String end){
        stationOutService.getPumpOut(start,end);
    }
}
