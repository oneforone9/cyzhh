package com.essence.job.warn;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.EventConstant;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 摄像头报警
 * 定时任务去扫描 摄像头报警表 然后给 事件eventBase表中添加一份数据
 */
@Component
@Log4j2
public class CameraWarningJob {
    @Resource
    private VideoWarningInfoDao videoWarningInfoDao;

    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Resource
    private EventBaseDao eventBaseDao;
    @Resource
    private UnitBaseDao unitBaseDao;
    @Resource
    private FileBaseDao fileBaseDao;
    @Resource
    private ReaBaseDao reaBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    String videoPath = "http://172.16.52.5:10088/cyzhhhvideowarn";

    /**
     * 定时更新视频在线状态
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void execute() throws ParseException {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行摄像头报警任务,结束.", BACK_JOB);
            return;
        }
        log.info("开始执行摄像头报警任务...");
        Map<String, VideoInfoBase> videoIpAddrMap = new HashMap<>();
        if (CollUtil.isEmpty(videoIpAddrMap)) {
            List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
            if (CollUtil.isNotEmpty(videoInfoBases)) {
                videoIpAddrMap = videoInfoBases.parallelStream().collect(Collectors.toMap(VideoInfoBase::getIpAddress, Function.identity(), (o1, o2) -> o2));

            }
        }
        List<UnitBase> unitBases = unitBaseDao.selectList(new QueryWrapper<>());
        Map<String, UnitBase> unitBaseMap = unitBases.parallelStream().collect(Collectors.toMap(UnitBase::getId, Function.identity()));

        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryRiver = new QueryWrapper<>();
        queryRiver.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryRiver);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }

        Date end = new Date();
        DateTime start = DateUtil.beginOfDay(end);
        String endStr = DateUtil.format(end, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        String startStr = DateUtil.format(start, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        QueryWrapper query = new QueryWrapper();
        query.le("ManagerStartTime", endStr);
        query.ge("ManagerStartTime", startStr);
        List<VideoWarningInfoEntity> videoWarningInfoEntities = videoWarningInfoDao.selectList(query);
        if (CollUtil.isNotEmpty(videoWarningInfoEntities)) {
            for (VideoWarningInfoEntity videoWarningInfoEntity : videoWarningInfoEntities) {
                EventBase eventBase = new EventBase();
                //此id 为摄像头报警的唯一id 将此id 放入eventBase 数据表中作为更新或者 新增的标识
                int dbAlarmID = videoWarningInfoEntity.getDBAlarmID();
                String scannerIP = videoWarningInfoEntity.getScannerIP();
                VideoInfoBase videoInfoBase = videoIpAddrMap.get(scannerIP);
                if (videoInfoBase != null) {
                    String unitId = videoInfoBase.getUnitId();
                    String stBRiverId = videoInfoBase.getStBRiverId();
                    ReaBase reaBase = riverMap.get(stBRiverId);
                    if (reaBase == null) {
                        continue;
                    }
                    String name = reaBase.getReaName();
                    Double lgtd = videoInfoBase.getLgtd();
                    Double lttd = videoInfoBase.getLttd();
                    String name1 = videoInfoBase.getName();
                    eventBase.setPosition(name1);
                    eventBase.setLgtd(lgtd);
                    eventBase.setLttd(lttd);
                    eventBase.setUnitId(unitId);
                    eventBase.setReaId(stBRiverId);
                    eventBase.setReaName(name);
                    eventBase.setUnitName(unitBaseMap.get(videoInfoBase.getUnitId()).getUnitName());
                    eventBase.setEventChannel("6");
                    String substring = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss").replace(":", "").replace("-", "").replace(" ", "").substring(0, 14) + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
                    ;
                    eventBase.setEventCode(substring);

                }
                String ruleNameStr = videoWarningInfoEntity.getRuleNameStr();
                if (ruleNameStr.contains("漂浮物")) {
                    eventBase.setEventType("1");
                    eventBase.setEventClass("11");
                } else if (ruleNameStr.contains("排污")) {
                    eventBase.setEventType("1");
                    eventBase.setEventClass("13");
                } else {
                    eventBase.setEventType("3");
                    eventBase.setEventClass("34");
                }
                String replace = videoWarningInfoEntity.getManagerStartTime().replace("T", " ");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = format.parse(replace);
                eventBase.setEventTime(parse);
                eventBase.setEndTime(DateUtil.endOfDay(parse));
                eventBase.setEventName(videoWarningInfoEntity.getRuleName());
                eventBase.setDbAlarmID(dbAlarmID);
                eventBase.setEventChannel(EventConstant.EVENT_CAMERA_CATCH);
                eventBase.setId(UUID.randomUUID().toString().replace("-", ""));
                eventBase.setProblemDesc(ruleNameStr);
                eventBase.setGmtCreate(new Date());
                eventBase.setIsDelete("0");
                eventBase.setGmtModified(new Date());
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("dbAlarmID", dbAlarmID);
                List<EventBase> eventBases = eventBaseDao.selectList(queryWrapper);
                if (CollUtil.isEmpty(eventBases)) {
                    //给文件添加网络地址
                    FileBase fileBaseEsu = new FileBase();
                    fileBaseEsu.setId(eventBase.getId());
                    fileBaseEsu.setFileFormat(".jpg");
                    //fix 补充代理 摄像头抓拍案件的 图片地址
                    if (StrUtil.isNotEmpty(videoWarningInfoEntity.getThumbnailPath())) {
                        String uri = videoPath + videoWarningInfoEntity.getThumbnailPath().substring(videoWarningInfoEntity.getThumbnailPath().lastIndexOf("//jpgs"));
                        fileBaseEsu.setFileUrl(uri);
                    }
//                    fileBaseEsu.setFileUrl(videoWarningInfoEntity.getThumbnailPath());
                    fileBaseEsu.setTypeId(ItemConstant.QUES_FILE_PREFIX + eventBase.getId());
                    fileBaseEsu.setFileName(ruleNameStr + UUID.randomUUID().toString().replace("-", ""));
                    fileBaseEsu.setFileSize("2048");
                    fileBaseEsu.setIsDelete("0");
                    fileBaseEsu.setGmtCreate(new Date());
                    fileBaseEsu.setGmtModified(new Date());
                    fileBaseDao.insert(fileBaseEsu);
                    eventBase.setStatus("0");
                    eventBase.setSendStatus("0");
                    eventBase.setDealUnit(1);
                    eventBaseDao.insert(eventBase);
                }
            }
        }
        log.info("视频预警信息入库完成.");
    }
}
