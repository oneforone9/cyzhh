package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.StWaterGaugeDto;
import com.essence.interfaces.api.StWaterGaugeService;
import com.essence.interfaces.model.StWaterGaugeEsr;
import com.essence.interfaces.model.StWaterGaugeEsu;
import com.essence.interfaces.model.StWaterGaugeEsuParam;
import com.essence.interfaces.param.StWaterGaugeEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 电子水尺积水台账管理
 *
 * @author liwy
 * @since 2023-05-11 18:39:10
 */
@RestController
@RequestMapping("/stWaterGauge")
public class StWaterGaugeController extends BaseController<Long, StWaterGaugeEsu, StWaterGaugeEsp, StWaterGaugeEsr> {
    @Autowired
    private StWaterGaugeService stWaterGaugeService;

    public StWaterGaugeController(StWaterGaugeService stWaterGaugeService) {
        super(stWaterGaugeService);
    }


    /**
     * 道路积水点列表(站点信息,水深,报警等)
     *
     * @param name       站点名称
     * @param warnStatus 报警状态-汉字
     * @return
     */
    @GetMapping("/stWaterGaugeNow")
    public ResponseResult<List<StWaterGaugeDto>> stWaterGaugeNow(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "warnStatus", required = false) String warnStatus) {
        return ResponseResult.success("查询成功", stWaterGaugeService.stWaterGaugeNow(name, warnStatus));
    }


    /**
     * 根据水务感知码获取道路积水点一段时间内的积水信息
     *
     * @return
     */
    @PostMapping("/stWaterGaugeNowByCondition")
    public ResponseResult<Object> stWaterGaugeNowByCondition(@RequestBody StWaterGaugeEsuParam stWaterGaugeEsuParam) {
        return ResponseResult.success("查询成功", stWaterGaugeService.stWaterGaugeNowByCondition(stWaterGaugeEsuParam));
    }
}
