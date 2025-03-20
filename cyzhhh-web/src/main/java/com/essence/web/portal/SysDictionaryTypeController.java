package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.SysDictionaryTypeService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.SysDictionaryTypeEsr;
import com.essence.interfaces.model.SysDictionaryTypeEsu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 字典表管理
 *
 * @author zhy
 * @since 2022-11-03 17:12:31
 */
@Controller
@RequestMapping("/sys/type")
public class SysDictionaryTypeController {
    @Autowired
    private SysDictionaryTypeService sysDictionaryTypeService;

    /**
     * 新增
     *
     * @param sysDictionaryTypeEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody SysDictionaryTypeEsu sysDictionaryTypeEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        // 默认值
        sysDictionaryTypeEsu.setId(UuidUtil.get32UUIDStr());
        sysDictionaryTypeEsu.setIsDeleted(ItemConstant.DICTTYPE_NO_DELETE);
        sysDictionaryTypeEsu.setCreator(userId);
        sysDictionaryTypeEsu.setUpdater(userId);
        sysDictionaryTypeEsu.setGmtCreate(new Date());
        sysDictionaryTypeEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", sysDictionaryTypeService.insert(sysDictionaryTypeEsu));
    }

    /**
     * 更新
     *
     * @param sysDictionaryTypeEsu
     * @return
     * @apiNote 更新哪个字段传哪个字段
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody SysDictionaryTypeEsu sysDictionaryTypeEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        sysDictionaryTypeEsu.setUpdater(userId);
        sysDictionaryTypeEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", sysDictionaryTypeService.update(sysDictionaryTypeEsu));
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
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        SysDictionaryTypeEsu sysDictionaryTypeEsu = new SysDictionaryTypeEsu();
        sysDictionaryTypeEsu.setId(id);
        sysDictionaryTypeEsu.setIsDeleted(ItemConstant.DICTTYPE_IS_DELETE);
        sysDictionaryTypeEsu.setUpdater(userId);
        sysDictionaryTypeEsu.setGmtModified(new Date());
        return ResponseResult.success("删除成功", sysDictionaryTypeService.update(sysDictionaryTypeEsu));
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<SysDictionaryTypeEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", sysDictionaryTypeService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<SysDictionaryTypeEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", sysDictionaryTypeService.findByPaginator(param));
    }
}
