package com.essence.interfaces.api;


import com.essence.common.dto.RainDateHourDto;
import com.essence.common.dto.RainInfoStatisticDto;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.HdyqDzmGisResp;
import com.essence.interfaces.entity.HourTimeAxisRainVo;
import com.essence.interfaces.model.StRainDateEsr;
import com.essence.interfaces.model.StRainDateEsu;
import com.essence.interfaces.param.StRainDateEsp;

import java.util.List;

/**
 * 服务层
 *
 * @author BINX
 * @since 2023-02-20 14:33:10
 */
public interface StRainDateService extends BaseApi<StRainDateEsu, StRainDateEsp, StRainDateEsr> {
    List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO);

    List<RainDateHourDto> getRainHourData(String stationID, String date);

    RainInfoStatisticDto getRainInfoStatistic();

    HdyqDzmGisResp getRainFallEqualsValueCase(Integer hour) throws Exception;

    /**
     * 雨量等值面
     */
    String getYldzm(Integer hours);

    /**
     * 小时时间轴降雨
     */
    List<HourTimeAxisRainVo> getHourTimeAxis(Integer hour, String stationID);


}
