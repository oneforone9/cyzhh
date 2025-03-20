package com.essence.web.portal;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.EventBaseService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件基础信息表管理
 *
 * @author zhy
 * @since 2022-10-30 18:06:24
 */
@Slf4j
@Controller
@RequestMapping("/event")
public class EventBaseController {

    @Autowired
    private EventBaseService eventBaseService;

    /**
     * 新增
     *
     * @param eventBaseEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(HttpServletRequest request,  @RequestBody EventBaseEsu eventBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        if (StrUtil.isNotEmpty(eventBaseEsu.getCreator())){
            eventBaseEsu.setCreator(eventBaseEsu.getCreator());
        }else {
            eventBaseEsu.setCreator(userId);
        }
        eventBaseEsu.setUpdater(userId);
        eventBaseEsu.setGmtCreate(new Date());
        eventBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("添加成功", eventBaseService.insertEventBase(eventBaseEsu));
    }
    /**
     * 案件问题图片上传
     *
     * @param eventBasePictures
     * @return
     */
    @PostMapping("/uploadPictures")
    @ResponseBody
    public ResponseResult uploadPictures(HttpServletRequest request,  @RequestBody EventBasePictures eventBasePictures) {
        return ResponseResult.success("上传成功", eventBaseService.uploadPictures(eventBasePictures));
    }
    /**
     * 更新
     *
     * @param eventBaseEsu
     * @return
     * @apiNote 更新哪个字段传哪个字段
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(HttpServletRequest request,  @RequestBody EventBaseEsu eventBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        eventBaseEsu.setUpdater(userId);
        eventBaseEsu.setGmtModified(new Date());
        return ResponseResult.success("更新成功", eventBaseService.update(eventBaseEsu));
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
    public ResponseResult delete(@PathVariable String id) {

        return ResponseResult.success("删除成功", eventBaseService.deleteById(id));
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<EventBaseEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", eventBaseService.findById(id));
    }

    /**
     * 根据条件分页查询   获取事件详情数据
     *
     * @param param
     * @return
     */
    @PostMapping("/searchEvent")
    @ResponseBody
    public ResponseResult<Paginator<EventBaseEsr>> searchEvent(HttpServletRequest request, @RequestBody PaginatorParam param) {
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        log.info("userName"+ userName);
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        //增加水环境组和闸坝设施组的权限   原因在事件表中 disposeName 有时候查到有时候查不到
//        if("二所水环境组".equals(userName) ||"一所水环境组".equals(userName) || "二所闸坝设施组".equals(userName) || "一所闸坝设施组".equals(userName)){
//            log.info("/event/search00000000000000000000");
//            Criterion criterion2 = new Criterion();
//            criterion2.setFieldName("disposeName");
//            criterion2.setOperator(Criterion.EQ);
//            criterion2.setValue(userName);
//            currency.add(criterion2);
//            log.info("/event/search00000000000000000001");
//        }
        //增加水环境组和闸坝设施组的权限
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", eventBaseService.findByPaginator(param));
    }


    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<EventBaseEsr>> search(HttpServletRequest request, @RequestBody PaginatorParam param) {
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        log.info("userName"+ userName);
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        //增加水环境组和闸坝设施组的权限   原因在事件表中 disposeName 有时候查到有时候查不到
        if("二所水环境组".equals(userName) ||"一所水环境组".equals(userName) || "二所闸坝设施组".equals(userName) || "一所闸坝设施组".equals(userName)){
            log.info("/event/search00000000000000000000");
            Criterion criterion2 = new Criterion();
            criterion2.setFieldName("disposeName");
            criterion2.setOperator(Criterion.EQ);
            criterion2.setValue(userName);
            currency.add(criterion2);
            log.info("/event/search00000000000000000001");
        }
        //增加水环境组和闸坝设施组的权限
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", eventBaseService.findByPaginator(param));
    }


    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/searchNoLimit")
    @ResponseBody
    public ResponseResult<Paginator<EventBaseEsr>> searchNoLimit(HttpServletRequest request, @RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", eventBaseService.findByPaginator(param));
    }

