package com.essence.web.portal;

import com.essence.interfaces.api.StOfficeBaseRelationService;
import com.essence.interfaces.model.StOfficeBaseRelationEsr;
import com.essence.interfaces.model.StOfficeBaseRelationEsu;
import com.essence.interfaces.param.StOfficeBaseRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 科室联系人表管理
 *
 * @author liwy
 * @since 2023-03-29 14:21:18
 */
@RestController
@RequestMapping("/stOfficeBaseRelation")
public class StOfficeBaseRelationController extends BaseController<Long, StOfficeBaseRelationEsu, StOfficeBaseRelationEsp, StOfficeBaseRelationEsr> {
    @Autowired
    private StOfficeBaseRelationService stOfficeBaseRelationService;

    public StOfficeBaseRelationController(StOfficeBaseRelationService stOfficeBaseRelationService) {
        super(stOfficeBaseRelationService);
    }
}
