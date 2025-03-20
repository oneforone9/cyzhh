package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.ReaFocusGeomService;
import com.essence.interfaces.api.WorkorderRecordGeomService;
import com.essence.interfaces.model.ReaFocusGeomEsr;
import com.essence.interfaces.model.ReaFocusGeomEsu;
import com.essence.interfaces.model.WorkorderRecordGeomEsr;
import com.essence.interfaces.param.ReaFocusGeomEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 河道重点位置地理信息表管理
 *
 * @author zhy
 * @since 2022-10-26 14:06:39
 */
@Controller
@RequestMapping("/rea/focus/geom")
public class ReaFocusGeomController extends BaseController<String, ReaFocusGeomEsu, ReaFocusGeomEsp, ReaFocusGeomEsr> {
    @Autowired
    private ReaFocusGeomService reaFocusGeomService;
    @Autowired
    private WorkorderRecordGeomService workorderRecordGeomService;


    public ReaFocusGeomController(ReaFocusGeomService reaFocusGeomService) {
        super(reaFocusGeomService);
    }

    /**
     * 根据河道主键查询（不含空间数据）
     *
     * @param reaId 河道主键
     * @return R
     */
    @GetMapping("/search/nogeom/{reaId}")
    @ResponseBody
    public ResponseResult<List<ReaFocusGeomEsr>> searchNogeom(@PathVariable String reaId) {
        return ResponseResult.success("查询成功", reaFocusGeomService.findByReaId(reaId));
    }

    /**
     * 根据工单主键查询
     *
     * @param orderId 工单主键查询
     * @return
     */
    @GetMapping("/search/order/{orderId}")
    @ResponseBody
    public ResponseResult<List<WorkorderRecordGeomEsr>> searchByOrder(@PathVariable String orderId) {
        return ResponseResult.success("查询成功", workorderRecordGeomService.findByOrderId(orderId));
    }
}
