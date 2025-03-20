package com.essence.job.satellite;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 定时清除无雨的雷达图/卫星云图
 *
 * @Author BINX
 * @Description 定时清除无雨的雷达图/卫星云图, 查询当天的雨量站是否存在数据, 若无数据则删除当天的目录
 * @Date 2023/5/30 15:16
 */
@Component
@Slf4j
public class CleanOverdueRadarHandler {

    @Value("${nmc.save.path}")
    private String savePath;

    @Value("${file.local.path}")
    private String filePath;

    @Autowired
    RadarImagesLdDao radarImagesLdDao;
    @Autowired
    RadarImagesLddzDao radarImagesLddzDao;
    @Autowired
    RadarImagesWxDao radarImagesWxDao;
    @Autowired
    StRainDateDao stRainDateDao;
    @Autowired
    private FileBaseDao fileBaseDao;

    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 清除过期雷达图/卫星云图
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void demoJobHandler() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行清除过期雷达图/卫星云图任务,结束.", BACK_JOB);
            return;
        }
        Date cleanDate = declareCleanDate();
        log.info("...正在清理过期雷达图/卫星云图...");
        cleanRadarLd(cleanDate);
        cleanRadarLddz(cleanDate);
        cleanRadarWx(cleanDate);
        log.info("过期雷达图/卫星云图清理完成.");
    }

    private Date declareCleanDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date cleanDate = calendar.getTime();
        return cleanDate;
    }

    private void cleanRadarLd(Date cleanDate) {

        // 1. 获取表中所有上个月及之前的雷达图, 并定位所在位置
        QueryWrapper<RadarImagesLdDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("image_date", cleanDate);
        List<RadarImagesLdDto> radarImagesLdDtos = radarImagesLdDao.selectList(queryWrapper);
        // 2. 获取对应存放的文件夹日期
        Map<LocalDate, List<RadarImagesLdDto>> collect = radarImagesLdDtos.stream()
                .collect(Collectors.groupingBy(a -> LocalDate.parse(
                        fileDateFormat.format(a.getImageDate()), formatter)));
        for (LocalDate key : collect.keySet()) {
            String path = savePath + formatter.format(key);
            // 3. 根据文件夹日期查询当天的雨量站是否存在数据, 若无数据则删除当天的目录
            QueryWrapper<StRainDateDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.le("date", formatter.format(key) + " 23:59:59");
            queryWrapper1.ge("date", formatter.format(key) + " 00:00:00");
            List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(queryWrapper1);
            boolean rainDay = false;
            if (0 < stRainDateDtos.size()) {
                for (StRainDateDto s : stRainDateDtos) {
                    if (StrUtil.isNotEmpty(s.getHhRain()) && 0 > BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(s.getHhRain())))) {
                        rainDay = true;
                        break;
                    }
                }
            }
            if (!rainDay) {
                List<RadarImagesLdDto> radarImagesRainDay = collect.get(key);
                for (RadarImagesLdDto radarImagesLdDto : radarImagesRainDay) {
                    String image = radarImagesLdDto.getImageUrl().substring(radarImagesLdDto.getImageUrl().lastIndexOf("cyzhhhfile/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath1 = path + radarImagesLdDto.getImageUrl().substring(radarImagesLdDto.getImageUrl().lastIndexOf("/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath2 = filePath + File.separator + image;
                    radarImagesLdDao.deleteById(radarImagesLdDto.getId());
                    File toDelete1 = new File(imagePath1);
                    toDelete1.delete();
                    deleteFileBase(radarImagesLdDto.getImageUrl());
                    File toDelete2 = new File(imagePath2);
                    toDelete2.delete();
                }
            }
        }
    }

    private void cleanRadarLddz(Date cleanDate) {
        // 1. 获取表中所有上个月及之前的雷达图, 并定位所在位置
        QueryWrapper<RadarImagesLddzDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("image_date", cleanDate);
        List<RadarImagesLddzDto> radarImagesLddzDtos = radarImagesLddzDao.selectList(queryWrapper);
        // 2. 获取对应存放的文件夹日期
        // 使用Stream API按照日期进行分组
        Map<LocalDate, List<RadarImagesLddzDto>> collect = radarImagesLddzDtos.stream()
                .collect(Collectors.groupingBy(a -> LocalDate.parse(
                        fileDateFormat.format(a.getImageDate()), formatter)));
        for (LocalDate key : collect.keySet()) {
            String path = savePath + formatter.format(key);
            // 3. 根据文件夹日期查询当天的雨量站是否存在数据, 若无数据则删除当天的目录
            QueryWrapper<StRainDateDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.le("date", formatter.format(key) + " 23:59:59");
            queryWrapper1.ge("date", formatter.format(key) + " 00:00:00");
            List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(queryWrapper1);
            boolean rainDay = false;
            if (0 < stRainDateDtos.size()) {
                for (StRainDateDto s : stRainDateDtos) {
                    if (StrUtil.isNotEmpty(s.getHhRain()) && 0 > BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(s.getHhRain())))) {
                        rainDay = true;
                        break;
                    }
                }
            }
            if (!rainDay) {
                List<RadarImagesLddzDto> radarImagesRainDay = collect.get(key);
                for (RadarImagesLddzDto radarImagesLddzDto : radarImagesRainDay) {
                    String image = radarImagesLddzDto.getImageUrl().substring(radarImagesLddzDto.getImageUrl().lastIndexOf("cyzhhhfile/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath1 = path + radarImagesLddzDto.getImageUrl().substring(radarImagesLddzDto.getImageUrl().lastIndexOf("/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath2 = filePath + File.separator + image;
                    radarImagesLddzDao.deleteById(radarImagesLddzDto.getId());
                    File toDelete1 = new File(imagePath1);
                    toDelete1.delete();
                    deleteFileBase(radarImagesLddzDto.getImageUrl());
                    File toDelete2 = new File(imagePath2);
                    toDelete2.delete();
                }
            }
        }
    }

    private void cleanRadarWx(Date cleanDate) {
        // 1. 获取表中所有上个月及之前的雷达图/卫星云图, 并定位所在位置
        QueryWrapper<RadarImagesWxDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("image_date", cleanDate);
        List<RadarImagesWxDto> radarImagesWxDtos = radarImagesWxDao.selectList(queryWrapper);
        // 2. 获取对应存放的文件夹日期
        // 使用Stream API按照日期进行分组
        Map<LocalDate, List<RadarImagesWxDto>> collect = radarImagesWxDtos.stream()
                .collect(Collectors.groupingBy(a -> LocalDate.parse(
                        fileDateFormat.format(a.getImageDate()), formatter)));
        for (LocalDate key : collect.keySet()) {
            String path = savePath + formatter.format(key);
            // 3. 根据文件夹日期查询当天的雨量站是否存在数据, 若无数据则删除当天的目录
            QueryWrapper<StRainDateDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.le("date", formatter.format(key) + " 23:59:59");
            queryWrapper1.ge("date", formatter.format(key) + " 00:00:00");
            List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(queryWrapper1);
            boolean rainDay = false;
            if (0 < stRainDateDtos.size()) {
                for (StRainDateDto s : stRainDateDtos) {
                    if (StrUtil.isNotEmpty(s.getHhRain()) && 0 > BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(s.getHhRain())))) {
                        rainDay = true;
                        break;
                    }
                }
            }
            if (!rainDay) {
                List<RadarImagesWxDto> radarImagesRainDay = collect.get(key);
                for (RadarImagesWxDto radarImagesWxDto : radarImagesRainDay) {
                    String image = radarImagesWxDto.getImageUrl().substring(radarImagesWxDto.getImageUrl().lastIndexOf("cyzhhhfile/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath1 = path + radarImagesWxDto.getImageUrl().substring(radarImagesWxDto.getImageUrl().lastIndexOf("/")).replace("/", File.separator).replace("\\", File.separator);
                    String imagePath2 = filePath + File.separator + image;
                    radarImagesWxDao.deleteById(radarImagesWxDto.getId());
                    File toDelete1 = new File(imagePath1);
                    toDelete1.delete();
                    deleteFileBase(radarImagesWxDto.getImageUrl());
                    File toDelete2 = new File(imagePath2);
                    toDelete2.delete();
                }
            }
        }
    }

    private void deleteFileBase(String fileUrl) {
        QueryWrapper<FileBase> wrapper = new QueryWrapper<>();
        wrapper.eq("file_url", fileUrl);
        fileBaseDao.delete(wrapper);
    }
}
