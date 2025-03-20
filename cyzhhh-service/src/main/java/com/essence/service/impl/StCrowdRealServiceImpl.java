package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StCrowdRealDao;
import com.essence.dao.entity.StCrowdRealDto;
import com.essence.interfaces.api.StCrowdRealService;
import com.essence.interfaces.model.StCrowdRealEsr;
import com.essence.interfaces.model.StCrowdRealEsu;
import com.essence.interfaces.param.StCrowdRealEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCrowdRealEtoT;
import com.essence.service.converter.ConverterStCrowdRealTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 清水的河 - 实时游客表(StCrowdReal)业务层
 *
 * @author BINX
 * @since 2023-02-28 11:44:14
 */
@Service
public class StCrowdRealServiceImpl extends BaseApiImpl<StCrowdRealEsu, StCrowdRealEsp, StCrowdRealEsr, StCrowdRealDto> implements StCrowdRealService {

    @Autowired
    private StCrowdRealDao stCrowdRealDao;
    @Autowired
    private ConverterStCrowdRealEtoT converterStCrowdRealEtoT;
    @Autowired
    private ConverterStCrowdRealTtoR converterStCrowdRealTtoR;

    public StCrowdRealServiceImpl(StCrowdRealDao stCrowdRealDao, ConverterStCrowdRealEtoT converterStCrowdRealEtoT, ConverterStCrowdRealTtoR converterStCrowdRealTtoR) {
        super(stCrowdRealDao, converterStCrowdRealEtoT, converterStCrowdRealTtoR);
    }

    /**
     * 实时游客客量
     *
     * @param stCrowdRealEsp
     * @return
     */
    @Override
    public List<StCrowdRealDto> getStCrowdReal(StCrowdRealEsp stCrowdRealEsp) {
        List<StCrowdRealDto> stCrowdRealDtos = new ArrayList<>();
        String area = stCrowdRealEsp.getArea();
        String date = stCrowdRealEsp.getDate();
        //热点区域 不为空则查该热点区域当天的所有数据
        if (!"".equals(stCrowdRealEsp.getArea()) && stCrowdRealEsp.getArea() != null) {
            QueryWrapper<StCrowdRealDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("area", area);
            queryWrapper.like("date", date);
            queryWrapper.orderByAsc("date");
            stCrowdRealDtos = stCrowdRealDao.selectList(queryWrapper);

        } else {
            //否则根据时间查询当天所有热点区域的最新 数据
            stCrowdRealDtos = stCrowdRealDao.getStCrowdReal(date);
        }
        for (int i = 0; i < stCrowdRealDtos.size(); i++) {
            StCrowdRealDto stCrowdRealDto = stCrowdRealDtos.get(i);
            String area1 = stCrowdRealDto.getArea();
            Integer bindId = this.selectBindID(area1);
            stCrowdRealDto.setBindId(bindId);
        }

        return stCrowdRealDtos;
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
