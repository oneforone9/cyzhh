package com.essence.web.xj;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备巡检班组/人员信息管理0108
 *
 * @author majunjie
 * @since 2025-01-08 08:13:22
 */
@RestController
@RequestMapping("/xjBzxx")
public class XjBzxxController {
    @Autowired
    private XjBzxxService xjBzxxService;
    @Autowired
    private XjHysxxService xjHysxxService;
    @Autowired
    private XjRyxxService xjRyxxService;
@Autowired
private XjDwService xjDwService;
@Autowired
private StBRiverService stBRiverService;
    /**
     * 班组信息根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchBzxx")
    public ResponseResult<Paginator<XjBzxxEsr>> searchBzxx(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjBzxxService.findByPaginator(param));
    }

    /**
     * 班组信息新增/编辑
     *
     * @param xjBzxxEsu
     * @return
     */
    @PostMapping("addBz")
    public ResponseResult<XjBzxxEsr> addBz(@RequestBody XjBzxxEsu xjBzxxEsu) {
        return ResponseResult.success("新增/修改成功", xjBzxxService.addBz(xjBzxxEsu));
    }

    /**
     * 会议室信息根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchHyxx")
    public ResponseResult<Paginator<XjHysxxEsr>> searchHyxx(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjHysxxService.findByPaginator(param));
    }
    /**
     * 根据会议室主键查询会议室巡查内容
     *
     * @param xjHysxjxxQuery
     * @return
     */
    @PostMapping("/selectHyxcXx")
    public ResponseResult<List<XjHysxjxxEsr>> selectHyxcXx(@RequestBody XjHysxjxxQuery xjHysxjxxQuery) {
        return ResponseResult.success("查询成功", xjHysxxService.selectHyxcXx(xjHysxjxxQuery));
    }
    /**
     * 巡检单位根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchXjdw")
    public ResponseResult<Paginator<XjDwEsr>> searchXjdw(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjDwService.findByPaginator(param));
    }
    /**
     * 河流根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchHl")
    public ResponseResult<Paginator<StBRiverEsr>> searchHl(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stBRiverService.findByPaginator(param));
    }
    /**
     * 巡检人员信息根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchRyxx")
    public ResponseResult<Paginator<XjRyxxEsr>> searchRyxx(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjRyxxService.findByPaginator(param));
    }
    /**
     * 通过id获取用户班组（查询班组长下面的人）
     *
     * @param xjRyxQuery
     * @return
     */
    @PostMapping("/searchRyxxById")
    public ResponseResult<List<XjRyxxEsr>> searchRyxxById(@RequestBody XjRyxQuery xjRyxQuery) {
        return ResponseResult.success("查询成功", xjRyxxService.searchRyxxById(xjRyxQuery));
    }
    /**
     * 通过id获取用户班组（查询通班组下面的人）
     *
     * @param xjRyxQuery
     * @return
     */
    @PostMapping("/searchRyxxByIds")
    public ResponseResult<List<XjRyxxEsr>> searchRyxxByIds(@RequestBody XjRyxQuery xjRyxQuery) {
        return ResponseResult.success("查询成功", xjRyxxService.searchRyxxByIds(xjRyxQuery));
    }

    /**
     * 巡检人员新增/编辑
     *
     * @param xjRyxxEsu
     * @return
     */
    @PostMapping("addStPlanPerson")
    public ResponseResult<XjRyxxEsr> addStPlanPerson(@RequestBody XjRyxxEsu xjRyxxEsu) {

        if (StringUtils.isNotBlank(xjRyxxEsu.getId())) {
            //已有id进行修
            return ResponseResult.success("修改成功", xjRyxxService.updateRyxx(xjRyxxEsu));
        } else {
            return ResponseResult.success("添加成功", xjRyxxService.addRyxx(xjRyxxEsu));
        }
    }
}
