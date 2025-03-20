package com.essence.service.impl;

import com.essence.dao.VideoStatusRecordDao;
import com.essence.dao.entity.VideoStatusRecord;
import com.essence.interfaces.api.VideoStatusRecordService;
import com.essence.interfaces.model.StatisticsBase;
import com.essence.interfaces.model.VideoStatusRecordEsr;
import com.essence.interfaces.model.VideoStatusRecordEsu;
import com.essence.interfaces.param.VideoStatusRecordEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterVideoStatusRecordEtoT;
import com.essence.service.converter.ConverterVideoStatusRecordTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class VideoStatusRecordServiceImpl extends BaseApiImpl<VideoStatusRecordEsu, VideoStatusRecordEsp, VideoStatusRecordEsr, VideoStatusRecord> implements VideoStatusRecordService {

    @Autowired
    private VideoStatusRecordDao videoStatusRecordDao;
    @Autowired
    private ConverterVideoStatusRecordEtoT converterVideoStatusRecordEtoT;
    @Autowired
    private ConverterVideoStatusRecordTtoR converterVideoStatusRecordTtoR;

    public VideoStatusRecordServiceImpl(VideoStatusRecordDao videoStatusRecordDao, ConverterVideoStatusRecordEtoT converterVideoStatusRecordEtoT, ConverterVideoStatusRecordTtoR converterVideoStatusRecordTtoR) {
        super(videoStatusRecordDao, converterVideoStatusRecordEtoT, converterVideoStatusRecordTtoR);
    }

    @Override
    public List<VideoStatusRecordEsr> latestStauts() {
        return converterVideoStatusRecordTtoR.toList(videoStatusRecordDao.latestStauts());
    }

    @Override
    public List<VideoStatusRecordEsr> latestStauts2() {
        //因海康华为视频与其他利旧视频分开统计，所以用新的方法
        return converterVideoStatusRecordTtoR.toList(videoStatusRecordDao.latestStauts2());
    }

    @Override
    public List<StatisticsBase> countLatestStatus() {
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = this.latestStauts2();
        Map<String, Long> collect = videoStatusRecordEsrs.stream().collect(Collectors.groupingBy(VideoStatusRecordEsr::getStatus, Collectors.counting()));
        List<StatisticsBase> resultList = new ArrayList<>();
        collect.entrySet().forEach(p->{
            resultList.add(new StatisticsBase(p.getKey(), p.getValue()));
        });
        return resultList;
    }

    /**
     * 摄像头在线/离线统计（总）智能摄像头+利旧摄像头
     * @return
     */
    @Override
    public List<StatisticsBase> countLatestStatusAll() {
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = this.latestStauts();
        Map<String, Long> collect = videoStatusRecordEsrs.stream().collect(Collectors.groupingBy(VideoStatusRecordEsr::getStatus, Collectors.counting()));
        List<StatisticsBase> resultList = new ArrayList<>();
        collect.entrySet().forEach(p->{
            resultList.add(new StatisticsBase(p.getKey(), p.getValue()));
        });
        return resultList;
    }

    @Override
    public List<VideoStatusRecordEsr> latestYSYStauts() {
        return converterVideoStatusRecordTtoR.toList(videoStatusRecordDao.latestYSYStauts());
    }

    @Override
    public List<VideoStatusRecordEsr> latestYUSYStauts() {
        return converterVideoStatusRecordTtoR.toList(videoStatusRecordDao.latestYUSYStauts());
    }
}
