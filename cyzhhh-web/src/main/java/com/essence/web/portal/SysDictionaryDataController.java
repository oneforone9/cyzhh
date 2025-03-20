package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.SysDictionaryDataService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.SysDictionaryDataEsr;
import com.essence.interfaces.model.SysDictionaryDataEsrEX;
import com.essence.interfaces.model.SysDictionaryDataEsu;
import com.essence.interfaces.param.SysDictionaryDataEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 字典数据表管理
 *
 * @author zhy
 * @since 2022-11-03 17:12:27
 */
@Controller
@RequestMapping("/sys/data")
public class SysDictionaryDataController {
    @Autowired
    private SysDictionaryDataService sysDictionaryDataService;


    /**
     * 新增
     *
     * @param sysDictionaryDataEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody SysDictionaryDataEsu sysDictionaryDataEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        // 默认值
        sysDictionaryDataEsu.setId(UuidUtil.get32UUIDStr());
        sysDictionaryDataEsu.setIsDeleted(ItemConstant.DICTDATA_NO_DELETE);

        sysDictionaryDataEsu.setCreator(userId);
        sysDictionaryDataEsu.setUpdater(userId);
        sysDictionaryDataEsu.setGmtCreate(new Date());
        sysDictionaryDataEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", sysDictionaryDataService.insert(sysDictionaryDataEsu));
    }

    /**
     * 更新
     * @apiNote 更新哪个字段传哪个字段
     * @param sysDictionaryDataEsu
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody SysDictionaryDataEsu sysDictionaryDataEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        sysDictionaryDataEsu.setUpdater(userId);
        sysDictionaryDataEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", sysDictionaryDataService.update(sysDictionaryDataEsu));
    }

    /**
     * 删除
     * @apiNote 提示只可以删除24小时内创建的人员
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(HttpServletRequest request, @PathVariable String id) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        SysDictionaryDataEsu sysDictionaryDataEsu = new SysDictionaryDataEsu();
        sysDictionaryDataEsu.setId(id);
        sysDictionaryDataEsu.setIsDeleted(ItemConstant.DICTDATA_IS_DELETE);
        sysDictionaryDataEsu.setUpdater(userId);
        sysDictionaryDataEsu.setGmtModified(new Date());
        return ResponseResult.success("删除成功", sysDictionaryDataService.update(sysDictionaryDataEsu));
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<SysDictionaryDataEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", sysDictionaryDataService.findById(id));
    }

    /**
     * 根据字典类型查询
     *
     * @param type
     * @return
     */
    @GetMapping("/search/type/{type}")
    @ResponseBody
    public ResponseResult<List<SysDictionaryDataEsrEX>> searchByType(@PathVariable String type) {
        return ResponseResult.success("查询成功", sysDictionaryDataService.findTreeByType(type));
    }
}
