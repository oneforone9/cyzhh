package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.GisUtils;
import com.essence.dao.TWorkorderRecordPointDao;
import com.essence.dao.entity.TWorkorderRecordPointDto;
import com.essence.interfaces.api.TWorkorderRecordPointService;
import com.essence.interfaces.model.TWorkorderRecordPointEsr;
import com.essence.interfaces.model.TWorkorderRecordPointEsu;
import com.essence.interfaces.param.TWorkorderRecordPointEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTWorkorderRecordPointEtoT;
import com.essence.service.converter.ConverterTWorkorderRecordPointTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息(TWorkorderRecordPoint)业务层
 * @author liwy
 * @since 2023-05-07 12:10:42
 */
@Service
public class TWorkorderRecordPointServiceImpl extends BaseApiImpl<TWorkorderRecordPointEsu, TWorkorderRecordPointEsp, TWorkorderRecordPointEsr, TWorkorderRecordPointDto> implements TWorkorderRecordPointService {

    @Autowired
    private TWorkorderRecordPointDao tWorkorderRecordPointDao;
    @Autowired
    private ConverterTWorkorderRecordPointEtoT converterTWorkorderRecordPointEtoT;
    @Autowired
    private ConverterTWorkorderRecordPointTtoR converterTWorkorderRecordPointTtoR;

    public TWorkorderRecordPointServiceImpl(TWorkorderRecordPointDao tWorkorderRecordPointDao, ConverterTWorkorderRecordPointEtoT converterTWorkorderRecordPointEtoT, ConverterTWorkorderRecordPointTtoR converterTWorkorderRecordPointTtoR) {
        super(tWorkorderRecordPointDao, converterTWorkorderRecordPointEtoT, converterTWorkorderRecordPointTtoR);
    }

    /**
     * 判断是否在打卡范围之内、是否是在必打卡点位
     * @param tWorkorderRecordPointEsu
     * @return
     */
    @Override
    public Object selectClockRange(TWorkorderRecordPointEsu tWorkorderRecordPointEsu) {
        Map<String,Object> map = new HashMap();
        Integer flag = 0;
        String pointName = null;
        BigDecimal range = null;
        String id = null;

        //1、根据orderId和经纬度判断签到点位是否在必打卡点位15米范围之内
        //前端传来的坐标
        BigDecimal lgtd = tWorkorderRecordPointEsu.getLgtd();
        BigDecimal lttd = tWorkorderRecordPointEsu.getLttd();

        QueryWrapper<TWorkorderRecordPointDto> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",tWorkorderRecordPointEsu.getOrderId());
        queryWrapper.eq("is_complete_clock", 0);
        List<TWorkorderRecordPointDto> list = tWorkorderRecordPointDao.selectList(queryWrapper);
        if(list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                TWorkorderRecordPointDto tWorkorderRecordPointDto = list.get(i);
                BigDecimal lgtd2 = tWorkorderRecordPointDto.getLgtd();
                BigDecimal lttd2 = tWorkorderRecordPointDto.getLttd();
                //获取距离
                Double distance = GisUtils.distance( lgtd.doubleValue(),lttd.doubleValue() , lgtd2.doubleValue(),lttd2.doubleValue() );
                BigDecimal distanceC = new BigDecimal(distance);
                BigDecimal distance20 = new BigDecimal(20);
                if (distanceC.compareTo(distance20) < 0){
                    //距离小于20米
                    flag = 1;
                    //2如果是在必打卡点位，判断是哪个必打卡点位，
                    pointName = tWorkorderRecordPointDto.getPointName();
                    range = distanceC.setScale(2, BigDecimal.ROUND_HALF_UP);
                    id = tWorkorderRecordPointDto.getId();
                    break;
                }
            }
        }
        map.put("flag",flag);
        map.put("pointName",pointName);
        map.put("range",range);
        map.put("id",id);
        return map;
        //3前端将此工单的完成打卡更新为已完成打卡(前端操作)
    }

    /**
     * 判断结束工单时是否再所有的必打卡点位完成打卡
     * @param orderId
     * @return
     */
    @Override
    public Object selectIsALLComplete(String orderId) {
        Map<String,Object> map = new HashMap();
        Integer flag = 1;
        String pointName = null;

        QueryWrapper<TWorkorderRecordPointDto> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",orderId);
        queryWrapper.eq("is_complete_clock", 0);
        List<TWorkorderRecordPointDto> list = tWorkorderRecordPointDao.selectList(queryWrapper);
        if(list.size()>0){
            flag= 0;
            for (int i = 0; i < list.size(); i++) {
                if(null != pointName && !"".equals(pointName)){
                    pointName = pointName + ","  + list.get(i).getPointName();

                }else{
                    pointName = list.get(i).getPointName();
                }
            }
        }
        map.put("flag",flag);
        map.put("pointName",pointName);
        return map;
    }
}
