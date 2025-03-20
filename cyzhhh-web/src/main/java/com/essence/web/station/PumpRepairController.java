package com.essence.web.station;

import com.essence.interfaces.api.PumpRepairService;
import com.essence.interfaces.model.PumpRepairEsr;
import com.essence.interfaces.model.PumpRepairEsu;
import com.essence.interfaces.param.PumpRepairEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 闸坝运行养护记录
 */
@RestController
@RequestMapping("/pump/repair")
public class PumpRepairController  extends BaseController<String, PumpRepairEsu, PumpRepairEsp, PumpRepairEsr> {
    @Autowired
    private PumpRepairService pumpRepairService;

    public PumpRepairController(PumpRepairService pumpRepairService) {
        super(pumpRepairService);
    }
}
