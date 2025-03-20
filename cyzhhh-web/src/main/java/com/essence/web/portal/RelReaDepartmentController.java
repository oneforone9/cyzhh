package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.RelReaDepartmentService;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RelReaDepartmentEsr;
import com.essence.interfaces.model.RelReaDepartmentEsu;
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
 * 河道部门关系表管理
 *
 * @author zhy
 * @since 2022-10-24 17:43:26
 */
@Controller
@RequestMapping("/rel/rea/depart")
public class RelReaDepartmentController {
    @Autowired
    private RelReaDepartmentService relReaDepartmentService;


    /**
     * 新增
     *
     * @param relReaDepartmentEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated @RequestBody RelReaDepartmentEsu relReaDepartmentEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        relReaDepartmentEsu.setCreator(userId);
        relReaDepartmentEsu.setGmtCreate(new Date());
        return ResponseResult.success("添加成功", relReaDepartmentService.insert(relReaDepartmentEsu));
    }

    /**
     * 删除
     *
     * @param relReaDepartmentEsu
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult delete(@Validated @RequestBody RelReaDepartmentEsu relReaDepartmentEsu) {

        return ResponseResult.success("更新成功", relReaDepartmentService.delete(relReaDepartmentEsu));
    }


    /**
     * 根据条件查询不分页
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<List<RelReaDepartmentEsr>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", relReaDepartmentService.findBycondition(param));
    }

}
