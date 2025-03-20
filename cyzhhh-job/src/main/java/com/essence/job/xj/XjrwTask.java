package com.essence.job.xj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 闸坝泵监测定时任务
 *
 * @author majunjie
 * @since 2023/04/19 16:07
 */
@Component
@Log4j2
public class XjrwTask {

    @Autowired
    private XjZjhDao xjZjhDao;
    @Autowired
    private XjSxtJhTimeDao xjSxtJhTimeDao;
    @Autowired
    private ViewVideoXjDao viewVideoXjDao;
    @Autowired
    private XjRyxxDao xjRyxxDao;
    @Autowired
    private XjrwDao xjrwDao;
    @Autowired
    private XjrwlcDao xjrwlcDao;
    @Autowired
    private ViewHysXjDao viewHysXjDao;
    @Autowired
    private XjSxtJhDao xjSxtJhDao;
    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(cron = "0 35 7 * * ?")
    public void saveXjrw() {
        try {
            log.info("巡检摄像头任务自动生成开始saveXjrw{}");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = new Date();

            String dayStr = sdf.format(time).substring(0, 10);
            String dayStrS = dayStr.replace("-", "");
//获取当前日期是周几
            LocalDate currentDate = LocalDate.parse(dayStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String z = String.valueOf(dayOfWeek.getValue());
            //巡检任务数组
            List<XjrwDto> xjrwList = new ArrayList<>();
            //巡检任务历程数组
            List<XjrwlcDto> lcList = new ArrayList<>();

            //1.获取人员信息
            List<XjRyxxDto> xjRyxxDtos = xjRyxxDao.selectList(new QueryWrapper<XjRyxxDto>());
            Map<String, XjRyxxDto> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(xjRyxxDtos)) {
                map = xjRyxxDtos.stream().collect(Collectors.toMap(XjRyxxDto::getId, Function.identity(), (o1, o2) -> o2));
            }

            //2.获取周计划数据
            List<XjZjhDto> xjZjhDtos = xjZjhDao.selectList(new QueryWrapper<XjZjhDto>().lambda().eq(XjZjhDto::getTime, dayStr));
            if (!CollectionUtils.isEmpty(xjZjhDtos)) {
                XjZjhDto xjZjhDto = xjZjhDtos.get(0);
                //3拿到周计划id 去获取当前摄像头数据
                List<XjSxtJhTimeDto> xjSxtJhTimeDtos = xjSxtJhTimeDao.selectList(new QueryWrapper<XjSxtJhTimeDto>().lambda().eq(XjSxtJhTimeDto::getZjhId, xjZjhDto.getId()));
                if (!CollectionUtils.isEmpty(xjSxtJhTimeDtos)) {
                    List<String> jhIdList = xjSxtJhTimeDtos.stream().map(x -> x.getJhId()).collect(Collectors.toList());
                    //4.拿到当前周有计划的计划数据
                    List<ViewVideoXjDto> viewVideoXjDtos = Optional.ofNullable(viewVideoXjDao.selectList(new QueryWrapper<ViewVideoXjDto>().lambda().in(ViewVideoXjDto::getJhid, jhIdList))).orElse(Lists.newArrayList());
                    Integer count = 1;
                    for (ViewVideoXjDto viewVideoXjDto : viewVideoXjDtos) {
                        //1.添加巡检任务
                        XjrwDto xjrwDto = new XjrwDto();
                        xjrwDto.setId(UUID.randomUUID().toString().replace("-", ""));
                        xjrwDto.setGdmc(dayStrS + viewVideoXjDto.getName() + "设备巡检工单");
                        xjrwDto.setBh(dayStrS + "SBXJ" + String.format("%04d", count));
                        xjrwDto.setLy("0");
                        xjrwDto.setMc(viewVideoXjDto.getName());
                        xjrwDto.setType("0");
                        xjrwDto.setLx("0");
                        xjrwDto.setRiverName(viewVideoXjDto.getRiverName());

                        xjrwDto.setAddress(viewVideoXjDto.getAddress());
                        xjrwDto.setLgtd(viewVideoXjDto.getLgtd());
                        xjrwDto.setLttd(viewVideoXjDto.getLttd());
                        xjrwDto.setXjnr(viewVideoXjDto.getXjnr());
                        xjrwDto.setFzr(viewVideoXjDto.getFzr());
                        xjrwDto.setFzrId(viewVideoXjDto.getFzrId());
                        XjRyxxDto xjRyxxDto = map.get(viewVideoXjDto.getFzrId());
                        xjrwDto.setBzmc(viewVideoXjDto.getBzmc());
                        if (null != xjRyxxDto) {
                            xjrwDto.setLxfs(xjRyxxDto.getLxfs());
                            xjrwDto.setBzmc(xjRyxxDto.getBzmc());
                        }

                        xjrwDto.setJhsj(xjZjhDto.getStartTime());
                        xjrwDto.setJssj(xjZjhDto.getEndTime());

                        xjrwDto.setJjsx(0);
                        xjrwDto.setZt(0);
                        xjrwDto.setWcqk(0);
                        xjrwDto.setFxwt(0);
                        xjrwDto.setCjsj(new Date());
                        xjrwDto.setBzId(viewVideoXjDto.getBzId());
                        xjrwDto.setPfry(ItemConstant.XJCZR);
                        xjrwList.add(xjrwDto);
                        //2.添加巡检任务历程
                        XjrwlcDto xjrwlcDto = new XjrwlcDto();
                        xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
                        xjrwlcDto.setRwId(xjrwDto.getId());
                        xjrwlcDto.setMs(ItemConstant.XJSCGD);
                        xjrwlcDto.setCzr(ItemConstant.XJCZR);
                        xjrwlcDto.setCjsj(time);
                        lcList.add(xjrwlcDto);
                        count = count + 1;
                    }
                }
            }
            //摄像头
            log.info("巡检摄像头任务自动生成结束");

            log.info("巡检会议室任务自动生成开始saveXjrw{}");
            //会议室
            List<ViewHysXjDto> viewHysXjDtos = Optional.ofNullable(viewHysXjDao.selectList(new QueryWrapper<ViewHysXjDto>().lambda().like(ViewHysXjDto::getXjRq, z))).orElse(Lists.newArrayList());
            Integer hysCount = 1;
            for (ViewHysXjDto viewHysXjDto : viewHysXjDtos) {
                //水务局机房需要生成2次
                if (viewHysXjDto.getId().equals("7")){
                    //1.添加巡检任务
                    XjrwDto xjrwDtoJf = new XjrwDto();
                    xjrwDtoJf.setId(UUID.randomUUID().toString().replace("-", ""));
                    xjrwDtoJf.setGdmc(dayStrS + viewHysXjDto.getMc() + "巡检工单");
                    xjrwDtoJf.setBh(dayStrS + "HYSXJ" + String.format("%04d", hysCount));
                    xjrwDtoJf.setLy("0");
                    xjrwDtoJf.setMc(viewHysXjDto.getMc());
                    xjrwDtoJf.setType("1");
                    xjrwDtoJf.setLx("0");
                    xjrwDtoJf.setXjnr(viewHysXjDto.getXjnr());
                    xjrwDtoJf.setFzr(viewHysXjDto.getFzr());
                    xjrwDtoJf.setFzrId(viewHysXjDto.getFzrId());
                    xjrwDtoJf.setLgtd(viewHysXjDto.getLgtd());
                    xjrwDtoJf.setLttd(viewHysXjDto.getLttd());
                    XjRyxxDto xjRyxxDto = map.get(viewHysXjDto.getFzrId());
                    xjrwDtoJf.setBzmc(viewHysXjDto.getBzmc());
                    if (null != xjRyxxDto) {
                        xjrwDtoJf.setLxfs(xjRyxxDto.getLxfs());
                        xjrwDtoJf.setBzmc(xjRyxxDto.getBzmc());
                    }
                    xjrwDtoJf.setJhsj(sdf.parse(dayStr + " 08:00:00"));
                    xjrwDtoJf.setJssj(DateUtils.addDays(xjrwDtoJf.getJhsj(), 1));
                    xjrwDtoJf.setZt(0);
                    xjrwDtoJf.setWcqk(0);
                    xjrwDtoJf.setFxwt(0);
                    xjrwDtoJf.setCjsj(new Date());
                    xjrwDtoJf.setBzId(viewHysXjDto.getBzId());
                    xjrwDtoJf.setPfry(ItemConstant.XJCZR);
                    xjrwDtoJf.setJjsx(0);
                    xjrwList.add(xjrwDtoJf);
                    //2.添加巡检任务历程
                    XjrwlcDto xjrwlcDtoJf = new XjrwlcDto();
                    xjrwlcDtoJf.setId(UUID.randomUUID().toString().replace("-", ""));
                    xjrwlcDtoJf.setRwId(xjrwDtoJf.getId());
                    xjrwlcDtoJf.setMs(ItemConstant.XJSCGD);
                    xjrwlcDtoJf.setCzr(ItemConstant.XJCZR);
                    xjrwlcDtoJf.setCjsj(time);
                    lcList.add(xjrwlcDtoJf);
                    hysCount = hysCount + 1;
                }
                //1.添加巡检任务
                XjrwDto xjrwDto = new XjrwDto();
                xjrwDto.setId(UUID.randomUUID().toString().replace("-", ""));
                xjrwDto.setGdmc(dayStrS + viewHysXjDto.getMc() + "巡检工单");
                xjrwDto.setBh(dayStrS + "HYSXJ" + String.format("%04d", hysCount));
                xjrwDto.setLy("0");
                xjrwDto.setMc(viewHysXjDto.getMc());
                xjrwDto.setType("1");
                xjrwDto.setLx("0");
                xjrwDto.setXjnr(viewHysXjDto.getXjnr());
                xjrwDto.setFzr(viewHysXjDto.getFzr());
                xjrwDto.setFzrId(viewHysXjDto.getFzrId());
                xjrwDto.setLgtd(viewHysXjDto.getLgtd());
                xjrwDto.setLttd(viewHysXjDto.getLttd());
                XjRyxxDto xjRyxxDto = map.get(viewHysXjDto.getFzrId());
                xjrwDto.setBzmc(viewHysXjDto.getBzmc());
                if (null != xjRyxxDto) {
                    xjrwDto.setLxfs(xjRyxxDto.getLxfs());
                    xjrwDto.setBzmc(xjRyxxDto.getBzmc());
                }
                xjrwDto.setJhsj(sdf.parse(dayStr + " 08:00:00"));
                xjrwDto.setJssj(DateUtils.addDays(xjrwDto.getJhsj(), 1));
                xjrwDto.setZt(0);
                xjrwDto.setWcqk(0);
                xjrwDto.setFxwt(0);
                xjrwDto.setCjsj(new Date());
                xjrwDto.setBzId(viewHysXjDto.getBzId());
                xjrwDto.setPfry(ItemConstant.XJCZR);
                xjrwDto.setJjsx(0);
                xjrwList.add(xjrwDto);
                //2.添加巡检任务历程
                XjrwlcDto xjrwlcDto = new XjrwlcDto();
                xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
                xjrwlcDto.setRwId(xjrwDto.getId());
                xjrwlcDto.setMs(ItemConstant.XJSCGD);
                xjrwlcDto.setCzr(ItemConstant.XJCZR);
                xjrwlcDto.setCjsj(time);
                lcList.add(xjrwlcDto);
                hysCount = hysCount + 1;
            }
            log.info("巡检会议室任务自动生成结束saveXjrw{}");
            if (!CollectionUtils.isEmpty(xjrwList)) {
                xjrwDao.saveData(xjrwList);
            }
            if (!CollectionUtils.isEmpty(lcList)) {
                xjrwlcDao.saveData(lcList);
            }
            log.info("巡检自动生成结束saveXjrw{}");
        } catch (Exception e) {
            log.error("巡检任务自动生成异常saveXjrw{}" + e);
        }
    }

