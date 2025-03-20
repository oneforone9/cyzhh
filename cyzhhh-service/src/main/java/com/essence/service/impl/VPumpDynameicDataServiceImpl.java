package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.PageUtils;
import com.essence.dao.StPumpDataDao;
import com.essence.dao.StSideGateDao;
import com.essence.dao.VPumpDynameicDataDao;
import com.essence.dao.entity.StPumpDataDto;
import com.essence.dao.entity.StSideGateDto;
import com.essence.dao.entity.VPumpDynameicDataDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.VPumpDynameicDataService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.VPumpDynameicDataEsr;
import com.essence.interfaces.model.VPumpDynameicDataEsu;
import com.essence.interfaces.param.VPumpDynameicDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterVPumpDynameicDataEtoT;
import com.essence.service.converter.ConverterVPumpDynameicDataTtoR;
//import org.geotools.geometry.jts.LiteShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (VPumpDynameicData)业务层
 * @author BINX
 * @since 2023-04-20 17:29:34
 */
@Service
public class VPumpDynameicDataServiceImpl extends BaseApiImpl<VPumpDynameicDataEsu, VPumpDynameicDataEsp, VPumpDynameicDataEsr, VPumpDynameicDataDto> implements VPumpDynameicDataService {
    @Resource
    private StSideGateDao stSideGateDao;
    @Resource
    private StPumpDataDao pumpDataDao;

    @Autowired
    private VPumpDynameicDataDao vPumpDynameicDataDao;
    @Autowired
    private ConverterVPumpDynameicDataEtoT converterVPumpDynameicDataEtoT;
    @Autowired
    private ConverterVPumpDynameicDataTtoR converterVPumpDynameicDataTtoR;

    public VPumpDynameicDataServiceImpl(VPumpDynameicDataDao vPumpDynameicDataDao, ConverterVPumpDynameicDataEtoT converterVPumpDynameicDataEtoT, ConverterVPumpDynameicDataTtoR converterVPumpDynameicDataTtoR) {
        super(vPumpDynameicDataDao, converterVPumpDynameicDataEtoT, converterVPumpDynameicDataTtoR);
    }

    @Override
    public Paginator<VPumpDynameicDataEsr> findByPaginator(PaginatorParam param) {
        List<VPumpDynameicDataEsr> res = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sttp","DP");
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper);
        Map<String, StSideGateDto> gateMap = stSideGateDtos.parallelStream().filter(stSideGateDto -> {
            return StrUtil.isNotEmpty(stSideGateDto.getStcd());
        }).collect(Collectors.toMap(StSideGateDto::getStcd, Function.identity(), (o1, o2) -> o2));
        Date start = DateUtil.beginOfDay(new Date());
        Date end = DateUtil.endOfDay(new Date());
        for (Criterion condition : param.getConditions()) {
            if (condition.getFieldName().contains("date")){
                start = DateUtil.beginOfDay(DateUtil.parse(condition.getValue().toString())) ;
                end = DateUtil.endOfDay(DateUtil.parse(condition.getValue().toString())) ;
            }
            if (condition.getFieldName().contains("stnm")){
                String stnm = condition.getValue().toString();
                gateMap = stSideGateDtos.parallelStream().filter(stSideGateDto -> {
                    return StrUtil.isNotEmpty(stSideGateDto.getStcd());
                }).filter(stSideGateDto -> {
                    return stSideGateDto.getStnm().contains(stnm);
                }).collect(Collectors.toMap(StSideGateDto::getStcd, Function.identity(), (o1, o2) -> o2));
            }
        }
        int currentPage = param.getCurrentPage();
        int pageSize = param.getPageSize();
        QueryWrapper dataQuery = new QueryWrapper();
        dataQuery.le("date",end);
        dataQuery.ge("date",start);
        dataQuery.in("device_addr",gateMap.keySet().parallelStream().collect(Collectors.toList()));
        dataQuery.orderByDesc("date");
        List<StPumpDataDto> stPumpDataDtos = pumpDataDao.selectList(dataQuery);
        for (StPumpDataDto stPumpDataDto : stPumpDataDtos) {
            StSideGateDto stSideGateDto = gateMap.get(stPumpDataDto.getDeviceAddr());
            VPumpDynameicDataEsr vPumpDynameicDataEsr = new VPumpDynameicDataEsr();
            BeanUtil.copyProperties(stPumpDataDto,vPumpDynameicDataEsr);
            BeanUtil.copyProperties(stSideGateDto,vPumpDynameicDataEsr);
            res.add(vPumpDynameicDataEsr);
        }
        if (pageSize == 0) {
            //给一个默认数值
            pageSize = 1;
            if (currentPage == 0) {
                currentPage = 1;
            }
        }
        //分页
        Paginator<VPumpDynameicDataEsr> paginator = new Paginator(res,pageSize,currentPage);
        return paginator;
    }
}
