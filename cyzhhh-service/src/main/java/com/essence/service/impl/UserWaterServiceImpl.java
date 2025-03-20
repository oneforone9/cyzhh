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
import com.essence.interfaces.api.UserWaterService;
import com.essence.interfaces.model.UserWaterBaseInfoEsu;
import com.essence.interfaces.model.UserWaterEsr;
import com.essence.interfaces.model.UserWaterEsu;
import com.essence.interfaces.param.UserWaterEsp;
import com.essence.service.baseimpl.BaseApiImpl;
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
public class UserWaterServiceImpl extends BaseApiImpl<UserWaterEsu, UserWaterEsp, UserWaterEsr, UserWaterDto> implements UserWaterService {


    @Autowired
    private UserWaterDao userWaterDao;
    @Autowired
    private ConverterUserWaterEtoT converterUserWaterEtoT;
    @Autowired
    private ConverterUserWaterTtoR converterUserWaterTtoR;
    @Resource
    private UserWaterBaseInfoDao userWaterBaseInfoDao;


    public UserWaterServiceImpl(UserWaterDao userWaterDao, ConverterUserWaterEtoT converterUserWaterEtoT, ConverterUserWaterTtoR converterUserWaterTtoR) {
        super(userWaterDao, converterUserWaterEtoT, converterUserWaterTtoR);
    }

    @Override
    public List<UserWaterStatisticDto> getStatistic2(String fileType) {
        List<UserWaterStatisticDto> userWaterStatisticDtos = new ArrayList<>();
        List<UserWaterBaseInfoDto> userWaterBaseInfoDtos = userWaterBaseInfoDao.selectList(new QueryWrapper<UserWaterBaseInfoDto>().lambda().eq(UserWaterBaseInfoDto::getFileType,fileType));
        Map<String, List<UserWaterBaseInfoDto>> userWaterBaseMap = userWaterBaseInfoDtos.stream().collect(Collectors.groupingBy(UserWaterBaseInfoDto::getFileType));
        for (Map.Entry<String, List<UserWaterBaseInfoDto>> stringListEntry : userWaterBaseMap.entrySet()) {
            String fileTypeKey = stringListEntry.getKey();
            List<UserWaterBaseInfoDto> userWaterList = stringListEntry.getValue();
            if(!userWaterList.isEmpty()) {
                Map<String, List<UserWaterBaseInfoDto>> userWaterList1 = userWaterList.stream().collect(Collectors.groupingBy(UserWaterBaseInfoDto::getEleNum));

                for (Map.Entry<String, List<UserWaterBaseInfoDto>> listEntry : userWaterList1.entrySet()) {
                    List<UserWaterBaseInfoDto> userWaterList2 = listEntry.getValue();
                    UserWaterStatisticDto userWaterStatisticDto = new UserWaterStatisticDto();
                    userWaterStatisticDto.setUserName(userWaterList2.get(0).getUserName());
                    userWaterStatisticDto.setType(userWaterList2.get(0).getType());
                    userWaterStatisticDto.setFileType(fileTypeKey);
                    userWaterStatisticDto.setOutNum(userWaterList2.size());
                    userWaterStatisticDto.setEleNum(userWaterList2.get(0).getEleNum());
                    userWaterStatisticDtos.add(userWaterStatisticDto);
                }
            }
        }
        return userWaterStatisticDtos;
    }


