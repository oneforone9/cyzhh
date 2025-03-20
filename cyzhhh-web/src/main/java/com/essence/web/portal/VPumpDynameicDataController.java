package com.essence.web.portal;


import com.essence.interfaces.api.VPumpDynameicDataService;
import com.essence.interfaces.model.VPumpDynameicDataEsr;
import com.essence.interfaces.model.VPumpDynameicDataEsu;
import com.essence.interfaces.param.VPumpDynameicDataEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理
 * @author BINX
 * @since 2023-04-20 17:29:31
 */
@RestController
@RequestMapping("/vPumpDynameicData")
public class VPumpDynameicDataController extends BaseController<String, VPumpDynameicDataEsu, VPumpDynameicDataEsp, VPumpDynameicDataEsr> {
    @Autowired
    private VPumpDynameicDataService vPumpDynameicDataService;

    public VPumpDynameicDataController(VPumpDynameicDataService vPumpDynameicDataService) {
        super(vPumpDynameicDataService);
    }
}
