package com.essence.web.portal;

import cn.hutool.core.util.StrUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.EventCompanyDto;
import com.essence.dao.entity.WorkorderNewest;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.EventCompanyService;
import com.essence.interfaces.api.WorkorderBaseService;
import com.essence.interfaces.api.WorkorderNewestService;
import com.essence.interfaces.api.WorkorderProcessService;
import com.essence.interfaces.dot.PortalEventCountDto;
import com.essence.interfaces.dot.PortalUserCountDto;
import com.essence.interfaces.dot.WorkMarkRequestDto;
import com.essence.interfaces.dot.WorkMarkResDto;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.WorkorderAttention;
import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.model.WorkorderNewestEsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工单基础信息表管理
 *
 * @author zhy
 * @since 2022-10-27 15:26:30
 */
@Controller
@RequestMapping("/order")
public class WorkorderBaseController {
    @Autowired
    private WorkorderBaseService workorderBaseService;

    @Autowired
    private WorkorderNewestService workorderNewestService;

    @Autowired
    private WorkorderProcessService workorderProcessService;
    @Autowired
    private EventCompanyService  eventCompanyService;

    /**
     * 新增
     *
     * @param workorderBaseEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @RequestBody WorkorderBaseEsu workorderBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        workorderBaseEsu.setCreator(userId);
        workorderBaseEsu.setUpdater(userId);
        workorderBaseEsu.setGmtCreate(new Date());
        workorderBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", workorderBaseService.insertWorkorder(workorderBaseEsu));
    }

    /**
     * 工单更新
     * @param request
     * @param workorderBaseEsu
     * @return
     */
    @PostMapping("/uodate")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request, @RequestBody WorkorderBaseEsu workorderBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }

        workorderBaseEsu.setGmtCreate(new Date());
        workorderBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", workorderBaseService.updateWorkorder(workorderBaseEsu));
    }
    /**
     * 更改关注
     * @param workorderAttention
     * @return
     */
    @PostMapping("/update/attention")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request, @Validated @RequestBody WorkorderAttention workorderAttention) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        WorkorderBaseEsu workorderBaseEsu = new WorkorderBaseEsu();
        if (StrUtil.isNotEmpty(workorderAttention.getIsAttention()) ){
            workorderBaseEsu.setIsAttention(workorderAttention.getIsAttention());
        }if (StrUtil.isNotEmpty(workorderAttention.getStartWorkTime())){
            workorderBaseEsu.setStartWorkTime(workorderAttention.getStartWorkTime());
        }if (StrUtil.isNotEmpty(workorderAttention.getEndWorkTime())){
            workorderBaseEsu.setEndWorkTime(workorderAttention.getEndWorkTime());
        }if (StrUtil.isNotEmpty(workorderAttention.getDistributeType())){
            workorderBaseEsu.setDistributeType(workorderAttention.getDistributeType());
        }if (StrUtil.isNotEmpty(workorderAttention.getOrderManager())){
            workorderBaseEsu.setOrderManager(workorderAttention.getOrderManager());
        }
        workorderBaseEsu.setRejectStartTime(workorderAttention.getRejectStartTime());
        workorderBaseEsu.setId(workorderAttention.getId());
        workorderBaseEsu.setUpdater(userId);
        workorderBaseEsu.setGmtModified(new Date());
        workorderBaseEsu.setStartTime(workorderAttention.getStartTime());
        int update = workorderBaseService.update(workorderBaseEsu);
        if ("1".equals(workorderAttention.getIsRejectTime())) {
            workorderBaseEsu.setRejectTime(workorderAttention.getRejectTime());
            workorderBaseService.updateRejectTime(workorderBaseEsu);
        }
        return ResponseResult.success("添加成功", update);

    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<WorkorderNewestEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", workorderNewestService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     * @apiNote 注意根据经办人主键集（operatorIds）查询的时候,操作使用LIKE,值传英文逗号+人员id+英文逗号；示例 {"fieldName": "operatorIds","value": ",11,","operator": "LIKE"}
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<WorkorderNewestEsr>> search(HttpServletRequest request,@RequestBody PaginatorParam param) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId = "0c73d3c1b49240aeb731112cddd5ad90";
        //去查询是否是三方用户
        List<EventCompanyDto> eventCompanyDtos = eventCompanyService.selectEventCompany(userId);
        if (eventCompanyDtos.size() > 0) {
            String companyId = eventCompanyDtos.get(0).getCompanyId();
            List currency = new ArrayList<>();
            Criterion criterion = new Criterion();
            criterion.setFieldName("companyId");
            criterion.setOperator(Criterion.EQ);
            criterion.setValue(companyId);
            currency.add(criterion);

            String[] realIdArray = new String[eventCompanyDtos.size()];
            for (int k = 0; k < eventCompanyDtos.size(); k++) {
                String riverId = eventCompanyDtos.get(k).getRiverId();
                realIdArray[k] =riverId;
            }
            Criterion criterion2 = new Criterion();
            criterion2.setFieldName("realId");
            criterion2.setOperator(Criterion.IN);
            criterion2.setValue(realIdArray);
            currency.add(criterion2);
            param.setCurrency(currency);


            //手动三方工单去除personId
            List<Criterion> conditions = param.getConditions();
            for (int q = 0; q < conditions.size(); q++) {
                String fieldName = conditions.get(q).getFieldName();
                //增加限制班长只能看到组员已终止和待审核的工单，其他的看不到
                if ("personId".equals(fieldName)){
                    conditions.remove(q);
                    break;
                }
            }
            //手动三方工单去除personId
            return ResponseResult.success("查询成功", workorderNewestService.findByPaginatorByCompanyId(param) );

        }else {
            return ResponseResult.success("查询成功", workorderNewestService.findByPaginator(param) );
        }
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     * @apiNote 注意根据经办人主键集（operatorIds）查询的时候,操作使用LIKE,值传英文逗号+人员id+英文逗号；示例 {"fieldName": "operatorIds","value": ",11,","operator": "LIKE"}
     */
    @PostMapping("/searchCount")
    @ResponseBody
    public ResponseResult<Map<String,Object>> searchCount(HttpServletRequest request, @RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", workorderNewestService.findByPaginatorCount(param) );
    }


    /**
     * 查询 巡查留痕迹
     *
     * @param workMarkRequestDto 查询参数
     * @return
     * @apiNote 查询 巡查留痕迹
     */
    @PostMapping("/mark")
    @ResponseBody
    public ResponseResult<List<WorkMarkResDto>> getWorkPortal(@RequestBody WorkMarkRequestDto workMarkRequestDto) {
        return ResponseResult.success("查询成功", workorderNewestService.getWorkPortal(workMarkRequestDto));
    }


    /**
     * 查询 巡河 按照河道统计
     *
     * @return
     * @apiNote 查询 巡查留痕迹
     */
    @GetMapping("/user/count")
    @ResponseBody
    public ResponseResult<PortalUserCountDto> getPortalCount(String unitId) {
        return ResponseResult.success("查询成功", workorderNewestService.getPortalCount(unitId));
    }

    /**
     * 查询 巡河人员统计
     *
     * @return
     * @apiNote 查询 按照人员 巡查留痕迹
     */
    @GetMapping("portal/user/count")
    @ResponseBody
    public ResponseResult<PortalUserCountDto> getUserPortalCount(String unitId) {
        return ResponseResult.success("查询成功", workorderNewestService.getPortalUserCount(unitId));
    }

    /**
     * 查询 河段下事件 热力图
     * @param type 1 事件  2 打卡
     * @return
     * @apiNote 查询 河段下事件 热力图
     */
    @GetMapping("/river/event")
    @ResponseBody
    public ResponseResult<List<PortalEventCountDto>> getRiverEventStatistic(String start, String end,String type) {
        return ResponseResult.success("查询成功", workorderNewestService.getRiverEventStatistic(start,end,type));
    }


    /**
     * 查询 未巡河河段
     *
     * @param workMarkRequestDto 查询参数
     * @return
     */
    @PostMapping("/unRiver")
    @ResponseBody
    public ResponseResult<List<WorkorderNewest>> unRiver(@RequestBody WorkMarkRequestDto workMarkRequestDto) {
        return ResponseResult.success("查询成功", workorderNewestService.unRiver(workMarkRequestDto));
    }


}
