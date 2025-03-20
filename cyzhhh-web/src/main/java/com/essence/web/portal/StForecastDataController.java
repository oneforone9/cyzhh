package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StForecastDataService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StForecastDataEsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预警报表记录管理
 * @author majunjie
 * @since 2023-04-17 19:38:57
 */
@RestController
@RequestMapping("/stForecastData")
public class StForecastDataController  {
    @Autowired
    private StForecastDataService stForecastDataService;
    /**
     * 根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectStForecastDataList")
    public ResponseResult<Paginator<StForecastDataEsr>> selectStForecastDataList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stForecastDataService.findByPaginator(param));
    }
}
