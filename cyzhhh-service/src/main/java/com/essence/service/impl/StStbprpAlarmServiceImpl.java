package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StStbprpAlarmDao;
import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.interfaces.api.StStbprpAlarmService;
import com.essence.interfaces.model.StStbprpAlarmEsr;
import com.essence.interfaces.model.StStbprpAlarmEsu;
import com.essence.interfaces.param.StStbprpAlarmEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStStbprpAlarmEtoT;
import com.essence.service.converter.ConverterStStbprpAlarmTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 水位流量站报警阀值配置表(StStbprpAlarm)业务层
 *
 * @author BINX
 * @since 2023-02-25 16:53:22
 */
@Service
public class StStbprpAlarmServiceImpl extends BaseApiImpl<StStbprpAlarmEsu, StStbprpAlarmEsp, StStbprpAlarmEsr, StStbprpAlarmDto> implements StStbprpAlarmService {

    @Autowired
    private StStbprpAlarmDao stStbprpAlarmDao;
    @Autowired
    private ConverterStStbprpAlarmEtoT converterStStbprpAlarmEtoT;
    @Autowired
    private ConverterStStbprpAlarmTtoR converterStStbprpAlarmTtoR;

    public StStbprpAlarmServiceImpl(StStbprpAlarmDao stStbprpAlarmDao, ConverterStStbprpAlarmEtoT converterStStbprpAlarmEtoT, ConverterStStbprpAlarmTtoR converterStStbprpAlarmTtoR) {
        super(stStbprpAlarmDao, converterStStbprpAlarmEtoT, converterStStbprpAlarmTtoR);
    }

    /**
     * 测站的报警阀值数据
     *
     * @param stStbprpAlarmEsu
     * @return
     */
    @Override
    public StStbprpAlarmDto selectStStbprpAlarm(StStbprpAlarmEsu stStbprpAlarmEsu) {
        return stStbprpAlarmDao.selectList(new LambdaQueryWrapper<StStbprpAlarmDto>()
                .eq(StStbprpAlarmDto::getStcd, stStbprpAlarmEsu.getStcd())
        ).stream().findFirst().orElse(null);
    }

    /**
     * 删除数据配置
     *
     * @param stcd
     * @return
     */
    @Override
    public Integer deleteStStbprpAlarm(String stcd) {
        QueryWrapper<StStbprpAlarmDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stcd", stcd);
        return stStbprpAlarmDao.delete(queryWrapper);
    }
}
