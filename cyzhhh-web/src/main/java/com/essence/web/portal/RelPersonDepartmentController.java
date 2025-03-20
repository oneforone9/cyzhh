package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.RelPersonDepartmentService;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RelPersonDepartmentEsr;
import com.essence.interfaces.model.RelPersonDepartmentEsu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 人员部门关系表管理
 *
 * @author zhy
 * @since 2022-10-24 17:42:58
 */
@Controller
@RequestMapping("/rel/person/depart")
public class RelPersonDepartmentController {
    @Autowired
    private RelPersonDepartmentService relPersonDepartmentService;

    /**
     * 新增
     *
     * @param relPersonDepartmentEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated @RequestBody RelPersonDepartmentEsu relPersonDepartmentEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        relPersonDepartmentEsu.setCreator(userId);
        relPersonDepartmentEsu.setGmtCreate(new Date());
        return ResponseResult.success("添加成功", relPersonDepartmentService.insert(relPersonDepartmentEsu));
    }

    /**
     * 删除
     *
     * @param relPersonDepartmentEsu
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult delete(@Validated @RequestBody RelPersonDepartmentEsu relPersonDepartmentEsu) {

        return ResponseResult.success("更新成功", relPersonDepartmentService.delete(relPersonDepartmentEsu));
    }


    /**
     * 根据条件查询不分页
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<List<RelPersonDepartmentEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", relPersonDepartmentService.findBycondition(param));
    }
}