    @Override
    public List<UserWaterStatisticDto> getStatistic() {
        List<UserWaterStatisticDto> res = new ArrayList<>();
        List<UserWaterStatisticDto> list = new ArrayList<>();
        DateTime yearStart = DateUtil.beginOfYear(new Date());
        DateTime yearEnd = DateUtil.endOfYear(new Date());

        String startStr = DateUtil.format(yearStart, "yyyy-MM");
        String endStr = DateUtil.format(yearEnd, "yyyy-MM");

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("date",startStr);
        wrapper.le("date",endStr);
        List<UserWaterDto> userWaterDtos = userWaterDao.selectList(wrapper);
        userWaterDtos = userWaterDtos.stream().filter(userWaterDto -> { return  null != userWaterDto.getWater() && !userWaterDto.getWater().equals("0") && !userWaterDto.getWater().equals("0.0");}).collect(Collectors.toList());
       if (CollUtil.isNotEmpty(userWaterDtos)){
           list = BeanUtil.copyToList(userWaterDtos, UserWaterStatisticDto.class);
           Map<String, List<UserWaterStatisticDto>> userNameMap = list.parallelStream().collect(Collectors.groupingBy(UserWaterStatisticDto::getUserName));
           for (String s : userNameMap.keySet()) {
               List<UserWaterStatisticDto> list1 = userNameMap.get(s);
               Integer outNum = list1.get(0).getOutNum();
               BigDecimal water = list1.get(0).getWater();
               BigDecimal reduce = list1.parallelStream().filter(userWaterStatisticDto -> {
                   boolean b = userWaterStatisticDto.getMnWater() != null;
                   return b;
               }).map(UserWaterStatisticDto::getMnWater).reduce(BigDecimal.ZERO, BigDecimal::add);
               UserWaterStatisticDto userWaterStatisticDto = new UserWaterStatisticDto();
               userWaterStatisticDto.setMnWater(reduce);
               userWaterStatisticDto.setUserName(s);
               userWaterStatisticDto.setWater(water);
               userWaterStatisticDto.setOutNum(outNum);
               userWaterStatisticDto.setUpdateTime(list1.get(0).getUpdateTime() == null ? DateUtil.format(DateUtil.offsetDay(new Date(),-1),"yyyy-MM-dd" ) :list1.get(0).getUpdateTime() );
               res.add(userWaterStatisticDto);
           }

       }
       else {
           DateTime offset = DateUtil.offset(new Date(), DateField.YEAR, -1);
           yearStart = DateUtil.beginOfYear(offset);
           yearEnd = DateUtil.endOfYear(offset);
           startStr = DateUtil.format(yearStart, "yyyy-MM");
           endStr = DateUtil.format(yearEnd, "yyyy-MM");
           QueryWrapper wrapper2 = new QueryWrapper();
           wrapper2.ge("date",startStr);
           wrapper2.le("date",endStr);
           userWaterDtos = userWaterDao.selectList(wrapper2);
           if (CollUtil.isNotEmpty(userWaterDtos)){
               list = BeanUtil.copyToList(userWaterDtos, UserWaterStatisticDto.class);
               Map<String, List<UserWaterStatisticDto>> userNameMap = list.parallelStream().collect(Collectors.groupingBy(UserWaterStatisticDto::getUserName));
               for (String s : userNameMap.keySet()) {
                   List<UserWaterStatisticDto> list1 = userNameMap.get(s);
                   Integer outNum = list1.get(0).getOutNum();
                   BigDecimal water = list1.get(0).getWater();
                   BigDecimal reduce = list1.parallelStream().filter(userWaterStatisticDto -> {
                       boolean b = userWaterStatisticDto.getMnWater() != null;
                       return b;
                   }).map(UserWaterStatisticDto::getMnWater).reduce(BigDecimal.ZERO, BigDecimal::add);
                   UserWaterStatisticDto userWaterStatisticDto = new UserWaterStatisticDto();
                   userWaterStatisticDto.setMnWater(reduce);
                   userWaterStatisticDto.setUserName(s);
                   userWaterStatisticDto.setWater(water);
                   userWaterStatisticDto.setOutNum(outNum);
                   userWaterStatisticDto.setUpdateTime(list1.get(0).getUpdateTime() == null ? DateUtil.format(DateUtil.offsetDay(new Date(),-1),"yyyy-MM-dd" ) :list1.get(0).getUpdateTime() );
                   res.add(userWaterStatisticDto);
               }
           }
       }
        res = res.stream().sorted(Comparator.comparing(UserWaterStatisticDto::getWater).reversed()).collect(Collectors.toList());
       return res;
    }
    @Override
    public void inputExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"非居民")).sheet(0).headRowNumber(2).doRead();
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"居民生活")).sheet(1).headRowNumber(2).doRead();
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"农业生产者")).sheet(2).headRowNumber(2).doRead();
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"园林绿地")).sheet(3).headRowNumber(2).doRead();
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"地热井")).sheet(4).headRowNumber(2).doRead();
        EasyExcel.read(file.getInputStream(), UserWaterBaseInfoEsu.class, new UserWaterBaeInfoListener(userWaterBaseInfoDao,"水源热泵")).sheet(5).headRowNumber(2).doRead();
    }
}
