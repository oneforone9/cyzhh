package com.essence.web.water;


import com.essence.interfaces.api.StWaterRiskForecastService;
import com.essence.interfaces.model.StWaterRiskForecastEsr;
import com.essence.interfaces.model.StWaterRiskForecastEsu;
import com.essence.interfaces.param.StWaterRiskForecastEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 水系联调-模型风险预报
*
* @author BINX
* @since 2023年5月11日 下午4:00:24
*/
@RestController
@RequestMapping("/stWaterRiskForecast")
public class StWaterRiskForecastController extends BaseController<String, StWaterRiskForecastEsu, StWaterRiskForecastEsp, StWaterRiskForecastEsr> {

    @Autowired
    private StWaterRiskForecastService stWaterRiskForecastService;

    public StWaterRiskForecastController(StWaterRiskForecastService stWaterRiskForecastService) {
        super(stWaterRiskForecastService);
    }
}
