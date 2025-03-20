package com.essence.web.station;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StPumpDataService;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StPumpDataEsr;
import com.essence.interfaces.model.StPumpDataEsu;
import com.essence.interfaces.param.PumpFlowChartEsp;
import com.essence.interfaces.param.StPumpDataEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 泵站数据管理
 * @author BINX
 * @since 2023-04-14 11:36:06
 */
@RestController
@RequestMapping("/stPumpData")
public class StPumpDataController extends BaseController<String, StPumpDataEsu, StPumpDataEsp, StPumpDataEsr> {
    @Autowired
    private StPumpDataService stPumpDataService;

    public StPumpDataController(StPumpDataService stPumpDataService) {
        super(stPumpDataService);
    }

    /**
     * 获取泵站列表
     * @return
     */
    @PostMapping("/getPumpList")
    public ResponseResult getPumpList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("泵站流量计流量曲线获取成功!",stPumpDataService.getPumpList(param));
    }

    /**
     * 获取泵站流量计流量曲线
     * @return
     */
    @PostMapping("/getPumpFlowChart")
    public ResponseResult getPumpFlowChart(@RequestBody PumpFlowChartEsp pumpFlowChartEsp) {
        return ResponseResult.success("泵站流量计流量曲线获取成功!",stPumpDataService.getPumpFlowChart(pumpFlowChartEsp));
    }
}
