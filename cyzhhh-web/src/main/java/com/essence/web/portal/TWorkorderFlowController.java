package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.TWorkorderFlowService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.TWorkorderFlowEsr;
import com.essence.interfaces.model.TWorkorderFlowEsu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 工单过程表管理
 * @author majunjie
 * @since 2023-06-07 10:31:06
 */
@RestController
@RequestMapping("/tWorkorderFlow")
public class TWorkorderFlowController  {
    @Autowired
    private TWorkorderFlowService tWorkorderFlowService;
    /**
     * 新增
     *
     * @param tWorkorderFlowEsu
     * @return
     */
    @PostMapping("/add")
    public ResponseResult addTWorkOrderFlow(HttpServletRequest request, @RequestBody TWorkorderFlowEsu tWorkorderFlowEsu) {
        return ResponseResult.success("添加成功", tWorkorderFlowService.addTWorkOrderFlow(tWorkorderFlowEsu));
    }
    /**
     * 新增(生成并派发)
     *
     * @param tWorkorderFlowEsu
     * @return
     */
    @PostMapping("/addP")
    public ResponseResult addTWorkOrderFlows(HttpServletRequest request, @RequestBody TWorkorderFlowEsu tWorkorderFlowEsu) {
        tWorkorderFlowService.addTWorkOrderFlowP(tWorkorderFlowEsu);
        return ResponseResult.success("添加成功","" );
    }
    /**
     * 根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectWaterAreaInfoList")
    public ResponseResult<Paginator<TWorkorderFlowEsr>> selectWaterAreaInfoList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", tWorkorderFlowService.findByPaginator(param));
    }
}
