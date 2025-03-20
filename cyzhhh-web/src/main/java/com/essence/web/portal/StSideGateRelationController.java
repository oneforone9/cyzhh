package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StSideGateRelationService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.model.StSideGateEsrRes;
import com.essence.interfaces.model.StSideGateEsuParam;
import com.essence.interfaces.model.StSideGateRelationEsr;
import com.essence.interfaces.model.StSideGateRelationEsu;
import com.essence.interfaces.param.StSideGateRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 闸坝负责人关联表管理
 * @author liwy
 * @since 2023-04-13 17:50:09
 */
@RestController
@RequestMapping("/stSideGateRelation")
public class StSideGateRelationController extends BaseController<Long, StSideGateRelationEsu, StSideGateRelationEsp, StSideGateRelationEsr> {
    @Autowired
    private StSideGateRelationService stSideGateRelationService;

    public StSideGateRelationController(StSideGateRelationService stSideGateRelationService) {
        super(stSideGateRelationService);
    }

    /**
     * 编辑闸坝负责人信息
     * @param stSideGateRelationEsu
     * @return
     */
    @PostMapping("updateStSideGateRelation")
    public ResponseResult updateStSideGateRelation(@RequestBody StSideGateRelationEsu stSideGateRelationEsu){
        return ResponseResult.success("编辑成功", stSideGateRelationService.updateStSideGateRelation(stSideGateRelationEsu));
    }

}
