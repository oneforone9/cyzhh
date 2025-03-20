package com.essence.web.portal;

import com.essence.common.dto.DeviceForRiverDTO;
import com.essence.common.dto.RiverRequestDTO;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.ReaBase;
import com.essence.interfaces.api.ReaBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.entity.ReaBaseDto;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.ReaBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 河道信息表管理
 *
 * @author zhy
 * @since 2022-10-18 17:22:18
 */
@Controller
@RequestMapping("/rea")
public class ReaBaseController extends BaseController<String, ReaBaseEsu, ReaBaseEsp, ReaBaseEsr> {
    @Autowired
    private ReaBaseService reaBaseService;

    public ReaBaseController(ReaBaseService reaBaseService) {
        super(reaBaseService);
    }


    /**
     * 树形结构分页查询
     *
     * @param param
     * @return
     * @apiNote 应用场景 根必须时一级河道 需要添加国定查询条件 reaLevel=1
     */
    @PostMapping("/search/tree")
    @ResponseBody
    public ResponseResult<Paginator<ReaBaseEsrEx>> searchTree(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", reaBaseService.searchTree(param));
    }

    /**
     * 一级河道类型数量统计
     *
     * @return
     * @apiNote 类型对应关系 1河 2沟 3渠
     */
    @GetMapping("/statistics")
    @ResponseBody
    public ResponseResult<List<StatisticsBase>> statistics() {
        return ResponseResult.success("查询成功", reaBaseService.statistics());
    }

    /**
     * 获取河道下的 水位站或者流量站
     *
     */
    @GetMapping("/of/device")
    @ResponseBody
    public ResponseResult< List<DeviceForRiverDTO>> getDeviceOfRiver(RiverRequestDTO riverRequestDTO) {
        return ResponseResult.success("查询成功", reaBaseService.getDeviceOfRiver(riverRequestDTO));
    }

    /**
     * 获取所有 河道详细信息

     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseResult< List<ReaBaseDTO>> getRiverInfoList() {
        return ResponseResult.success("查询成功", reaBaseService.getRiverInfoList());
    }


    /**
     * 巡查一览
     * @return
     */
    @GetMapping("/patrol/view")
    @ResponseBody
    public ResponseResult<List<UnitBaseDTO>> getPatrolPreview() {
        return ResponseResult.success("查询成功", reaBaseService.getPatrolPreview());
    }


    /**
     * 河系概况
     * @return
     */
    @GetMapping("/rea/view")
    @ResponseBody
    public ResponseResult<ReaViewStatisticDto> getReaView(String unitId) {
        return ResponseResult.success("查询成功", reaBaseService.getReaViewData(unitId));
    }

}
