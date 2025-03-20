package com.essence.web.plan;

import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StPlanInfoService;
import com.essence.interfaces.api.StSideGateService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StPlanInfoEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 闸坝计划排班信息表管理
 * @author liwy
 * @since 2023-07-13 14:40:21
 */
@RestController
@RequestMapping("/stPlanInfo")
public class StPlanInfoController extends BaseController<Long, StPlanInfoEsu, StPlanInfoEsp, StPlanInfoEsr> {



    @Autowired
    private StPlanInfoService stPlanInfoService;
    @Autowired
    private StSideGateService stSideGateService;

    public StPlanInfoController(StPlanInfoService stPlanInfoService) {
        super(stPlanInfoService);
    }

    /**
     * 增加维护计划列表
     * @param stPlanInfoEsu
     * @return
     */
    @PostMapping("addStPlanInfo")
    public ResponseResult addStPlanInfo(@RequestBody StPlanInfoEsu stPlanInfoEsu){
        stPlanInfoEsu.setGmtCreate(new Date());
        String whTime = stPlanInfoEsu.getWhTime();
        whTime = whTime.substring(0,whTime.length()-1);
        stPlanInfoEsu.setWhTime(whTime);
        StSideGateEsr stSideGateEsr = stSideGateService.findById(stPlanInfoEsu.getSideGateId());
        stPlanInfoEsu.setLgtd(stSideGateEsr.getLgtd());
        stPlanInfoEsu.setLttd(stSideGateEsr.getLttd());
        int insert = stPlanInfoService.insert(stPlanInfoEsu);
        return ResponseResult.success("添加成功", insert);
    }

    /**
     * 编辑维护计划列表
     * @param stPlanInfoEsu
     * @return
     */
    @PostMapping("editStPlanInfo")
    public ResponseResult editStPlanInfo(@RequestBody StPlanInfoEsu stPlanInfoEsu){
        String whTime = stPlanInfoEsu.getWhTime();
        if(whTime.endsWith(",")){
            whTime = whTime.substring(0,whTime.length()-1);
            stPlanInfoEsu.setWhTime(whTime);
            stPlanInfoEsu.setGmtCreate(new Date());
        }
        Integer sideGateId = stPlanInfoEsu.getSideGateId();
        if(null !=sideGateId && "".equals(sideGateId)){
            StSideGateEsr stSideGateEsr = stSideGateService.findById(sideGateId);
            stPlanInfoEsu.setLgtd(stSideGateEsr.getLgtd());
            stPlanInfoEsu.setLttd(stSideGateEsr.getLttd());
        }
        int update = stPlanInfoService.update(stPlanInfoEsu);
        return ResponseResult.success("更新成功", update);
    }


    /**
     * 查询维护计划列表
     * @param param
     * @return
     */
    @PostMapping("getStPlanInfo")
    public ResponseResult<PageUtil<StPlanInfoEsrRes>> getStPlanInfo(@RequestBody StPlanInfoEsuParam param){
        PageUtil<StPlanInfoEsrRes> p = stPlanInfoService.getStPlanInfo(param);
        return ResponseResult.success("查询成功", p);
    }







}
