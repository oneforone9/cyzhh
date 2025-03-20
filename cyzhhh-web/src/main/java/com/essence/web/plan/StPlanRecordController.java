package com.essence.web.plan;

import com.essence.interfaces.api.StPlanRecordService;
import com.essence.interfaces.model.StPlanRecordEsr;
import com.essence.interfaces.model.StPlanRecordEsu;
import com.essence.interfaces.param.StPlanRecordEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 闸坝计划生成记录表管理
 * @author liwy
 * @since 2023-07-18 11:15:16
 */
@RestController
@RequestMapping("/stPlanRecord")
public class StPlanRecordController extends BaseController<Long, StPlanRecordEsu, StPlanRecordEsp, StPlanRecordEsr> {
    @Autowired
    private StPlanRecordService stPlanRecordService;

    public StPlanRecordController(StPlanRecordService stPlanRecordService) {
        super(stPlanRecordService);
    }
}
