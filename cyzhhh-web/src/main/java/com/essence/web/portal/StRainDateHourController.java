package com.essence.web.portal;

import com.essence.interfaces.api.StRainDateHourService;
import com.essence.interfaces.model.StRainDateHourEsr;
import com.essence.interfaces.model.StRainDateHourEsu;
import com.essence.interfaces.param.StRainDateHourEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小时雨量管理
 *
 * @author tyy
 * @since 2024-07-20 11:04:26
 */
@RestController
@RequestMapping("/stRainDateHour")
public class StRainDateHourController extends BaseController<Long, StRainDateHourEsu, StRainDateHourEsp, StRainDateHourEsr> {
    @Autowired
    private StRainDateHourService stRainDateHourService;

    public StRainDateHourController(StRainDateHourService stRainDateHourService) {
        super(stRainDateHourService);
    }
}
