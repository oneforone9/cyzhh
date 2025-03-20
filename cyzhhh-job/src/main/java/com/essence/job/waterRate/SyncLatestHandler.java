package com.essence.job.waterRate;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StWaterRateDao;
import com.essence.dao.StWaterRateLatestDao;
import com.essence.dao.entity.StWaterRateEntity;
import com.essence.dao.entity.StWaterRateLatestDto;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 同步各测站最新数据
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/6/25 10:18
 */
@Component
@Log4j2
public class SyncLatestHandler {

    @Autowired
    StWaterRateDao stWaterRateDao;
    @Autowired
    StWaterRateLatestDao stWaterRateLatestDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 5分钟同步数据, 同步各测站24h最新数据, 没有该测站则插入
     */
    @SneakyThrows
    @Scheduled(cron = "0 0 3 * * ?")
    public void execute() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行各测站最新数据任务,结束.", BACK_JOB);
            return;
        }
        log.info("开始同步各测站最新数据...");
        DateTime now = DateUtil.dateNew(new Date());
        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date yesterday = DateUtil.yesterday();
        QueryWrapper<StWaterRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("ctime", yesterday);
        queryWrapper.le("ctime", now);
        List<StWaterRateEntity> origenList1 = stWaterRateDao.selectList(queryWrapper);
        QueryWrapper<StWaterRateEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ge("ctime", zzFormat.format(zqFormat.parse(String.valueOf(yesterday))));
        queryWrapper1.le("ctime", zzFormat.format(zqFormat.parse(String.valueOf(now))));
        List<StWaterRateEntity> origenList2 = stWaterRateDao.selectList(queryWrapper1);
        List<StWaterRateEntity> origenList = new ArrayList<>();
        origenList.addAll(origenList1);
        origenList.addAll(origenList2);
        Map<String, List<StWaterRateEntity>> stMap = origenList.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
        for (String s : stMap.keySet()) {
            List<StWaterRateEntity> stData = stMap.get(s);
            List<StWaterRateEntity> sortedStData = stData.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime).reversed()).collect(Collectors.toList());
            StWaterRateEntity stLatest = sortedStData.get(0);
            QueryWrapper<StWaterRateLatestDto> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("did", s);
            StWaterRateLatestDto stLatestData = stWaterRateLatestDao.selectOne(queryWrapper2);
            StWaterRateLatestDto entity = new StWaterRateLatestDto();
            BeanUtils.copyProperties(stLatest, entity);
            if (null != stLatestData) {
                QueryWrapper<StWaterRateLatestDto> updateWrapper = new QueryWrapper<>();
                updateWrapper.eq("did", s);
                stWaterRateLatestDao.update(entity, updateWrapper);
            } else {
                stWaterRateLatestDao.insert(entity);
            }

        }
        log.info("同步各测站最新数据结束.");
    }

}
