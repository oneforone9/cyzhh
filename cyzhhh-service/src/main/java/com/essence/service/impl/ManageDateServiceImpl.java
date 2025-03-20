package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.AnCaseTypeDto;
import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.common.dto.YearCountStatisticDto;
import com.essence.common.utils.ResponseResult;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.ManageDateService;
import com.essence.interfaces.api.StWellcollectFeeService;
import com.essence.interfaces.model.StWellcollectFeeEsuVo;
import com.essence.interfaces.model.TRewardDealEsr;
import com.essence.interfaces.model.TRewardDealEsu;
import com.essence.interfaces.param.TRewardDealEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTRewardDealEtoT;
import com.essence.service.converter.ConverterTRewardDealTtoR;
import com.essence.service.impl.listener.ManageDateServiceListener;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManageDateServiceImpl extends BaseApiImpl<TRewardDealEsu, TRewardDealEsp, TRewardDealEsr, RewardDealEntity> implements ManageDateService {
    @Autowired
    private StWellcollectFeeService stWellcollectFeeService;
    @Resource
    private RewardDealDao rewardDealDao;
    /**
     * 区用水
     */
    @Resource
    private UserWaterDao userWaterDao;
    @Resource
    private StWellcollectFeeDao stWellcollectFeeDao;
    @Resource
    private UseWaterDao useWaterDao;
    @Resource
    private CyCaseBaseDao cyCaseBaseDao;
    @Autowired
    private ConverterTRewardDealEtoT converterTRewardDealEtoT;
    @Autowired
    private ConverterTRewardDealTtoR converterTRewardDealTtoR;

    public ManageDateServiceImpl(RewardDealDao tRewardDealDao, ConverterTRewardDealEtoT converterTRewardDealEtoT, ConverterTRewardDealTtoR converterTRewardDealTtoR) {
        super(tRewardDealDao, converterTRewardDealEtoT, converterTRewardDealTtoR);
    }

    @Override
    public void upload(InputStream inputStream) {
        EasyExcel.read(inputStream, RewardDealEntity.class, new ManageDateServiceListener(rewardDealDao)).sheet().doRead();
    }

    @Override
    public List<AnCaseTypeDto> getStatistic(String strStart, String strEnd) {
        List<AnCaseTypeDto> list = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("call_time",strStart);
        wrapper.le("call_time",strEnd);
        List<RewardDealEntity> rewardDealEntities = rewardDealDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(rewardDealEntities)){
            int total = rewardDealEntities.size();
            Map<String, List<RewardDealEntity>> map = rewardDealEntities.parallelStream().collect(Collectors.groupingBy(RewardDealEntity::getBigKind));
            for (String bigKind : map.keySet()) {
                AnCaseTypeDto anCaseTypeDto = new AnCaseTypeDto();
                List<RewardDealEntity> rewardDealEntities1 = map.get(bigKind);
                int kindSize = rewardDealEntities1.size();
                BigDecimal percent = new BigDecimal(kindSize).divide(new BigDecimal(total),4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
                anCaseTypeDto.setCaseType(bigKind);
                anCaseTypeDto.setPercent(percent);
                anCaseTypeDto.setNum(new BigDecimal(kindSize) );
                list.add(anCaseTypeDto);
            }

        }else {
            DateTime offset = DateUtil.offset(new Date(), DateField.YEAR, -1);

            DateTime start = DateUtil.beginOfYear(offset);
            DateTime end = DateUtil.endOfYear(offset);
            String formatStart = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
            String formatEnd = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
            //如果没有数据 则 查询2022 年
            QueryWrapper wrapperPre = new QueryWrapper();
            wrapperPre.le("call_time",formatEnd);
            wrapperPre.ge("call_time",formatStart);
            List<RewardDealEntity> rewardDealEntitiesPre = rewardDealDao.selectList(wrapperPre);
            if (CollUtil.isNotEmpty(rewardDealEntitiesPre)){
                int total = rewardDealEntitiesPre.size();
                Map<String, List<RewardDealEntity>> map = rewardDealEntitiesPre.parallelStream().collect(Collectors.groupingBy(RewardDealEntity::getBigKind));
                for (String bigKind : map.keySet()) {
                    AnCaseTypeDto anCaseTypeDto = new AnCaseTypeDto();
                    List<RewardDealEntity> rewardDealEntities1 = map.get(bigKind);
                    int kindSize = rewardDealEntities1.size();
                    BigDecimal percent = new BigDecimal(kindSize).divide(new BigDecimal(total),4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
                    anCaseTypeDto.setCaseType(bigKind);
                    anCaseTypeDto.setPercent(percent);
                    anCaseTypeDto.setNum(new BigDecimal(kindSize) );
                    list.add(anCaseTypeDto);
                }
            }

        }
        return list;
    }


    public  List<RewardDealEntity> getList(String strStart, String strEnd) {

        DateTime offset = DateUtil.offset(new Date(), DateField.MONTH, -1);
        List<AnCaseTypeDto> list = new ArrayList<>();
        DateTime start = DateUtil.beginOfYear(offset);
        DateTime end = DateUtil.endOfYear(offset);
        String formatStart = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
        String formatEnd = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(strStart) && StrUtil.isNotEmpty(strEnd)){
            formatStart = strStart;
            formatEnd = strEnd;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("call_time",formatEnd);
        wrapper.ge("call_time",formatStart);
        List<RewardDealEntity> rewardDealEntities = rewardDealDao.selectList(wrapper);

        return rewardDealEntities;
    }

    @Override
    public YearCountStatisticDto YearCountStatisticDto(String strStart, String strEnd) {
        YearCountStatisticDto res = new YearCountStatisticDto();
        DateTime start = DateUtil.beginOfYear(new Date());
        DateTime end = DateUtil.endOfYear(new Date());
        String formatStart = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
        String formatEnd = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(strStart) && StrUtil.isNotEmpty(strEnd)){
            formatStart = strStart;
            formatEnd = strEnd;
        }
        //接诉 即办
        List<RewardDealEntity> list = getList(formatStart, formatEnd);
        res.setAnswerDeal(list.size());


        //区用水 当年的 月份 数据
        QueryWrapper wrapper = new QueryWrapper();
        String year = TimeUtil.getYear(new Date());
        wrapper.like("date", year );
        List<UseWaterDto> useWaterDtos = useWaterDao.selectList(wrapper);
        BigDecimal useInfo = new BigDecimal(0);
        if (CollUtil.isNotEmpty(useWaterDtos)){
            for (UseWaterDto useWaterDto : useWaterDtos) {
                useInfo =useInfo.add(StrUtil.isNotEmpty(useWaterDto.getUseInfo()) ? new BigDecimal(useWaterDto.getUseInfo()) : BigDecimal.ZERO);
            }
        }
        res.setWater(useInfo);
        // 用水户

        res.setUserWater(getStatistic());
        //机井年收费
        StWellcollectFeeEsuVo stWellcollectFeeEsuVo = new StWellcollectFeeEsuVo();
        List<StWellcollectFeeDto> stWellcollectFeeDtos = stWellcollectFeeDao.selectList(new QueryWrapper<StWellcollectFeeDto>().lambda().eq(StWellcollectFeeDto::getTjTime, "2022"));
        if (null != stWellcollectFeeDtos && stWellcollectFeeDtos.size() > 0) {
            List<StWellcollectFeeDto> xSumList = Optional.ofNullable(stWellcollectFeeDtos.stream().filter(x -> x.getType().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            DecimalFormat decimalFormats = new DecimalFormat("0.0000");
            double xSumPayment = 0.00;
            double shPaymentSum = 0.00;
            if (null != xSumList && xSumList.size() > 0) {
                double xgPaymentSum = xSumList.stream().mapToDouble(StWellcollectFeeDto::getXzjjSj).sum();
                double xgShPaymentSum = xSumList.stream().mapToDouble(StWellcollectFeeDto::getShdwjjSj).sum();
                stWellcollectFeeEsuVo.setXgPayment(decimalFormats.format(xgPaymentSum));
                stWellcollectFeeEsuVo.setXgShPayment(decimalFormats.format(xgShPaymentSum));
                xSumPayment = xgPaymentSum + xgShPaymentSum;
                stWellcollectFeeEsuVo.setXSumPayment(decimalFormats.format(xSumPayment));
            }
            List<StWellcollectFeeDto> shSumList = Optional.ofNullable(stWellcollectFeeDtos.stream().filter(x -> x.getType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (null != shSumList && shSumList.size() > 0) {
                shPaymentSum = shSumList.stream().mapToDouble(StWellcollectFeeDto::getShdwjjSj).sum();
                stWellcollectFeeEsuVo.setShPayment(decimalFormats.format(shPaymentSum));
                stWellcollectFeeEsuVo.setJSumPayment(decimalFormats.format(shPaymentSum));
            }
            double sum = xSumPayment + shPaymentSum;
            stWellcollectFeeEsuVo.setSumPayment(decimalFormats.format(sum));

        }
//        ResponseResult responseResult = stWellcollectFeeService.selectStWellcollectFee("2022");
//        QueryWrapper wellWrapper = new QueryWrapper();
//        wellWrapper.like("tj_time","2022");
//        List<StWellcollectFeeDto> stWellcollectFeeDtos = stWellcollectFeeDao.selectList(wellWrapper);
//        BigDecimal wellCharge = new BigDecimal(0);
//        BigDecimal townCharge = new BigDecimal(0);
//        if (CollUtil.isNotEmpty(stWellcollectFeeDtos)){
//            for (StWellcollectFeeDto stWellcollectFeeDto : stWellcollectFeeDtos) {
//                wellCharge = wellCharge.add(stWellcollectFeeDto.getXzjjSj() == null ? BigDecimal.ZERO : new BigDecimal(stWellcollectFeeDto.getXzjjSj())) ;
//                townCharge = wellCharge.add(stWellcollectFeeDto.getShdwjjSj() == null ? BigDecimal.ZERO : new BigDecimal(stWellcollectFeeDto.getShdwjjSj())) ;
//            }
//        }

        res.setMechain(new BigDecimal(stWellcollectFeeEsuVo.getSumPayment()) );
        //  行政处罚 案件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.le("closing_date",end);
        queryWrapper.ge("closing_date",start);
        List<CyCaseBase> caseBases = cyCaseBaseDao.selectList(queryWrapper);
        if (CollUtil.isEmpty(caseBases)){
            //往上推一年
            DateTime ed = DateUtil.offset(end, DateField.YEAR, -1);
            DateTime str = DateUtil.offset(start, DateField.YEAR, -1);
            QueryWrapper queryWrapperPre = new QueryWrapper();
            queryWrapperPre.le("closing_date",ed);
            queryWrapperPre.ge("closing_date",str);
            caseBases = cyCaseBaseDao.selectList(queryWrapperPre);
        }
        res.setPunish(caseBases.size());
        return res;
    }


    public BigDecimal getStatistic() {
        List<UserWaterStatisticDto> res = new ArrayList<>();
        List<UserWaterStatisticDto> list = new ArrayList<>();
//        DateTime last  = DateUtil.offset(new Date(), DateField.YEAR, -1);
        DateTime yearStart = DateUtil.beginOfYear(new Date());
        DateTime yearEnd = DateUtil.endOfYear(new Date());


        String startStr = DateUtil.format(yearStart, "yyyy-MM-dd");
        String endStr = DateUtil.format(yearEnd, "yyyy-MM-dd");

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("date",startStr);
        wrapper.le("date",endStr);
        List<UserWaterDto> userWaterDtos = userWaterDao.selectList(wrapper);
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
            startStr = DateUtil.format(yearStart, "yyyy-MM-dd");
            endStr = DateUtil.format(yearEnd, "yyyy-MM-dd");
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
        if (CollUtil.isNotEmpty(res)){
            BigDecimal value = res.parallelStream().map(UserWaterStatisticDto::getMnWater).reduce(BigDecimal.ZERO, BigDecimal::add);
            return value;
        }
        return null;
    }
}
