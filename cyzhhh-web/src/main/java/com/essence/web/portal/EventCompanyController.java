package com.essence.web.portal;

import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.EventCompanyDto;
import com.essence.euauth.common.SysConstant;
import com.essence.interfaces.api.EventCompanyService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.vaild.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 第三方服务公司人员配置信息
 * @author majunjie
 * @since 2023-06-05 11:24:40
 */
@RestController
@RequestMapping("/eventCompany")
public class EventCompanyController {
    @Autowired
    private EventCompanyService eventCompanyService;

    /**
     * 根据三方人员userId获取功能手机号等
     *
     * @param userId
     * @return
     */
    @GetMapping("/selectTCompanyBase/{userId}")
    public ResponseResult selectTCompanyBase(@PathVariable("userId")  String userId) {
        return ResponseResult.success("添加成功", eventCompanyService.selectTCompanyBase(userId));
    }

    /**
     * 新增第三方服务公司人员信息表
     *
     * @param eventCompanySave
     * @return
     */
    @PostMapping("/addTCompanyBase")
    public ResponseResult addTCompanyBase(HttpServletRequest request, @RequestBody @Validated(Insert.class) EventCompanySave eventCompanySave) {
            return ResponseResult.success("添加成功", eventCompanyService.addTCompanyBase(eventCompanySave));
    }
    /**
     * 根据条件分页查询
     * @return
     */
    @PostMapping("selectTCompanyBaseList")
    public ResponseResult<PageUtil<EventCompanyList>> selectTCompanyBaseList(@RequestBody EventCompanySelect eventCompanySelect){
        PageUtil<EventCompanyList> eventCompanyList = eventCompanyService.selectTCompanyBaseList(eventCompanySelect);
        return ResponseResult.success("ok",eventCompanyList);
    }
    /**
     * 编辑删除第三方服务公司人员信息表
     *
     * @param EventCompanyList
     * @return
     */
    @PostMapping("/deleteTCompanyBase")
    public ResponseResult deleteTCompanyBase(HttpServletRequest request, @RequestBody  EventCompanyList EventCompanyList) {
        return ResponseResult.success("删除成功", eventCompanyService.deleteTCompanyBase(EventCompanyList));
    }
    /**
     * 修改第三方服务公司人员信息表
     *
     * @param eventCompanySave
     * @return
     */
    @PostMapping("/updateTCompanyBase")
    public ResponseResult updateTCompanyBase(HttpServletRequest request, @RequestBody EventCompanySave eventCompanySave) {
        return ResponseResult.success("修改成功", eventCompanyService.updateTCompanyBase(eventCompanySave));
    }
    /**
     * 查询第三方公司
     *
     * @param eventCompanyQuery
     * @return
     */
    @PostMapping("/selectTCompanyByRiverId")
    public ResponseResult <Map<String, List<EventCompanyPerson>>> selectTCompanyByRiverId(HttpServletRequest request, @RequestBody  EventCompanyQuery eventCompanyQuery) {
        return ResponseResult.success("查询成功", eventCompanyService.selectTCompanyByRiverId(eventCompanyQuery));
    }


    /**
     * 获取第三方公司下拉列表
     * @return
     */
    @PostMapping("selectEventCompanyList")
    public ResponseResult<List<EventCompanyRes>> selectEventCompanyList(@RequestBody EventCompanyEsu eventCompanyEsu){
        List<EventCompanyRes> list = eventCompanyService.selectEventCompanyList(eventCompanyEsu);
        return ResponseResult.success("查询成功",list);
    }

    /**
     * 是否是负责人 返回 true-是， false-不是负责人
     * @return
     */
    @GetMapping("selectIsHead")
    public ResponseResult selectIsHead(HttpServletRequest request){
        Boolean flag = false;
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //去查询是否是三方用户
        List<EventCompanyDto> eventCompanyDtos = eventCompanyService.selectEventCompany(userId);
        if (eventCompanyDtos.size() > 0) {
            flag = true;
        }else{
            flag = false;
        }
        return ResponseResult.success("查询成功",flag);
    }



}
