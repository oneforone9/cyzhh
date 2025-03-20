package com.essence.web.portal;


import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForecastService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 预警报表管理
 *
 * @author majunjie
 * @since 2023-04-17 19:38:13
 */
@RestController
@RequestMapping("/stForecast")
public class StForecastController {
    @Autowired
    private StForecastService stForecastService;

    /**
     * 新增
     *
     * @param stForecastEsu
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<StForecastEsr> addStForecast(HttpServletRequest request, @RequestBody @Validated(Insert.class) StForecastEsu stForecastEsu) {
        return ResponseResult.success("添加成功", stForecastService.addStForecast(stForecastEsu));
    }

    /**
     * 修改
     *
     * @param stForecastEsu
     * @return
     */
    @PostMapping("/update")
    public ResponseResult<StForecastEsr> updateStForecast(HttpServletRequest request, @RequestBody @Validated(Update.class) StForecastEsu stForecastEsu) {
        return ResponseResult.success("修改成功", stForecastService.updateStForecast(stForecastEsu));
    }

    /**
     * 查询详情
     *
     * @param forecastId
     * @return
     */
    @GetMapping("/selectStForecast/{forecastId}")
    public ResponseResult<StForecastEsr> selectStForecast(@PathVariable String forecastId) {
        StForecastEsr stForecastEsr = stForecastService.selectStForecast(forecastId);
        if (StringUtil.isNotBlank(stForecastEsr.getForecastId())){
            return ResponseResult.success("查询成功",stForecastEsr);
        }else {
            return ResponseResult.error("预警id不存在！");
        }

    }

    /**
     * 预警接收和发送
     *
     * @param stForecastReception
     * @return
     */
    @PostMapping("/reception")
    public ResponseResult receptionStForecast(HttpServletRequest request, @RequestBody StForecastReception stForecastReception) {
        stForecastService.receptionStForecast(stForecastReception);
        return ResponseResult.success("接收/发送成功", null);
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectStForecastList")
    public ResponseResult<Paginator<StForecastEsr>> selectStForecastList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stForecastService.findByPaginator(param));
    }

    /**
     * 根据设备类型和设备编号查询关联责任人(闸坝才有)
     *
     * @param stForecast
     * @return R
     */
    @PostMapping("/selectStForecastPerson")
    public ResponseResult<List<StForecastPerson>> selectStForecastPerson(@RequestBody StForecast stForecast) {
        return ResponseResult.success("查询成功", stForecastService.selectStForecastPerson(stForecast));
    }

    /**
     * 根据设备坐标 查询关联设备
     *
     * @param stForecastRelevance
     * @return R
     */
    @PostMapping("/selectStForecastRelevanceList")
    public ResponseResult<List<StForecastRelevanceList>> selectStForecastRelevanceList(@RequestBody StForecastRelevance stForecastRelevance) {
        return ResponseResult.success("查询成功", stForecastService.selectStForecastRelevanceList(stForecastRelevance));
    }

}
