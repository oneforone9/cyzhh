package com.essence.web.plan;

import com.essence.interfaces.api.StLibraryTypeService;
import com.essence.interfaces.model.StLibraryTypeEsr;
import com.essence.interfaces.model.StLibraryTypeEsu;
import com.essence.interfaces.param.StLibraryTypeEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 文库类型基础表管理
 * @author liwy
 * @since 2023-08-17 10:31:08
 */
@RestController
@RequestMapping("/stLibraryType")
public class StLibraryTypeController extends BaseController<Long, StLibraryTypeEsu, StLibraryTypeEsp, StLibraryTypeEsr> {
    @Autowired
    private StLibraryTypeService stLibraryTypeService;

    public StLibraryTypeController(StLibraryTypeService stLibraryTypeService) {
        super(stLibraryTypeService);
    }
}
