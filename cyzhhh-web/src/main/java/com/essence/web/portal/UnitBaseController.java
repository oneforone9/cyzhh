package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.UnitBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.DepartmentBaseEsu;
import com.essence.interfaces.model.UnitBaseEsr;
import com.essence.interfaces.model.UnitBaseEsrEx;
import com.essence.interfaces.model.UnitBaseEsu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 单位信息表管理
 *
 * @author zhy
 * @since 2022-10-20 14:16:38
 */
@Controller
@RequestMapping("/unit")
public class UnitBaseController {
    @Autowired
    private UnitBaseService unitBaseService;


    /**
     * 新增
     *
     * @param unitBaseEsu
     * @return
     */
    /*@PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated(Insert.class) @RequestBody UnitBaseEsu unitBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        unitBaseEsu.setCreator(userId);
        unitBaseEsu.setUpdater(userId);
        unitBaseEsu.setGmtCreate(new Date());
        unitBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", unitBaseService.insert(unitBaseEsu));
    }*/

    /**
     * 更新
     *
     * @param unitBaseEsu
     * @return
     */
    /*@PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @Validated(Update.class) @RequestBody UnitBaseEsu unitBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        unitBaseEsu.setUpdater(userId);
        unitBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", unitBaseService.update(unitBaseEsu));
    }*/

    /**
     * 删除（暂不提供删除接口）
     *
     * @param id
     * @return
     */
    /*@GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable String id) {

        return ResponseResult.success("删除成功", unitBaseService.deleteById(id));
    }*/

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<UnitBaseEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", unitBaseService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<UnitBaseEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", unitBaseService.findByPaginator(param));
    }

    /**
     * 根据条件分页查询(含班组)
     *
     * @param param
     * @return
     */
    @PostMapping("/search/depart")
    @ResponseBody
    public ResponseResult<Paginator<UnitBaseEsrEx>> searchDepart(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", unitBaseService.searchDepart(param));
    }

    /**
     * 设置巡河组长
     *
     * @param departmentBaseEsu
     * @return
     */
    @PostMapping("/search/setDepart")
    @ResponseBody
    public ResponseResult setDepart(@RequestBody DepartmentBaseEsu departmentBaseEsu) {
        return ResponseResult.success("设置巡河组长成功", unitBaseService.setDepart(departmentBaseEsu));
    }

    /**
     * 查询巡河组长
     *
     * @param personBaseId
     * @return
     */
    @GetMapping("/search/selectDepart")
    @ResponseBody
    public ResponseResult selectDepart(@RequestParam String personBaseId) {
        return ResponseResult.success("查询巡河组长成功", unitBaseService.selectDepart(personBaseId));
    }

}