    /**
     * 导出案件数据
     *
     * @param param
     * @return R
     */
    @PostMapping("/export/infoList")
    public void exportInfoList(HttpServletResponse response, @RequestBody PaginatorParam param) {

        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<EventBaseEsr> byPaginator = eventBaseService.findByPaginator(param);
        List<EventBaseEsr> items = Optional.ofNullable(byPaginator.getItems()).orElse(Lists.newArrayList());
        Map<String, String> mapEventChannel = new HashMap<>();
        mapEventChannel.put("1", "市级交办");
        mapEventChannel.put("2", "区级交办");
        mapEventChannel.put("3", "自查问题");
        mapEventChannel.put("4", "12345");
        mapEventChannel.put("5", "网络上报");
        mapEventChannel.put("6", "摄像头抓拍");
        Map<String, String> mapEventClass = new HashMap<>();
        mapEventClass.put("11", "河道岸坡、水面保洁情况，有不符合相关标准和要求的管理行为和环境卫生问题");
        mapEventClass.put("12", "河道绿化养护情况，有不符合相关标准和要求的管理行为和环境卫生问题");
        mapEventClass.put("13", "河道水质异常，存在污水入河现象");
        mapEventClass.put("14", "有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱到、乱放行为");
        mapEventClass.put("15", "管理范围内倾倒垃圾和渣土、堆放非防汛物资");
        mapEventClass.put("21", "有防洪工程设施未验收，即将建设项目投入生产或者使用的行为");
        mapEventClass.put("22", "在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，经我局批准后开工建设，建成后经我局相关河道管理单位验收合格后投入使用");
        mapEventClass.put("23", "在河道管理范围内有法律法规禁止的不当行为; 在河湖管理范围、保护范围内未经我局批准擅自进行的有关活动");
        mapEventClass.put("31", "水闸设施建筑物完好");
        mapEventClass.put("32", "泵站设施正常运行");
        mapEventClass.put("33", "边闸设施出现污水入河");
        mapEventClass.put("34", "其他附属设施出现损坏，建筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损坏堤护岸林木");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<EventBaseExport> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(items)) {
                list = items.stream().map(x -> {
                    EventBaseExport eventBaseExport = new EventBaseExport();
                    eventBaseExport.setEventCode(x.getEventCode());
                    eventBaseExport.setReaName(x.getReaName());
                    eventBaseExport.setEventChannel(mapEventChannel.get(x.getEventChannel()));
                    eventBaseExport.setEventClass(mapEventClass.get(x.getEventClass()));
                    if (StringUtils.isNotBlank(x.getProblemDesc())) {
                        eventBaseExport.setProblemDesc(x.getProblemDesc().replace("[", "").replace("]", "").replaceAll("\"", ""));
                    }
                    eventBaseExport.setPosition(x.getPosition());
                    eventBaseExport.setEventTime(sdf.format(x.getEventTime()));
                    //获取当天得截止时间
                    if (x.getEndTime() != null) {
                        eventBaseExport.setEndTime(sdf.format(x.getEndTime()));
                    } else {
                        eventBaseExport.setEndTime(sdf.format(DateUtil.endOfDay(x.getEventTime())));
                    }
                    eventBaseExport.setStatus(x.getStatus().equals("1") ? "已办" : "未办");
                    eventBaseExport.setDisposeName(x.getDisposeName());
                    return eventBaseExport;
                }).collect(Collectors.toList());
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("案件数据(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(response.getOutputStream(), EventBaseExport.class).sheet("案件数据").registerWriteHandler(horizontalCellStyleStrategy).doWrite(list);
        } catch (Exception e) {
            System.out.println("案件数据" + e);
        }
    }

    /**
     * 事件综合分析
     *
     * @return
     */
    @GetMapping("/analysis")
    @ResponseBody
    public ResponseResult<EventAnalysisDto> getEventAnalysis(String unitId) {

        return ResponseResult.success("查询成功", eventBaseService.getEventAnalysis(unitId));
    }


    /**
     * 案件渠道
     *
     * @return
     */
    @GetMapping("/channel")
    @ResponseBody
    public ResponseResult<EventChannelDto> getEventChannel(String unitId) {

        return ResponseResult.success("查询成功", eventBaseService.getEventChannel(unitId));
    }


    /**
     * 根据条件分页查询作业记录
     *
     * @param param
     * @return
     */
    @PostMapping("/searchBase")
    @ResponseBody
    public ResponseResult<Paginator<EventBaseEsr>> searchBase(@RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.EVENT_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", eventBaseService.findByPaginatorBase(param));
    }

}
