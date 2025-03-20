package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.EventBaseService;
import com.essence.interfaces.api.EventDepositService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.EventBaseEsr;
import com.essence.interfaces.model.EventBaseEsu;
import com.essence.interfaces.model.EventDepositEsr;
import com.essence.interfaces.model.EventDepositEsu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事件基础信息表管理(小程序上传临时存放)
 *
 * @author zhy
 * @since 2022-10-30 18:06:24
 */
@Controller
@RequestMapping("/event/deposit")
public class EventDepositController {

    @Autowired
    private EventDepositService eventDepositService;

    /**
     * 新增
     *
     * @param eventDepositEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody EventDepositEsu eventDepositEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        eventDepositEsu.setCreator(userId);
        eventDepositEsu.setUpdater(userId);
        eventDepositEsu.setGmtCreate(new Date());
        eventDepositEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", eventDepositService.insert(eventDepositEsu));
    }

    /**
     * 更新
     *
     * @param eventDepositEsu
     * @return
     * @apiNote 更新哪个字段传哪个字段
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody EventDepositEsu eventDepositEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        eventDepositEsu.setUpdater(userId);
        eventDepositEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", eventDepositService.update(eventDepositEsu));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @apiNote 提示只可以删除24小时内创建的人员
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable String id) {

        return ResponseResult.success("删除成功", eventDepositService.deleteById(id));
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<EventDepositEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", eventDepositService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<EventDepositEsr>> search(@RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", eventDepositService.findByPaginator(param));
    }
}
