package com.essence.web.portal;


import com.essence.interfaces.api.StCompanyBaseRelationService;
import com.essence.interfaces.model.StCompanyBaseRelationEsr;
import com.essence.interfaces.model.StCompanyBaseRelationEsu;
import com.essence.interfaces.param.StCompanyBaseRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理
 * @author BINX
 * @since 2023-02-16 11:59:29
 */
@RestController
@RequestMapping("/stCompanyBaseRelation")
public class StCompanyBaseRelationController extends BaseController<Long, StCompanyBaseRelationEsu, StCompanyBaseRelationEsp, StCompanyBaseRelationEsr> {
    @Autowired
    private StCompanyBaseRelationService stCompanyBaseRelationService;

    public StCompanyBaseRelationController(StCompanyBaseRelationService stCompanyBaseRelationService) {
        super(stCompanyBaseRelationService);
    }
}
