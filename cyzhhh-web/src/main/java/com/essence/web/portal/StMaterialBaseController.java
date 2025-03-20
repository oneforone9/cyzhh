package com.essence.web.portal;

import com.essence.interfaces.api.StMaterialBaseService;
import com.essence.interfaces.model.StMaterialBaseEsr;
import com.essence.interfaces.model.StMaterialBaseEsu;
import com.essence.interfaces.param.StMaterialBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 防汛物资基础表管理
 * @author liwy
 * @since 2023-04-13 14:57:25
 */
@RestController
@RequestMapping("/stMaterialBase")
public class StMaterialBaseController extends BaseController<Long, StMaterialBaseEsu, StMaterialBaseEsp, StMaterialBaseEsr> {
    @Autowired
    private StMaterialBaseService stMaterialBaseService;

    public StMaterialBaseController(StMaterialBaseService stMaterialBaseService) {
        super(stMaterialBaseService);
    }
}
