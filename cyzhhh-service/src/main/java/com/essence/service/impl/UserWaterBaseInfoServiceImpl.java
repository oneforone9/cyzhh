package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.dao.UserWaterBaseInfoDao;
import com.essence.dao.UserWaterDao;
import com.essence.dao.entity.UserWaterBaseInfoDto;
import com.essence.dao.entity.UserWaterDto;
import com.essence.interfaces.api.UserWaterBaseInfoService;
import com.essence.interfaces.api.UserWaterService;
import com.essence.interfaces.model.UserWaterBaseInfoEsr;
import com.essence.interfaces.model.UserWaterBaseInfoEsu;
import com.essence.interfaces.model.UserWaterEsr;
import com.essence.interfaces.model.UserWaterEsu;
import com.essence.interfaces.param.UserWaterBaseInfoEsp;
import com.essence.interfaces.param.UserWaterEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterUserWaterBaseInfoEtoT;
import com.essence.service.converter.ConverterUserWaterBaseInfoTtoR;
import com.essence.service.converter.ConverterUserWaterEtoT;
import com.essence.service.converter.ConverterUserWaterTtoR;
import com.essence.service.listener.UserWaterBaeInfoListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用水户取水量(UserWater)业务层
 * @author BINX
 * @since 2023-01-04 17:50:29
 */
@Service
public class UserWaterBaseInfoServiceImpl extends BaseApiImpl<UserWaterBaseInfoEsu, UserWaterBaseInfoEsp, UserWaterBaseInfoEsr, UserWaterBaseInfoDto> implements UserWaterBaseInfoService {

    @Autowired
    private UserWaterBaseInfoDao userWaterBaseInfoDao;
    @Autowired
    private ConverterUserWaterBaseInfoEtoT converterUserWaterBaseInfoEtoT;
    @Autowired
    private ConverterUserWaterBaseInfoTtoR converterUserWaterBaseInfoTtoR;

    public UserWaterBaseInfoServiceImpl(UserWaterBaseInfoDao userWaterBaseInfoDao,
                        ConverterUserWaterBaseInfoEtoT converterUserWaterBaseInfoEtoT,
                        ConverterUserWaterBaseInfoTtoR converterUserWaterBaseInfoTtoR) {
        super(userWaterBaseInfoDao, converterUserWaterBaseInfoEtoT, converterUserWaterBaseInfoTtoR);
    }

    @Override
    public List<UserWaterStatisticDto> selectByType(String type) {
//        List<UserWaterBaseInfoDto> userWaterBaseInfoDtos = userWaterBaseInfoDao.selectList(new QueryWrapper<UserWaterBaseInfoDto>().lambda().eq(UserWaterBaseInfoDto::getFileType, type));

        return Collections.emptyList();
    }
}
