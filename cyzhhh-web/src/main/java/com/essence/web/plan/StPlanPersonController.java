package com.essence.web.plan;

import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StPlanPersonService;
import com.essence.interfaces.model.StPlanInfoEsrRes;
import com.essence.interfaces.model.StPlanInfoEsuParam;
import com.essence.interfaces.model.StPlanPersonEsr;
import com.essence.interfaces.model.StPlanPersonEsu;
import com.essence.interfaces.param.StPlanPersonEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方养护人员信息表管理
 * @author liwy
 * @since 2023-07-17 14:52:39
 */
@RestController
@RequestMapping("/stPlanPerson")
public class StPlanPersonController extends BaseController<Long, StPlanPersonEsu, StPlanPersonEsp, StPlanPersonEsr> {
    @Autowired
    private StPlanPersonService stPlanPersonService;

    public StPlanPersonController(StPlanPersonService stPlanPersonService) {
        super(stPlanPersonService);
    }

    /**
     * 新增三方养护人员信息
     * @param stPlanPersonEsu
     * @return
     */
    @PostMapping("addStPlanPerson")
    public ResponseResult addStPlanPerson(@RequestBody StPlanPersonEsu stPlanPersonEsu){

        if (null != stPlanPersonEsu.getId()) {
            //已有id进行修
            return ResponseResult.success("修改成功", stPlanPersonService.updateStPlanPerson(stPlanPersonEsu));
        } else {
            return ResponseResult.success("添加成功", stPlanPersonService.addStPlanPerson(stPlanPersonEsu));
        }
    }
}
