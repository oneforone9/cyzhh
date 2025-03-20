package com.essence.web.portal;

import com.essence.interfaces.api.StCrowdRealService;
import com.essence.interfaces.model.StCrowdRealEsr;
import com.essence.interfaces.model.StCrowdRealEsu;
import com.essence.interfaces.param.StCrowdRealEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 清水的河 - 实时游客表管理
 * @author BINX
 * @since 2023-02-28 11:44:07
 */
@RestController
@RequestMapping("/stCrowdReal")
public class StCrowdRealController extends BaseController<Long, StCrowdRealEsu, StCrowdRealEsp, StCrowdRealEsr> {
    @Autowired
    private StCrowdRealService stCrowdRealService;

    public StCrowdRealController(StCrowdRealService stCrowdRealService) {
        super(stCrowdRealService);
    }
}
