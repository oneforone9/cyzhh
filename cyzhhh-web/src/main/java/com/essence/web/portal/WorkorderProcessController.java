package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.WorkorderProcess;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.WorkorderProcessExService;
import com.essence.interfaces.api.WorkorderProcessService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.WorkorderProcessEsr;
import com.essence.interfaces.model.WorkorderProcessEsu;
import com.essence.interfaces.model.WorkorderProcessExEsr;
import com.essence.interfaces.param.WorkorderProcessEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 工单处理过程表管理
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */
@Controller
@RequestMapping("/order/process")
public class WorkorderProcessController  {
    @Autowired
    private WorkorderProcessService workorderProcessService;
    @Autowired
    private WorkorderProcessExService workorderProcessExService;

//    public WorkorderProcessController(WorkorderProcessService workorderProcessService) {
//        super(workorderProcessService);
//    }
    /**
     * 新增
     *
     * @param e
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody WorkorderProcessEsu e) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        //fix 新增如果 新增的工单状态是 工单状态-进行中 就去查询工单状态表中当前用户是否有 工单状态-进行中 有的话则不允许新增 没有的话则可以新增
        if (ItemConstant.ORDER_STATUS_RUNNING.equals(e.getOrderStatus())){
            Boolean flag = workorderProcessService.findByPersonIdAndWorkOrderStatus(userId, ItemConstant.ORDER_STATUS_RUNNING,e.getOrderId());
            if (flag){
                //如果查询到 则标识该用户存在 进行中的工单 不允许新增
                return ResponseResult.error("已经存在进行中的工单-添加失败");
            }
        }
        //20230801优化工单流程
        //将工单的状态表拆分成所有流程表和最新的工单状态表
        int insert = workorderProcessService.insertDeal(e);
        //20230801优化工单流程
        return ResponseResult.success("添加成功", insert);

    }

    /**
     * 更新
     *
     * @param e
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(@Validated(Update.class) @RequestBody WorkorderProcessEsu e) {
        return ResponseResult.success("更新成功", workorderProcessService.update(e));
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable String id) {
        return ResponseResult.success("删除成功", workorderProcessService.deleteById((Serializable) id));
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<WorkorderProcessEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", workorderProcessService.findById((Serializable) id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<WorkorderProcessEsr>> search(@RequestBody PaginatorParam param) {
        //Paginator<WorkorderProcessEsr> byPaginator = workorderProcessService.findByPaginator(param);
        return ResponseResult.success("查询成功", workorderProcessService.findByPaginator(param));

    }

    /**
     * 根据主键查询-含工单名称
     *
     * @param id
     * @return
     */
    @GetMapping("/search/ex/{id}")
    @ResponseBody
    public ResponseResult<WorkorderProcessExEsr> searchEx(@PathVariable String id) {
        return ResponseResult.success("查询成功", workorderProcessExService.findById(id));
    }

    /**
     * 根据条件分页查询-含工单名称
     *
     * @param param
     * @return
     */
    @PostMapping("/search/ex")
    @ResponseBody
    public ResponseResult<Paginator<WorkorderProcessExEsr>> searchEx(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", workorderProcessExService.findByPaginator(param));
    }

}
