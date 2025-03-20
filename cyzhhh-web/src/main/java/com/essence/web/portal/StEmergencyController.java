package com.essence.web.portal;

import com.essence.interfaces.api.StEmergencyService;
import com.essence.interfaces.model.StEmergencyEsr;
import com.essence.interfaces.model.StEmergencyEsu;
import com.essence.interfaces.param.StEmergencyEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抢险队基本信息表管理
 * @author liwy
 * @since 2023-04-13 16:14:41
 */
@RestController
@RequestMapping("/stEmergency")
public class StEmergencyController extends BaseController<Long, StEmergencyEsu, StEmergencyEsp, StEmergencyEsr> {
    @Autowired
    private StEmergencyService stEmergencyService;

    public StEmergencyController(StEmergencyService stEmergencyService) {
        super(stEmergencyService);
    }
}
