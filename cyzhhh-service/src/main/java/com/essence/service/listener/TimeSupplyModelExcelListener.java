package com.essence.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.WaterPortTimeSupplyDao;
import com.essence.dao.WaterSupplyCaseDao;
import com.essence.dao.entity.TimeSupplyModelEntity;
import com.essence.dao.entity.WaterPortTimeSupplyDto;
import com.essence.dao.entity.WaterSupplyCaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 补水口时间序列流量Excel解析
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/6 14:50
 */
@Slf4j
public class TimeSupplyModelExcelListener extends AnalysisEventListener<TimeSupplyModelEntity> {

    private Long id;
    private String waterPortName;
    private List<TimeSupplyModelEntity> waterPortTimeSupplyDtoList = new ArrayList<>();

    WaterPortTimeSupplyDao waterPortTimeSupplyDao;
    WaterSupplyCaseDao waterSupplyCaseDao;

    public TimeSupplyModelExcelListener(Long id, String waterPortName, WaterPortTimeSupplyDao waterPortTimeSupplyDao, WaterSupplyCaseDao waterSupplyCaseDao) {
        this.id = id;
        this.waterPortName = waterPortName;
        this.waterPortTimeSupplyDao = waterPortTimeSupplyDao;
        this.waterSupplyCaseDao = waterSupplyCaseDao;
    }

    @Override
    public void invoke(TimeSupplyModelEntity timeSupplyModelEntity, AnalysisContext analysisContext) {
        this.waterPortTimeSupplyDtoList.add(timeSupplyModelEntity);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(this.waterPortTimeSupplyDtoList));
        if(this.id == null) {
            WaterPortTimeSupplyDto entity = new WaterPortTimeSupplyDto();
            entity.setWaterPortName(this.waterPortName);
            entity.setTimesupply(String.valueOf(jsonArray));
            QueryWrapper<WaterPortTimeSupplyDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("water_port_name", this.waterPortName);
            WaterPortTimeSupplyDto waterPortTimeSupplyDto = waterPortTimeSupplyDao.selectOne(queryWrapper);
            if(null != waterPortTimeSupplyDto) {
                QueryWrapper<WaterPortTimeSupplyDto> updateWrapper1 = new QueryWrapper<>();
                updateWrapper1.eq("water_port_name", this.waterPortName);
                waterPortTimeSupplyDao.update(entity, updateWrapper1);
            } else {
                waterPortTimeSupplyDao.insert(entity);
            }
        } else {
            WaterSupplyCaseDto entity = new WaterSupplyCaseDto();
            entity.setId(this.id);
            entity.setSupplyWay(1);
            entity.setTimeSupply(String.valueOf(jsonArray));
            waterSupplyCaseDao.updateById(entity);
        }
    }
}
