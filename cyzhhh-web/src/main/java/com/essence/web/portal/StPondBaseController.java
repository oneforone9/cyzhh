package com.essence.web.portal;

import com.essence.interfaces.api.StPondBaseService;
import com.essence.interfaces.model.StPondBaseEsr;
import com.essence.interfaces.model.StPondBaseEsu;
import com.essence.interfaces.param.StPondBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 易积滞水点基础表管理
 * @author liwy
 * @since 2023-04-03 14:45:28
 */
@RestController
@RequestMapping("/stPondBase")
public class StPondBaseController extends BaseController<Long, StPondBaseEsu, StPondBaseEsp, StPondBaseEsr> {
    @Autowired
    private StPondBaseService stPondBaseService;

    public StPondBaseController(StPondBaseService stPondBaseService) {
        super(stPondBaseService);
    }
}
