package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.PersonBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.PersonBaseEsr;
import com.essence.interfaces.model.PersonBaseEsu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 人员基础信息表管理
 *
 * @author zhy
 * @since 2022-10-20 15:07:24
 */
@Controller
@RequestMapping("/person")
public class PersonBaseController {
    @Autowired
    private PersonBaseService personBaseService;


    /**
     * 新增
     *
     * @param personBaseEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody PersonBaseEsu personBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        personBaseEsu.setCreator(userId);
        personBaseEsu.setUpdater(userId);
        personBaseEsu.setGmtCreate(new Date());
        personBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", personBaseService.insert(personBaseEsu));
    }

    /**
     * 更新
     * @apiNote 更新哪个字段传哪个字段
     * @param personBaseEsu
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody PersonBaseEsu personBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        personBaseEsu.setUpdater(userId);
        personBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", personBaseService.update(personBaseEsu));
    }

    /**
     * 删除
     * @apiNote 提示只可以删除24小时内创建的人员
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable String id) {

        return ResponseResult.success("删除成功", personBaseService.deleteById(id));
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<PersonBaseEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", personBaseService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<PersonBaseEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", personBaseService.findByPaginator(param));
    }
}
