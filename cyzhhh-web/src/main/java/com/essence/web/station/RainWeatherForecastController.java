package com.essence.web.station;


import com.essence.interfaces.api.CyWeatherForecastService;
import com.essence.interfaces.model.CyWeatherForecastEsr;
import com.essence.interfaces.model.CyWeatherForecastEsu;
import com.essence.interfaces.param.CyWeatherForecastEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理
 * @author BINX
 * @since 2023-03-16 16:41:52
 */
@RestController
@RequestMapping("/rain/forecast")
public class RainWeatherForecastController extends BaseController<Long, CyWeatherForecastEsu, CyWeatherForecastEsp, CyWeatherForecastEsr> {
    @Autowired
    private CyWeatherForecastService cyWeatherForecastService;

    public RainWeatherForecastController(CyWeatherForecastService cyWeatherForecastService) {
        super(cyWeatherForecastService);
    }
}