    @Scheduled(cron = "0 35 5 * * ?")
    public void saveXjrq() {
        try {
            log.info("巡检摄像头日期修改saveXjrq{}");
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 获取当前月份的第一天
            LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
            // 当月开始的时间（00:00:00）
            LocalDateTime startOfMonth = LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
            // 获取当前月份的最后一天
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            // 创建当月结束的时间（23:59:59）
            LocalDateTime endOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);

            LocalDate localDate = startOfMonth.toLocalDate();
            LocalDateTime startOfDay = localDate.atStartOfDay();
            //开始时间
            Date startTime = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

            LocalDate localDateEnd = endOfMonth.toLocalDate();
            LocalDateTime endOfDay = localDateEnd.atStartOfDay();
            //开始时间
            Date endTime = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
            List<XjZjhDto> xjZjhDtos = xjZjhDao.selectList(new QueryWrapper<XjZjhDto>().lambda().ge(XjZjhDto::getStartTime, startTime).le(XjZjhDto::getStartTime, endTime));
            Map<String, List<XjSxtJhTimeDto>> jhMap = new HashMap<>();
            Map<String, XjZjhDto> xjZjhMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(xjZjhDtos)) {
                xjZjhMap = xjZjhDtos.stream().collect(Collectors.toMap(XjZjhDto::getId, Function.identity(), (o1, o2) -> o2));
                List<String> xjrqList = xjZjhDtos.stream().map(XjZjhDto::getId).collect(Collectors.toList());
                List<XjSxtJhTimeDto> xjSxtJhTimeDtos = xjSxtJhTimeDao.selectList(new QueryWrapper<XjSxtJhTimeDto>().lambda().in(XjSxtJhTimeDto::getZjhId, xjrqList));
                if (!CollectionUtils.isEmpty(xjSxtJhTimeDtos)) {
                    jhMap = xjSxtJhTimeDtos.stream().collect(Collectors.groupingBy(XjSxtJhTimeDto::getJhId));
                }
            }
            List<XjSxtJhDto> xjSxtJhDtos = Optional.ofNullable(xjSxtJhDao.selectList(new QueryWrapper<XjSxtJhDto>())).orElse(new ArrayList<>());
            if (!CollectionUtils.isEmpty(xjSxtJhDtos)) {
                for (XjSxtJhDto xjSxtJhDto : xjSxtJhDtos) {
                    String xjrq = "";
                    List<XjSxtJhTimeDto> xjSxtJhTimeDtos = Optional.ofNullable(jhMap.get(xjSxtJhDto.getId())).orElse(new ArrayList<>());

                    if (CollectionUtils.isEmpty(xjSxtJhTimeDtos)) {
                        xjSxtJhDto.setXjRq(null);
                    } else {
                        List<XjZjhDto> zjhList = new ArrayList<>();
                        for (XjSxtJhTimeDto xjSxtJhTimeDto : xjSxtJhTimeDtos) {
                            if (null != xjZjhMap.get(xjSxtJhTimeDto.getZjhId())) {
                                zjhList.add(xjZjhMap.get(xjSxtJhTimeDto.getZjhId()));
                            }
                        }
                        if (CollectionUtils.isEmpty(zjhList)) {
                            xjSxtJhDto.setXjRq(null);
                        } else {
                            zjhList = zjhList.stream().sorted(Comparator.comparing(XjZjhDto::getStartTime)).collect(Collectors.toList());
                            for (XjZjhDto xjZjhDto : zjhList) {
                                xjrq = xjrq + xjZjhDto.getMs() + ",";
                            }
                            xjSxtJhDto.setXjRq(xjrq.substring(0, xjrq.length() - 1));
                        }
                    }
                }
                xjSxtJhDao.delete(new QueryWrapper<XjSxtJhDto>());
                xjSxtJhDao.saveData(xjSxtJhDtos);
            }
            log.info("巡检摄像头日期结束saveXjrq{}");
        } catch (Exception e) {
            log.error("巡检摄像头日期异常saveXjrq{}" + e);
        }
    }
}
