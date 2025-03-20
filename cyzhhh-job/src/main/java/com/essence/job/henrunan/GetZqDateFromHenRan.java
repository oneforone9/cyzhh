package com.essence.job.henrunan;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StWaterRateDao;
import com.essence.dao.StWaterRateLatestDao;
import com.essence.dao.TBllElementDao;
import com.essence.dao.entity.StWaterRateEntity;
import com.essence.dao.entity.StWaterRateLatestDto;
import com.essence.dao.entity.henrunan.StWaterRateZqOrgDto;
import com.essence.dao.entity.henrunan.TBllElementDto;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

@Component
@Log4j2
public class GetZqDateFromHenRan {
    @Autowired
    StWaterRateLatestDao stWaterRateLatestDao;
    @Resource
    private TBllElementDao tBllElementDao;
    @Resource
    private StWaterRateDao stWaterRateDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(cron = "0 0/2 * * * ? ")
    public void demoJobHandler() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行同步恒润安任务,结束.", BACK_JOB);
            return;
        }
        log.info("同步恒润安开始");
        run();
        log.info("同步恒润安结束");
    }

    public void run() {
        Date end = new Date();
        DateTime start = DateUtil.offsetMinute(end, -20);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.le("RecvTime", end);
        queryWrapper.ge("RecvTime", start);
        List<TBllElementDto> tBllElementDtos = tBllElementDao.selectList(queryWrapper);
        //过滤 只需要  ElementType----> 瞬时流量	0X27     瞬时河道水位	0X39
        if (CollUtil.isNotEmpty(tBllElementDtos)) {
            Map<String, List<TBllElementDto>> stationMap = tBllElementDtos.parallelStream().collect(Collectors.groupingBy(TBllElementDto::getStationaddr));

            for (String s : stationMap.keySet()) {
                List<TBllElementDto> tBllElementDtos1 = stationMap.get(s);
                List<TBllElementDto> collect = tBllElementDtos1.parallelStream().filter(tBllElementDto -> {
                    boolean b = "0X27".equals(tBllElementDto.getElementtype()) || "0X39".equals(tBllElementDto.getElementtype());
                    return b;
                }).collect(Collectors.toList());
                //通过接收时间分组
                Map<Date, List<TBllElementDto>> collect1 = collect.parallelStream().collect(Collectors.groupingBy(TBllElementDto::getRecvtime));
                for (Date date : collect1.keySet()) {
                    StWaterRateEntity stWaterRateZqOrgDto = new StWaterRateEntity();
                    List<TBllElementDto> tBllElementDtos2 = collect1.get(date);
                    if (CollUtil.isEmpty(tBllElementDtos2)) {
                        continue;
                    }
                    //瞬时流量	0X27  进行累加
                    List<TBllElementDto> collect2 = tBllElementDtos2.parallelStream().filter(tBllElementDto -> {
                        boolean equals = "0X27".equals(tBllElementDto.getElementtype());
                        return equals;
                    }).collect(Collectors.toList());
                    List<String> collect4 = collect2.parallelStream().map(TBllElementDto::getDatacontent).collect(Collectors.toList());
                    BigDecimal rate = new BigDecimal(0);
                    rate = collect4.parallelStream().map(s1 -> {
                        return new BigDecimal(s1);
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);
                    //采集时间
                    if (collect2.size() == 0) {
                        continue;
                    }
                    Date datatime = collect2.get(0).getDatatime();
                    String format = DateUtil.format(datatime, "yyyy-MM-dd HH:mm:ss");
                    //瞬时河道水  0X39 累加求平均
                    List<TBllElementDto> collect3 = tBllElementDtos2.parallelStream().filter(tBllElementDto -> {
                        boolean equals = "0X39".equals(tBllElementDto.getElementtype());
                        return equals;
                    }).collect(Collectors.toList());
                    BigDecimal position = new BigDecimal(0);
                    List<String> collect5 = collect3.parallelStream().map(TBllElementDto::getDatacontent).collect(Collectors.toList());
                    position = collect5.parallelStream().map(s1 -> {
                        return new BigDecimal(s1);
                    }).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(collect5.size()), 2, RoundingMode.HALF_UP);
                    stWaterRateZqOrgDto.setDid(s.substring(2));
                    stWaterRateZqOrgDto.setMomentRate(rate.toString());
                    stWaterRateZqOrgDto.setCtime(format);
                    stWaterRateZqOrgDto.setMomentRiverPosition(position.toString());
                    QueryWrapper query = new QueryWrapper();
                    query.eq("did", s.substring(2));
                    query.eq("ctime", format);
                    List<StWaterRateZqOrgDto> stWaterRateZqOrgDtos = stWaterRateDao.selectList(query);
                    if (CollUtil.isNotEmpty(stWaterRateZqOrgDtos)) {
                        stWaterRateDao.update(stWaterRateZqOrgDto, query);
                    } else {
                        stWaterRateDao.insert(stWaterRateZqOrgDto);
                    }

                    //fix 填充最新的
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("did", stWaterRateZqOrgDto.getDid());
                    List<StWaterRateLatestDto> stWaterRateLatestDtos = stWaterRateLatestDao.selectList(wrapper);
                    StWaterRateLatestDto stWaterRateLatestDto = new StWaterRateLatestDto();
                    stWaterRateLatestDto.setAddr("liuliang");
                    stWaterRateLatestDto.setMomentRate(stWaterRateZqOrgDto.getMomentRate());
                    stWaterRateLatestDto.setDid(stWaterRateZqOrgDto.getDid());
                    stWaterRateLatestDto.setPreMomentRate(stWaterRateZqOrgDto.getPreMomentRate());
                    stWaterRateLatestDto.setMomentRiverPosition(stWaterRateZqOrgDto.getMomentRiverPosition());
                    stWaterRateLatestDto.setCtime(stWaterRateZqOrgDto.getCtime());
                    if (CollUtil.isNotEmpty(stWaterRateLatestDtos)) {
                        //此处更新流量站的最新数据通过站点did
                        stWaterRateLatestDao.update(stWaterRateLatestDto, wrapper);
                    } else {
                        stWaterRateLatestDao.insert(stWaterRateLatestDto);
                    }
                }

            }
        }
    }
}
