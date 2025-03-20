package com.essence.web.water;


import com.essence.common.cache.service.RedisService;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StWaterEngineeringSchedulingCodeService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingDataService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingLeadService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StWaterEngineeringSchedulingEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 水系联调-工程调度
 *
 * @author BINX
 * @since 2023年5月13日 下午3:50:06
 */
@RestController
@RequestMapping("/stWaterEngineeringScheduling")
public class StWaterEngineeringSchedulingController extends BaseController<String, StWaterEngineeringSchedulingEsu, StWaterEngineeringSchedulingEsp, StWaterEngineeringSchedulingEsr> {
    /**
     * 注入redis
     */
    @Autowired
    RedisService redisService;

    @Autowired
    private StWaterEngineeringSchedulingService stWaterEngineeringSchedulingService;
    @Autowired
    private StWaterEngineeringSchedulingDataService stWaterEngineeringSchedulingDataService;
    @Autowired
    private StWaterEngineeringSchedulingLeadService stWaterEngineeringSchedulingLeadService;
    @Autowired
    private StWaterEngineeringSchedulingCodeService stWaterEngineeringSchedulingCodeService;
    public StWaterEngineeringSchedulingController(StWaterEngineeringSchedulingService stWaterEngineeringSchedulingService) {
        super(stWaterEngineeringSchedulingService);
    }
    /**
     * 调度权限查询(查询是否是河道所领导)
     *
     * @param id
     * @return
     */
    @GetMapping("/selectSz/{id}")
    public ResponseResult selectSz(HttpServletRequest request, @PathVariable String id) {
        Boolean type= stWaterEngineeringSchedulingLeadService.selectSz(id);
        return ResponseResult.success("下发成功", type);
    }
    /**
     * 局指下发调令
     *
     * @param stFloodDispatch
     * @return
     */
    @PostMapping("/selectFloodDispatchG")
    public ResponseResult selectFloodDispatchs(HttpServletRequest request, @RequestBody StFloodDispatch stFloodDispatch) {
        stWaterEngineeringSchedulingService.selectFloodDispatchs(stFloodDispatch);
        return ResponseResult.success("下发成功", null);
    }
    /**
     * 河道负责人调度列表查询
     *
     * @param param
     * @return
     */
    @PostMapping("/list")
    public ResponseResult<Paginator<StWaterEngineeringSchedulingCodeEsr>>  list(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功",   stWaterEngineeringSchedulingCodeService.findByPaginator(param));
    }
    /**
     * 根据条件分页查询指令下发记录
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectFloodDispatchDataList")
    public ResponseResult<Paginator<StWaterEngineeringSchedulingDataEsr>> selectFloodDispatchDataList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stWaterEngineeringSchedulingDataService.findByPaginator(param));
    }
    /**
     * 河道所调度接收下发/负责人接收
     *
     * @param stFloodDispatch
     * @return
     */
    @PostMapping("/selectFloodDispatch")
    public ResponseResult selectFloodDispatch(HttpServletRequest request, @RequestBody StFloodDispatch stFloodDispatch) {
        stWaterEngineeringSchedulingService.selectFloodDispatch(stFloodDispatch);
        return ResponseResult.success("下发/接收成功", null);
    }

    /**
     * 调度处理完成
     *
     * @param stWaterEngineeringSchedulingCodeEsu
     * @return
     */
    @PostMapping("/updateFloodDispatch")
    public ResponseResult <StWaterEngineeringSchedulingCodeEsr> updateFloodDispatch(HttpServletRequest request, @RequestBody StWaterEngineeringSchedulingCodeEsu stWaterEngineeringSchedulingCodeEsu) {
        return ResponseResult.success("调度处理完成", stWaterEngineeringSchedulingService.updateFloodDispatch(stWaterEngineeringSchedulingCodeEsu));
    }
    /**
     * 调度（河道和个人）查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/selectFloodDispatchCode/{id}")
    public ResponseResult<StWaterEngineeringSchedulingCodeEsr> selectFloodDispatchCodeById(@PathVariable String id) {
        if (StringUtil.isNotBlank(id)) {
            return ResponseResult.success("查询成功", stWaterEngineeringSchedulingCodeService.selectFloodDispatchCodeById(id));
        } else {
            return ResponseResult.error("参数有误！");
        }
    }
    /**
     * 工程调度
     */
    @GetMapping("/selectList")
    public ResponseResult<List<StWaterEngineeringSchedulingDto>> selectEngineeringScheduling(String caseId) {
//        List<StWaterEngineeringSchedulingDto> cacheList = redisService.getCacheObject("quick:" + caseId);
//        if (CollUtil.isEmpty(cacheList)){
        List<StWaterEngineeringSchedulingDto> cacheList = stWaterEngineeringSchedulingService.selectEngineeringScheduling(caseId);
//        }
        return ResponseResult.success("查询成功", cacheList);
    }


    /**
     * 调度查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/selectFloodDispatch/{id}")
    public ResponseResult<StWaterEngineeringSchedulingEsr> selectFloodDispatchById(@PathVariable String id) {
        if (StringUtil.isNotBlank(id)) {
            return ResponseResult.success("查询成功", stWaterEngineeringSchedulingService.selectFloodDispatchById(id));
        } else {
            return ResponseResult.error("参数有误！");
        }
    }
    /**
     * 调度查询河道
     *
     *
     * @return
     */
    @PostMapping("/getRiverList")
    public ResponseResult <List<StWaterEngineeringSchedulingQuery>> getRiverList(HttpServletRequest request) {
        return ResponseResult.success("调度查询河道", stWaterEngineeringSchedulingService.getRiverList());
    }
    /**
     * 调度查询站点
     *
     *
     * @return
     */
    @PostMapping("/getStcdList")
    public ResponseResult <List<StWaterEngineeringSchedulingQuery>> getStcdList(HttpServletRequest request) {
        return ResponseResult.success("调度查询站点", stWaterEngineeringSchedulingService.getStcdList());
    }

    /**
     * 数据雨量站
     *
     *
     * @return
     */
    @PostMapping("/getStcdLists")
    public ResponseResult  getStcdLists(HttpServletRequest request) {
        stWaterEngineeringSchedulingService.getStcdLists();
        return ResponseResult.success("调度查询站点s",null );
    }
    /**
     * 数据水位流量站
     *
     *
     * @return
     */
    @PostMapping("/getStcdListss")
    public ResponseResult  getStcdListss(HttpServletRequest request,@RequestParam String stcd) {
        stWaterEngineeringSchedulingService.getStcdListss(stcd);
        return ResponseResult.success("调度查询站点s",null );
    }

}
