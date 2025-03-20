package com.essence.web.portal;

import com.essence.interfaces.api.StWaterPortService;
import com.essence.interfaces.model.StWaterPortEsr;
import com.essence.interfaces.model.StWaterPortEsu;
import com.essence.interfaces.param.StWaterPortEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 补水口基础表管理
 * @author BINX
 * @since 2023-02-22 17:12:28
 */
@RestController
@RequestMapping("/stWaterPort")
public class StWaterPortController extends BaseController<Long, StWaterPortEsu, StWaterPortEsp, StWaterPortEsr> {
    @Autowired
    private StWaterPortService stWaterPortService;

    public StWaterPortController(StWaterPortService stWaterPortService) {
        super(stWaterPortService);
    }
}
