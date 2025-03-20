package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.VGateDataService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StSideGateEsuParam;
import com.essence.interfaces.model.StSideGateEsuParamv;
import com.essence.interfaces.model.VGateDataEsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实时闸数据管理
 * @author majunjie
 * @since 2023-04-20 17:37:14
 */
@RestController
@RequestMapping("/vGateData")
public class VGateDataController  {
    @Autowired
    private VGateDataService vGateDataService;
    /**
     * 根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectvGateDataList")
    public ResponseResult<Paginator<VGateDataEsr>> selectvGateDataList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", vGateDataService.findByPaginator(param));
    }

    /**
     * 闸坝运行工况
     * @param stSideGateEsuParamv
     * @return
     */
    @PostMapping("/selectvGateDataList2")
    public ResponseResult selectvGateDataList2(@RequestBody StSideGateEsuParamv stSideGateEsuParamv) {
        return ResponseResult.success("查询成功", vGateDataService.selectvGateDataList2(stSideGateEsuParamv));
    }

    /**
     * 获取闸坝运行工况根据 stcd
     * @param stcd
     * @return
     */
    @GetMapping("/selectvGateData")
    public ResponseResult selectvGateData(@RequestParam String stcd) {
        return ResponseResult.success("查询成功", vGateDataService.selectvGateData(stcd));
    }
}
