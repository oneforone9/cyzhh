package com.essence.web.plan;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StPlanOperateService;
import com.essence.interfaces.model.StPlanOperateEsr;
import com.essence.interfaces.model.StPlanOperateEsu;
import com.essence.interfaces.param.StPlanOperateEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 养护内容记录表管理
 * @author liwy
 * @since 2023-07-24 14:16:34
 */
@RestController
@RequestMapping("/stPlanOperate")
public class StPlanOperateController extends BaseController<Long, StPlanOperateEsu, StPlanOperateEsp, StPlanOperateEsr> {
    @Autowired
    private StPlanOperateService stPlanOperateService;

    public StPlanOperateController(StPlanOperateService stPlanOperateService) {
        super(stPlanOperateService);
    }

    /**
     * 新增养护工单作业录
     *
     * @param list
     * @return
     */
    @PostMapping("/addStPlanOperate")
    @ResponseBody
    public ResponseResult addStPlanOperate(HttpServletRequest request, @Validated(Insert.class) @RequestBody List<StPlanOperateEsu> list) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        return ResponseResult.success("添加成功", stPlanOperateService.addStPlanOperate(list));
    }
}
