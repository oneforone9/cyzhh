package com.essence.web.flood;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.interfaces.api.FloodIncidentService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.FloodIncidentEsr;
import com.essence.interfaces.model.FloodIncidentEsu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 积水事件处置
 *
 * @author zhy
 * @since 2024-07-17 19:32:43
 */
@RestController
@RequestMapping("/floodIncident")
public class FloodIncidentController {

    @Autowired
    private FloodIncidentService floodIncidentService;

    /**
     * 新增（排水中心+防御科）
     *
     * @param e
     * @return
     */
    @PostMapping("/add")
    public ResponseResult insert(@Validated(Insert.class) @RequestBody FloodIncidentEsu e, HttpServletRequest request) {
        FloodIncidentEsu floodIncidentEsu = setUserData(e, request);
        String operateUserMobile = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_MOBILE);
        floodIncidentEsu.setApplicationPhone(operateUserMobile);
        return ResponseResult.success("添加成功", floodIncidentService.insert(floodIncidentEsu));
    }


    /**
     * 防御科审核或驳回
     * @param e
     * @param request
     */
    @PostMapping("/auditOrReject")
    public ResponseResult auditOrReject(@Validated(Update.class)  @RequestBody FloodIncidentEsu e, HttpServletRequest request) {
        FloodIncidentEsu floodIncidentEsu = setUserData(e, request);
        floodIncidentService.auditOrReject(floodIncidentEsu);
        return ResponseResult.success("审核或驳回成功", 1);
    }


    /**
     * 抢险队伍接受或拒绝
     * @param e
     * @param request
     */
    @PostMapping("/acceptOrRefuse")
    public ResponseResult acceptOrRefuse(@Validated(Update.class)  @RequestBody FloodIncidentEsu e, HttpServletRequest request) {
        FloodIncidentEsu floodIncidentEsu = setUserData(e, request);
        floodIncidentService.acceptOrRefuse(floodIncidentEsu);
        return ResponseResult.success("审核或驳回成功", 1);
    }

    /**
     * 抢险队伍，更新到达时间
     * @param e
     * @param request
     */
    @PostMapping("/updateRescueTime")
    public ResponseResult updateRescueTime(@Validated(Update.class)  @RequestBody FloodIncidentEsu e, HttpServletRequest request) {
        FloodIncidentEsu floodIncidentEsu = setUserData(e, request);
        floodIncidentService.updateRescueTime(floodIncidentEsu);
        return ResponseResult.success("更新到达时间成功", 1);
    }

    /**
     * 抢险队伍，更新完成时间
     * @param e
     * @param request
     */
    @PostMapping("/finishRescueTime")
    public ResponseResult finishRescueTime(@Validated(Update.class)  @RequestBody FloodIncidentEsu e, HttpServletRequest request) {
        FloodIncidentEsu floodIncidentEsu = setUserData(e, request);
        floodIncidentService.finishRescueTime(floodIncidentEsu);
        return ResponseResult.success("更新完成时间成功",1);
    }

    /**
     * 赋予登录用户信息
     * @param e
     * @param request
     * @return
     */
    public FloodIncidentEsu setUserData(FloodIncidentEsu e, HttpServletRequest request) {
        String operateUserId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String operateUserName = (String) request.getSession().getAttribute(SysConstant.CURRENT_LOGIN_NAME);
        String operateCorpId = (String) request.getSession().getAttribute(SysConstant.CURRENT_UNIT_ID);
        String operateCorpName = (String) request.getSession().getAttribute(SysConstant.CURRENT_UNIT_NAME);
        e.setOperateUserId(operateUserId);
        e.setOperateUserName(operateUserName);
        e.setOperateUserCorpId(operateCorpId);
        e.setOperateUserCorpName(operateCorpName);
        return e;
    }

    /**
     * 更新
     *
     * @param e
     * @return
     */
    @PostMapping("/update")
    public ResponseResult update(@Validated(Update.class) @RequestBody FloodIncidentEsu e) {
        return ResponseResult.success("更新成功", floodIncidentService.update(e));
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        return ResponseResult.success("删除成功", floodIncidentService.deleteById((Serializable) id));
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/search/{id}")
    public ResponseResult<FloodIncidentEsr> search(@PathVariable Long id) {
        return ResponseResult.success("查询成功", floodIncidentService.findById((Serializable) id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/search")
    public ResponseResult<Paginator<FloodIncidentEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", floodIncidentService.findByPaginator(param));
    }

}
