package com.essence.web.xj;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.ViewHysXjService;
import com.essence.interfaces.api.ViewVideoXjService;
import com.essence.interfaces.api.XjZjhService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.ViewHysXjEsr;
import com.essence.interfaces.model.ViewVideoQuery;
import com.essence.interfaces.model.ViewVideoXjEsr;
import com.essence.interfaces.model.XjZjhData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 设备巡检管理（摄像头和会议室）0108
 * @author majunjie
 * @since 2025-01-08 14:13:44
 */
@RestController
@RequestMapping("/xj")
public class ViewVideoXjController  {
    @Autowired
    private ViewVideoXjService viewVideoXjService;
    @Autowired
    private XjZjhService xjZjhService;
    @Autowired
    private ViewHysXjService viewHysXjService;
    /**
     *周数据生成(维护专用)
     *
     * @param xjZjhData
     *
     */
    @PostMapping("/getWeekData")
    public ResponseResult getWeekData(@RequestBody XjZjhData xjZjhData) {
        return ResponseResult.success("查询成功", xjZjhService.getWeekData(xjZjhData));
    }
    /**
     * 摄像头巡检计划根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchVideo")
    public ResponseResult<Paginator<ViewVideoXjEsr>> searchVideo(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", viewVideoXjService.findByPaginator(param));
    }
    /**
     * 会议室巡检计划根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchHys")
    public ResponseResult<Paginator<ViewHysXjEsr>> searchHys(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", viewHysXjService.findByPaginator(param));
    }
    /**
     * 编辑会议室巡检计划
     *
     * @param viewHysXjEsr
     * @return
     */
    @PostMapping("/updateHysJh")
    public ResponseResult<ViewHysXjEsr> updateHysJh(@RequestBody ViewHysXjEsr viewHysXjEsr) {
        return ResponseResult.success("编辑成功", viewHysXjService.updateHysJh(viewHysXjEsr));
    }

    /**
     * 通过id查询摄像头巡检计划
     *
     * @param viewVideoQuery
     * @return
     */
    @PostMapping("/selectVideo")
    public ResponseResult<ViewVideoXjEsr> selectVideo(@RequestBody ViewVideoQuery viewVideoQuery) {
        return ResponseResult.success("查询成功", viewVideoXjService.selectVideo(viewVideoQuery));
    }
    /**
     * 编辑摄像头巡检计划
     *
     * @param ViewVideoXjEsr
     * @return
     */
    @PostMapping("/updateVideoJh")
    public ResponseResult<ViewVideoXjEsr> updateVideoJh(@RequestBody ViewVideoXjEsr ViewVideoXjEsr) {
        return ResponseResult.success("编辑成功", viewVideoXjService.updateVideoJh(ViewVideoXjEsr));
    }

}
