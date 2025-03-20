package com.essence.web.portal;

import com.essence.interfaces.api.StOfficeBaseService;
import com.essence.interfaces.model.StOfficeBaseEsr;
import com.essence.interfaces.model.StOfficeBaseEsu;
import com.essence.interfaces.param.StOfficeBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 科室基础表管理
 * @author liwy
 * @since 2023-03-29 14:20:43
 */
@RestController
@RequestMapping("/stOfficeBase")
public class StOfficeBaseController extends BaseController<Long, StOfficeBaseEsu, StOfficeBaseEsp, StOfficeBaseEsr> {
    @Autowired
    private StOfficeBaseService stOfficeBaseService;

    public StOfficeBaseController(StOfficeBaseService stOfficeBaseService) {
        super(stOfficeBaseService);
    }

}
