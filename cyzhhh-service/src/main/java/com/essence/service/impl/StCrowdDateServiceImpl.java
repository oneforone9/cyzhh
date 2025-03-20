package com.essence.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.CrowdDateRequest;
import com.essence.dao.StCrowdDateDao;
import com.essence.dao.entity.StCrowdDateDto;
import com.essence.dao.entity.StCrowdRealDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StCrowdDateService;

import com.essence.interfaces.dot.CrowDataDto;
import com.essence.interfaces.model.StCrowdDateEsr;
import com.essence.interfaces.model.StCrowdDateEsu;
import com.essence.interfaces.param.StCrowdDateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCrowdDateEtoT;
import com.essence.service.converter.ConverterStCrowdDateTtoR;
import com.essence.service.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 清水的河 - 用水人数量(StCrowdDate)业务层
 * @author BINX
 * @since 2023-01-12 17:36:44
 */
@Service
public class StCrowdDateServiceImpl extends BaseApiImpl<StCrowdDateEsu, StCrowdDateEsp, StCrowdDateEsr, StCrowdDateDto> implements StCrowdDateService {


    @Autowired
    private StCrowdDateDao stCrowdDateDao;
    @Autowired
    private ConverterStCrowdDateEtoT converterStCrowdDateEtoT;
    @Autowired
    private ConverterStCrowdDateTtoR converterStCrowdDateTtoR;

    public StCrowdDateServiceImpl(StCrowdDateDao stCrowdDateDao, ConverterStCrowdDateEtoT converterStCrowdDateEtoT, ConverterStCrowdDateTtoR converterStCrowdDateTtoR) {
        super(stCrowdDateDao, converterStCrowdDateEtoT, converterStCrowdDateTtoR);
    }

    @Override
    public CrowDataDto getManageRiverList(CrowdDateRequest crowdDateRequest) {
        CrowDataDto crowDataDto = new CrowDataDto();
        Date start = crowdDateRequest.getStart();
        Date end = crowdDateRequest.getEnd();
        List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
        List<List<Integer>> dateList = new ArrayList<>();
        List<String> date = new ArrayList<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            String format = DateUtil.format(dateTimes.get(i), "yyyy-MM-dd");
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("date",format);
            List<StCrowdDateDto> stCrowdDateDtos = stCrowdDateDao.selectList(wrapper);

            if (CollUtil.isNotEmpty(stCrowdDateDtos)){
                List<String> areaList = stCrowdDateDtos.stream().sorted(Comparator.comparing(StCrowdDateDto::getArea)).map(StCrowdDateDto::getArea).collect(Collectors.toList());
                crowDataDto.setArea(areaList);
            }

            List<Integer> rvnmList = new ArrayList<>();
            if (CollUtil.isNotEmpty(stCrowdDateDtos)){
                rvnmList = stCrowdDateDtos.stream().sorted(Comparator.comparing(StCrowdDateDto::getArea)).map(StCrowdDateDto::getNum).collect(Collectors.toList());
                date.add(format);
                dateList.add(rvnmList);
            }

        }
        crowDataDto.setDateList(dateList);
        crowDataDto.setDate(date);
        return crowDataDto;
    }

    /**
     * 日游客量-根据日期查询
     * @param stCrowdDateEsp
     * @return
     */
    @Override
    public List<StCrowdDateDto> getStCrowdDate(StCrowdDateEsp stCrowdDateEsp) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date",stCrowdDateEsp.getDate());
        if (StringUtil.isNotEmpty(stCrowdDateEsp.getArea()) ){
            wrapper.like("area",stCrowdDateEsp.getArea());
        }
        List<StCrowdDateDto> stCrowdDateDtos = stCrowdDateDao.selectList(wrapper);
        for (int i = 0; i < stCrowdDateDtos.size(); i++) {
            StCrowdDateDto stCrowdDateDto = stCrowdDateDtos.get(i);
            String area1 = stCrowdDateDto.getArea();
            Integer bindId = this.selectBindID(area1);
            stCrowdDateDto.setBindId(bindId);
        }
        return stCrowdDateDtos;
    }

    protected Integer selectBindID(String area) {
        Map<String, Object> map = new HashMap<>();
        map.put("市管河道通惠河高碑店湖钓鱼区", 1);
        map.put("北小河公园", 2);
        map.put("市管河道北护城河坝河泄洪道闸上游", 3);
        map.put("四得公园", 4);
        map.put("市管河道南护城河龙潭闸上游游泳钓区", 5);
        map.put("将府公园", 6);
        map.put("市管河道通惠河双桥上下游钓鱼区", 7);
        map.put("马家湾湿地公园", 8);
        map.put("萧太后河源头广场", 9);
        map.put("黄草湾郊野公园", 10);
        map.put("凉水河珊瑚桥下游右岸钓鱼区", 11);
        map.put("凉水河珊瑚桥上游左岸钓鱼区", 12);
        map.put("朝阳公园", 13);
        map.put("大望京公园", 14);
        map.put("温榆河公园", 15);
        Integer bindId  = (Integer) map.get(area);
        return bindId;
    }
}
