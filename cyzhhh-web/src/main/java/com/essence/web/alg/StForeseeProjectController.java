package com.essence.web.alg;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StForeseeProjectService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StForeseeProjectEsr;
import com.essence.interfaces.model.StForeseeProjectSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预设调度方案管理
 * @author majunjie
 * @since 2023-04-24 11:21:04
 */
@RestController
@RequestMapping("/stForeseeProject")
public class StForeseeProjectController  {
    @Autowired
    private StForeseeProjectService stForeseeProjectService;
    /**
     * 预设调度根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectStForeseeProjectList")
    public ResponseResult<Paginator<StForeseeProjectEsr>> selectStForeseeProjectList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stForeseeProjectService.findByPaginator(param));
    }
    /**
     * 预设调度闸坝原数据查询
     *
     * @param stForeseeProjectSelect
     * @return R
     */
    @PostMapping("/selectStForeseeProject")
    public ResponseResult<List<StForeseeProjectEsr>> selectStForeseeProject(@RequestBody StForeseeProjectSelect stForeseeProjectSelect) {
        return ResponseResult.success("查询成功", stForeseeProjectService.selectStForeseeProject(stForeseeProjectSelect));
    }
   /**
     * 预设调度闸坝原数据保存
     *
     * @param stForeseeProjectEsrList
     * @return R
     */
    @PostMapping("/saveStForeseeProject")
    public ResponseResult saveStForeseeProject(@RequestBody List<StForeseeProjectEsr> stForeseeProjectEsrList) {
        return ResponseResult.success("查询成功", stForeseeProjectService.saveStForeseeProject(stForeseeProjectEsrList));
    }

}
