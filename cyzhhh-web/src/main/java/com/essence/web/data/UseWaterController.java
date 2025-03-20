package com.essence.web.data;


import com.essence.interfaces.api.UseWaterService;
import com.essence.interfaces.model.UseWaterEsr;
import com.essence.interfaces.model.UseWaterEsu;
import com.essence.interfaces.param.UseWaterEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用水量管理
 * @author BINX
 * @since 2023-01-04 17:18:00
 */
@RestController
@RequestMapping("/useWater")
public class UseWaterController extends BaseController<String, UseWaterEsu, UseWaterEsp, UseWaterEsr> {
    @Autowired
    private UseWaterService useWaterService;

    public UseWaterController(UseWaterService useWaterService) {
        super(useWaterService);
    }
}
