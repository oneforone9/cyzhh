package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.RosteringInfoService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RosteringInfoEsr;
import com.essence.interfaces.model.RosteringInfoEsu;
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
 * 人员巡河排班信息表管理
 *
 * @author zhy
 * @since 2022-10-25 11:22:56
 */
@Controller
@RequestMapping("/rostering")
public class RosteringInfoController {
    @Autowired
    private RosteringInfoService rosteringInfoService;

    /**
     * 新增
     *
     * @param rosteringInfoEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody RosteringInfoEsu rosteringInfoEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }

        rosteringInfoEsu.setId(UuidUtil.get32UUIDStr());
        rosteringInfoEsu.setIsDelete(ItemConstant.ROSTERING_NO_DELETE);
        rosteringInfoEsu.setCreator(userId);
        rosteringInfoEsu.setUpdater(userId);
        rosteringInfoEsu.setGmtCreate(new Date());
        rosteringInfoEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", rosteringInfoService.insert(rosteringInfoEsu));
    }

    /**
     * 更新
     *
     * @param rosteringInfoEsu
     * @return
     * @apiNote 更新哪个字段传哪个字段
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody RosteringInfoEsu rosteringInfoEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        rosteringInfoEsu.setUpdater(userId);
        rosteringInfoEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", rosteringInfoService.update(rosteringInfoEsu));
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
    public ResponseResult delete(HttpServletRequest request, @PathVariable String id) {
        RosteringInfoEsu rosteringInfoEsu = new RosteringInfoEsu();
        rosteringInfoEsu.setId(id);
        rosteringInfoEsu.setIsDelete(ItemConstant.ROSTERING_IS_DELETE);
        ResponseResult update = this.update(request, rosteringInfoEsu);
        if (ResponseResult.OK.equals(update.getCode())) {
            return ResponseResult.success("删除成功", update.getResult());
        }
        return ResponseResult.error("删除失败");
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    /*@GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<RosteringInfoEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", rosteringInfoService.findById(id));
    }*/

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<RosteringInfoEsr>> search(@RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.ROSTERING_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", rosteringInfoService.findByPaginator(param));
    }

}
