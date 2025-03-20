package com.essence.web.caiyun;


import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealTableDto;
import com.essence.interfaces.api.StCaiyunPrecipitationRealService;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsr;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsu;
import com.essence.interfaces.param.StCaiyunPrecipitationRealEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* 彩云预报实时数据
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@RestController
@RequestMapping("/stCaiyunPrecipitationReal")
public class StCaiyunPrecipitationRealController extends BaseController<String, StCaiyunPrecipitationRealEsu, StCaiyunPrecipitationRealEsp, StCaiyunPrecipitationRealEsr> {

    @Autowired
    private StCaiyunPrecipitationRealService stCaiyunPrecipitationRealService;



    public StCaiyunPrecipitationRealController(StCaiyunPrecipitationRealService stCaiyunPrecipitationRealService) {
        super(stCaiyunPrecipitationRealService);
    }


    @GetMapping("/grid")
    public ResponseResult<List<Shiduan>> getGridRainData() {
        return ResponseResult.success("查询网格数据", stCaiyunPrecipitationRealService.getGridRainData());
    }

    @GetMapping("/grid59")
    public ResponseResult<List<Shiduan>> getGridRainData59() {
        return ResponseResult.success("查询网格数据", stCaiyunPrecipitationRealService.getGridRainData59());
    }

    @GetMapping("/table")
    public ResponseResult< List<StCaiyunPrecipitationRealTableDto>> getTableRainData59() {
        return ResponseResult.success("查询网格数据", stCaiyunPrecipitationRealService.getTableRainData59());
    }
}
