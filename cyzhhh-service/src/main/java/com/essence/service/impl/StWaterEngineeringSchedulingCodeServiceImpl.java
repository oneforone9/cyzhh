package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.FileBaseDao;
import com.essence.dao.StWaterEngineeringSchedulingCodeDao;
import com.essence.dao.entity.FileBase;
import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.interfaces.api.StWaterEngineeringSchedulingCodeService;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingCodeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterFileBaseTtoR;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingCodeEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingCodeTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 水系联调-工程调度调度指令记录(StWaterEngineeringSchedulingCode)业务层
 * @author majunjie
 * @since 2023-07-04 14:57:09
 */
@Service
public class StWaterEngineeringSchedulingCodeServiceImpl extends BaseApiImpl<StWaterEngineeringSchedulingCodeEsu, StWaterEngineeringSchedulingCodeEsp, StWaterEngineeringSchedulingCodeEsr, StWaterEngineeringSchedulingCodeDto> implements StWaterEngineeringSchedulingCodeService {

    @Autowired
    private StWaterEngineeringSchedulingCodeDao stWaterEngineeringSchedulingCodeDao;
    @Autowired
    private ConverterStWaterEngineeringSchedulingCodeEtoT converterStWaterEngineeringSchedulingCodeEtoT;
    @Autowired
    private ConverterStWaterEngineeringSchedulingCodeTtoR converterStWaterEngineeringSchedulingCodeTtoR;
@Autowired
private FileBaseDao fileBaseDao;
    @Autowired
    private ConverterFileBaseTtoR converterFileBaseTtoR;
    public StWaterEngineeringSchedulingCodeServiceImpl(StWaterEngineeringSchedulingCodeDao stWaterEngineeringSchedulingCodeDao, ConverterStWaterEngineeringSchedulingCodeEtoT converterStWaterEngineeringSchedulingCodeEtoT, ConverterStWaterEngineeringSchedulingCodeTtoR converterStWaterEngineeringSchedulingCodeTtoR) {
        super(stWaterEngineeringSchedulingCodeDao, converterStWaterEngineeringSchedulingCodeEtoT, converterStWaterEngineeringSchedulingCodeTtoR);
    }

    @Override
    public void saveStWaterEngineeringSchedulingCode(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto) {
        stWaterEngineeringSchedulingCodeDao.insert(stWaterEngineeringSchedulingCodeDto);
    }

    @Override
    public StWaterEngineeringSchedulingCodeEsr selectFloodDispatchCodeById(String id) {
        StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto = stWaterEngineeringSchedulingCodeDao.selectById(id);
        StWaterEngineeringSchedulingCodeEsr stWaterEngineeringSchedulingCodeEsr = new StWaterEngineeringSchedulingCodeEsr();
        if (null != stWaterEngineeringSchedulingCodeDto) {
            stWaterEngineeringSchedulingCodeEsr = converterStWaterEngineeringSchedulingCodeTtoR.toBean(stWaterEngineeringSchedulingCodeDto);
            List<FileBase> fileBases = Optional.ofNullable(fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().eq(FileBase::getTypeId, "FXDD" + stWaterEngineeringSchedulingCodeDto.getId()).eq(FileBase::getIsDelete, "0"))).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(fileBases)) {
                List<FileBaseEsr> fileBaseEsrs = Optional.ofNullable(converterFileBaseTtoR.toList(fileBases)).orElse(Lists.newArrayList());
                stWaterEngineeringSchedulingCodeEsr.setFile(fileBaseEsrs);
            }
        }
        return stWaterEngineeringSchedulingCodeEsr;
    }
}
