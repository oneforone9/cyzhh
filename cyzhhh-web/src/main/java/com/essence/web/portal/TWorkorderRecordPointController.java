package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.TWorkorderRecordPointService;
import com.essence.interfaces.model.TWorkorderRecordPointEsr;
import com.essence.interfaces.model.TWorkorderRecordPointEsu;
import com.essence.interfaces.model.WorkorderProcessEsu;
import com.essence.interfaces.param.TWorkorderRecordPointEsp;
import com.essence.interfaces.vaild.Update;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息管理
 * @author liwy
 * @since 2023-05-07 12:10:35
 */
@RestController
@RequestMapping("/tWorkorderRecordPoint")
public class TWorkorderRecordPointController extends BaseController<Long, TWorkorderRecordPointEsu, TWorkorderRecordPointEsp, TWorkorderRecordPointEsr> {
    @Autowired
    private TWorkorderRecordPointService tWorkorderRecordPointService;

    public TWorkorderRecordPointController(TWorkorderRecordPointService tWorkorderRecordPointService) {
        super(tWorkorderRecordPointService);
    }

    /**
     * 判断是否在打卡范围之内、是否是在必打卡点位
     *
     * @param tWorkorderRecordPointEsu
     * @return
     */
    @PostMapping("/selectClockRange")
    @ResponseBody
    public ResponseResult selectClockRange(@RequestBody TWorkorderRecordPointEsu tWorkorderRecordPointEsu) {
        return ResponseResult.success("判断是否在打卡范围之内、是否是在必打卡点位成功", tWorkorderRecordPointService.selectClockRange(tWorkorderRecordPointEsu));
    }


    /**
     * 判断结束工单时是否再所有的必打卡点位完成打卡
     *
     * @param orderId
     * @return
     */
    @GetMapping("/selectIsALLComplete")
    @ResponseBody
    public ResponseResult selectIsALLComplete(@RequestParam String  orderId) {
        return ResponseResult.success("结束工单时是否再所有的必打卡点位完成打卡成功", tWorkorderRecordPointService.selectIsALLComplete(orderId));
    }
}
