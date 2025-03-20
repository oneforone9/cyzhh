package com.essence.web.portal;


import com.essence.interfaces.api.StGreenReportRelationService;
import com.essence.interfaces.model.StGreenReportRelationEsr;
import com.essence.interfaces.model.StGreenReportRelationEsu;
import com.essence.interfaces.param.StGreenReportRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 绿化保洁工作日志上报表-关联表管理
 * @author liwy
 * @since 2023-03-17 17:19:29
 */
@RestController
@RequestMapping("/stGreenReportRelation")
public class StGreenReportRelationController extends BaseController<Long, StGreenReportRelationEsu, StGreenReportRelationEsp, StGreenReportRelationEsr> {
    @Autowired
    private StGreenReportRelationService stGreenReportRelationService;

    public StGreenReportRelationController(StGreenReportRelationService stGreenReportRelationService) {
        super(stGreenReportRelationService);
    }
}
