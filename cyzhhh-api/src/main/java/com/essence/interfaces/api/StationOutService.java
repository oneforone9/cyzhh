package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.GateStationOutVo;
import com.essence.interfaces.dot.StationOutVo;
import com.essence.interfaces.dot.TimeParam;
import com.essence.interfaces.model.CyWeatherForecastOut;
import com.essence.interfaces.model.StRainDateOutSelect;
import com.essence.interfaces.model.StationOutEsr;
import com.essence.interfaces.model.StationOutEsu;
import com.essence.interfaces.param.StationOutEsp;

import java.util.List;

/**
 * 水位流量站(对外)服务层
 * @author BINX
 * @since 2023-05-11 10:37:28
 */
public interface StationOutService extends BaseApi<StationOutEsu, StationOutEsp, StationOutEsr> {
    List<StationOutVo> getStationData(TimeParam timeParam);

    List<StRainDateOutSelect> getRainData(TimeParam timeParam);

    List<CyWeatherForecastOut> getCyWeatherForecast(TimeParam timeParam);

    List<GateStationOutVo> getGateStationData(TimeParam timeParam);

    String getNumber();

    void getPumpOut(String start, String end);

}
