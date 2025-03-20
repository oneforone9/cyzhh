package com.essence.web.portal;

import com.essence.interfaces.api.StGatedamReportRelationService;
import com.essence.interfaces.model.StGatedamReportRelationEsr;
import com.essence.interfaces.model.StGatedamReportRelationEsu;
import com.essence.interfaces.param.StGatedamReportRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 闸坝运行维保日志上报-关联表管理
 * @author liwy
 * @since 2023-03-15 11:56:53
 */
@RestController
@RequestMapping("/stGatedamReportRelation")
public class StGatedamReportRelationController extends BaseController<Long, StGatedamReportRelationEsu, StGatedamReportRelationEsp, StGatedamReportRelationEsr> {
    @Autowired
    private StGatedamReportRelationService stGatedamReportRelationService;

    public StGatedamReportRelationController(StGatedamReportRelationService stGatedamReportRelationService) {
        super(stGatedamReportRelationService);
    }
}
