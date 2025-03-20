package com.essence.web.plan;


import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StPlanOperateRejectService;
import com.essence.interfaces.model.StPlanOperateRejectEsr;
import com.essence.interfaces.model.StPlanOperateRejectEsu;
import com.essence.interfaces.param.StPlanOperateRejectEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 养护内容-驳回记录表管理
 *
 * @author BINX
 * @since 2023-09-11 17:52:34
 */
@RestController
@RequestMapping("/stPlanOperateReject")
public class StPlanOperateRejectController extends BaseController<String, StPlanOperateRejectEsu, StPlanOperateRejectEsp, StPlanOperateRejectEsr> {
    @Autowired
    private StPlanOperateRejectService stPlanOperateRejectService;

    public StPlanOperateRejectController(StPlanOperateRejectService stPlanOperateRejectService) {
        super(stPlanOperateRejectService);
    }

    /**
     * 新增养护工单作业录
     *
     * @param list
     * @return
     */
    @PostMapping("/addList")
    @ResponseBody
    public ResponseResult addStPlanOperate(HttpServletRequest request, @RequestBody List<StPlanOperateRejectEsu> list) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        return ResponseResult.success("添加成功", stPlanOperateRejectService.addStPlanOperate(list));
    }
}
