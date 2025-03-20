package com.essence.web.alg;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StWaterDispatchService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.vaild.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 预案预演管理
 *
 * @author majunjie
 * @since 2023-05-08 14:26:14
 */
@RestController
@RequestMapping("/stWaterDispatch")
public class StWaterDispatchController {
    @Autowired
    private StWaterDispatchService stWaterDispatchService;

    /**
     * 预案预演新增
     *
     * @param stWaterDispatchEsu
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<StWaterDispatchEsr> addWaterPlan(HttpServletRequest request, @RequestBody @Validated(Insert.class) StWaterDispatchEsu stWaterDispatchEsu) {
        return ResponseResult.success("添加成功", stWaterDispatchService.addWaterPlan(stWaterDispatchEsu));
    }

    /**
     * 预案预演根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/stWaterDispatchList")
    public ResponseResult<Paginator<StWaterDispatchEsr>> stWaterDispatchList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stWaterDispatchService.findByPaginator(param));
    }
   /**
     * 预案预演根据河流查询泵站
     *
     * @param riverId
     * @return R
     */
    @GetMapping("/stWaterDispatchDp/{riverId}")
    public ResponseResult<List<StWaterDispatchDP>> stWaterDispatchDp(@PathVariable("riverId") String riverId) {
        return ResponseResult.success("查询成功", stWaterDispatchService.stWaterDispatchDp(riverId));
    }
    /**
     * 预案预演根据泵站编码查询流量
     *
     * @param stcd
     * @return R
     */
    @GetMapping("/stWaterDispatchFlow/{stcd}")
    public ResponseResult<List<StWaterDispatchFlow>> stWaterDispatchFlowList(@PathVariable("stcd") String stcd) {
        return ResponseResult.success("查询成功", stWaterDispatchService.stWaterDispatchFlowList(stcd));
    }
    /**
     * 预案预演根据河流查询相关闸坝
     *
     * @param riverId
     * @return R
     */
    @GetMapping("/stWaterLevelByZB/{riverId}")
    public ResponseResult<List<StWaterDispatchLevelList>> stWaterLevelByZB(@PathVariable("riverId") String riverId) {
        return ResponseResult.success("查询成功", stWaterDispatchService.stWaterLevelByZB(riverId));
    }
    /**
     * 预案预演根据闸坝查询相关水位数据
     *
     * @param stcdId
     * @return R
     */
    @GetMapping("/stWaterLevelByStcd/{stcdId}")
    public ResponseResult<List<StWaterDispatchLevel>> stWaterLevelByStcd(@PathVariable("stcdId") Integer stcdId) {
        return ResponseResult.success("查询成功", stWaterDispatchService.stWaterLevelByStcd(stcdId));
    }
    /**
     * 预案预演根据闸坝查询相关水位数据(闸前闸后)
     *
     * @param stWaterDispatchZBLevelQuery
     * @return R
     */
    @PostMapping("/stWaterLevelZBByStcd")
    public ResponseResult<List<StWaterDispatchZBLevel>> stWaterLevelZBByStcd(@RequestBody StWaterDispatchZBLevelQuery stWaterDispatchZBLevelQuery) {
        return ResponseResult.success("查询成功", stWaterDispatchService.stWaterLevelZBByStcd(stWaterDispatchZBLevelQuery));
    }
}
