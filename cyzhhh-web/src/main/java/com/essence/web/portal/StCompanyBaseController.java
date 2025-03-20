package com.essence.web.portal;


import com.alibaba.excel.EasyExcel;
import com.essence.common.utils.PageUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StCompanyBaseService;
import com.essence.interfaces.dot.StCompanyBaseDtos;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.EventCompanyEsr;
import com.essence.interfaces.model.StCompanyBaseEsr;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.interfaces.param.StCompanyBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 第三方服务公司基础表管理
 *
 * @author BINX
 * @since 2023-02-16 11:57:35
 */
@RestController
@RequestMapping("/stCompanyBase")
public class StCompanyBaseController extends BaseController<Long, StCompanyBaseEsu, StCompanyBaseEsp, StCompanyBaseEsr> {
    @Autowired
    private StCompanyBaseService stCompanyBaseService;

    public StCompanyBaseController(StCompanyBaseService stCompanyBaseService) {
        super(stCompanyBaseService);
    }


    /**
     * 新增第三方服务公司基础表管理
     *
     * @param StCompanyBaseEsu
     * @return
     */
    @PostMapping("/addStCompanyBase")
    public ResponseResult addStCompanyBase(HttpServletRequest request, @RequestBody StCompanyBaseEsu StCompanyBaseEsu) {

        if (null != StCompanyBaseEsu.getId()) {
            //已有id进行修
            return ResponseResult.success("修改成功", stCompanyBaseService.updateStCompanyBase(StCompanyBaseEsu));
        } else {
            return ResponseResult.success("添加成功", stCompanyBaseService.addStCompanyBase(StCompanyBaseEsu));
        }
    }


    /**
     * 根据idx查询第三方服务公司基础表管理
     *
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public ResponseResult selectById(HttpServletRequest request, @RequestParam String id) {
        if (null != id) {
            return ResponseResult.success("查询成功", stCompanyBaseService.selectById(id));
        } else {
            return ResponseResult.error("id不能为空", null);
        }
    }


    /**
     * 根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchAll")
    @ResponseBody
    public ResponseResult<Paginator<StCompanyBaseEsr>> searchAll(@RequestBody PaginatorParam param, String orderType) {
        Paginator<StCompanyBaseEsr> p = stCompanyBaseService.searchAll(param, orderType);
        return ResponseResult.success("查询成功", p);
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchAllR")
    @ResponseBody
    public ResponseResult<Paginator<EventCompanyEsr>> searchAllR(HttpServletRequest request,@RequestBody PaginatorParam param, String orderType) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        Paginator<EventCompanyEsr> p = stCompanyBaseService.searchAllR(param, orderType, userId);
        return ResponseResult.success("查询成功", p);
    }


    /**
     * 河湖管理-河道绿化保洁（绿化、保洁工单统计）
     *
     * @param unitId
     * @return
     */
    @GetMapping("/searchCount")
    @ResponseBody
    public ResponseResult searchCount(@RequestParam String unitId) {
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchCount(unitId);
        return ResponseResult.success("查询成功", list);
    }

    /**
     * 管河成效-第三方服务公司处理工单统计（修改）
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBase")
    @ResponseBody
    public ResponseResult searchStCompanyBase(@RequestBody StCompanyBaseDtos stCompanyBaseDtos) {
       // List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBase(stCompanyBaseDtos);
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseNew(stCompanyBaseDtos);
        return ResponseResult.success("查询成功", list);
    }

    /**
     * 管河成效-第三方服务公司处理工单统计
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBase2")
    @ResponseBody
    public ResponseResult<PageUtils<StCompanyBaseRelationDto>> searchStCompanyBase2(@RequestBody StCompanyBaseDtos stCompanyBaseDtos) {
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseNew(stCompanyBaseDtos);
        PageUtils<StCompanyBaseRelationDto> pageUtil = new PageUtils(list, stCompanyBaseDtos.getCurrent(), stCompanyBaseDtos.getSize());
        return ResponseResult.success("查询成功", pageUtil);
    }

    /**
     * 管河成效-第三方服务公司处理工单统计
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBase2/export")
    @ResponseBody
    public ResponseResult searchStCompanyBase2Export(@RequestBody StCompanyBaseDtos stCompanyBaseDtos, HttpServletResponse response) throws IOException {
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseNew(stCompanyBaseDtos);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName ="第三方服务公司管河段成效统计";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
        EasyExcel.write(response.getOutputStream(), StCompanyBaseRelationDto.class).sheet("第三方服务公司管河段成效统计").doWrite(list);
        return ResponseResult.success("查询成功",null);

    }

    /**
     * 管河成效-第三方服务公司案件处理效能统计(改)
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBaseDeal")
    @ResponseBody
    public ResponseResult searchStCompanyBaseDeal(@RequestBody StCompanyBaseDtos stCompanyBaseDtos) {
       // List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseDeal(stCompanyBaseDtos);
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseDealNew(stCompanyBaseDtos);
        return ResponseResult.success("查询成功", list);
    }
    /**
     * 第三方服务公司管河成效统计-绿化保洁工单
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBaseCount")
    @ResponseBody
    public ResponseResult searchStCompanyBaseCount(@RequestBody StCompanyBaseDtos stCompanyBaseDtos) {
        List<StCompanyBaseRelationDto> list = stCompanyBaseService.searchStCompanyBaseCount(stCompanyBaseDtos);
        return ResponseResult.success("查询成功", list);
    }


    /**
     * 第三方服务公司处理工单统计 每年的12个月
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @PostMapping("/searchStCompanyBaseMouth")
    @ResponseBody
    public ResponseResult searchStCompanyBaseMouth(@RequestBody StCompanyBaseDtos stCompanyBaseDtos) {
        List list = stCompanyBaseService.searchStCompanyBaseMouth(stCompanyBaseDtos);
        return ResponseResult.success("查询成功", list);
    }

}
