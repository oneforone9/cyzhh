package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.WorkorderTrackService;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.interfaces.model.WorkorderTrackEsu;
import com.essence.interfaces.param.WorkorderTrackEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 工单巡查轨迹-记录点管理
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */
@Controller
@RequestMapping("/track")
public class WorkorderTrackController extends BaseController<Long, WorkorderTrackEsu, WorkorderTrackEsp, WorkorderTrackEsr> {
    @Autowired
    private WorkorderTrackService workorderTrackService;

    public WorkorderTrackController(WorkorderTrackService workorderTrackService) {
        super(workorderTrackService);
    }

    /**
     * 根据工单主键查全部
     * @apiNote 根据时间正序排列
     * @param orderId
     * @return
     */
    @GetMapping("/search/all/{orderId}")
    @ResponseBody
    public ResponseResult<List<WorkorderTrackEsr>> searchAll(@PathVariable("orderId") String orderId) {
        return ResponseResult.success("查询成功", workorderTrackService.findListByOrderId(orderId));

    }

    /**
     * 根据工单主键查最新一条
     * @param orderId
     * @return
     */
    @GetMapping("/search/newest/{orderId}")
    @ResponseBody
    public ResponseResult<WorkorderTrackEsr> searchNewest(@PathVariable("orderId") String orderId) {
        return ResponseResult.success("查询成功", workorderTrackService.findOneByOrderId(orderId));

    }
}
