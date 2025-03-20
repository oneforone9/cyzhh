package com.essence.web.data;


import com.essence.interfaces.api.TDeepWaterService;
import com.essence.interfaces.model.TDeepWaterEsr;
import com.essence.interfaces.model.TDeepWaterEsu;
import com.essence.interfaces.param.TDeepWaterEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地下水埋深管理
 * @author cuirx
 * @since 2023-01-04 14:46:11
 */
@RestController
@RequestMapping("/tDeepWater")
public class TDeepWaterController extends BaseController<String, TDeepWaterEsu, TDeepWaterEsp, TDeepWaterEsr> {
    @Autowired
    private TDeepWaterService tDeepWaterService;

    public TDeepWaterController(TDeepWaterService tDeepWaterService) {
        super(tDeepWaterService);
    }
}
