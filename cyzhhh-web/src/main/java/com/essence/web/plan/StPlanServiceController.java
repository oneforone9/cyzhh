package com.essence.web.plan;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StPlanServiceService;
import com.essence.interfaces.model.EventCompanyEsu;
import com.essence.interfaces.model.EventCompanyRes;
import com.essence.interfaces.model.StPlanServiceEsr;
import com.essence.interfaces.model.StPlanServiceEsu;
import com.essence.interfaces.param.StPlanServiceEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 闸坝维护计划基础表管理
 * @author liwy
 * @since 2023-07-13 14:46:18
 */
@RestController
@RequestMapping("/stPlanService")
public class StPlanServiceController extends BaseController<Long, StPlanServiceEsu, StPlanServiceEsp, StPlanServiceEsr> {
    @Autowired
    private StPlanServiceService stPlanServiceService;

    public StPlanServiceController(StPlanServiceService stPlanServiceService) {
        super(stPlanServiceService);
    }

    /**
     * 根据类型获取设备设施名称
     * @return
     */
    @PostMapping("selectEquipmentName")
    public ResponseResult<List<StPlanServiceEsr>> selectEquipmentName(@RequestBody StPlanServiceEsu stPlanServiceEsu){
        List<StPlanServiceEsr> list = stPlanServiceService.selectEquipmentName(stPlanServiceEsu);
        return ResponseResult.success("查询成功",list);
    }

    /**
     * 根据设施名称和类型获取日常维护内容
     * @return
     */
    @PostMapping("selectServiceContent")
    public ResponseResult<List<StPlanServiceEsr>> selectServiceContent(@RequestBody StPlanServiceEsu stPlanServiceEsu){
        List<StPlanServiceEsr> list = stPlanServiceService.selectServiceContent(stPlanServiceEsu);
        return ResponseResult.success("查询成功",list);
    }


}
