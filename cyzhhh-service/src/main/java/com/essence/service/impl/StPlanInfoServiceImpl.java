package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.PageUtil;
import com.essence.dao.StPlanInfoDao;
import com.essence.dao.entity.StPlanInfoDto;
import com.essence.interfaces.api.StPlanInfoService;
import com.essence.interfaces.model.StPlanInfoEsr;
import com.essence.interfaces.model.StPlanInfoEsrRes;
import com.essence.interfaces.model.StPlanInfoEsu;
import com.essence.interfaces.model.StPlanInfoEsuParam;
import com.essence.interfaces.param.StPlanInfoEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanInfoEtoT;
import com.essence.service.converter.ConverterStPlanInfoTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 闸坝计划排班信息表(StPlanInfo)业务层
 *
 * @author liwy
 * @since 2023-07-13 14:40:26
 */
@Service
public class StPlanInfoServiceImpl extends BaseApiImpl<StPlanInfoEsu, StPlanInfoEsp, StPlanInfoEsr, StPlanInfoDto> implements StPlanInfoService {

    @Autowired
    private StPlanInfoDao stPlanInfoDao;
    @Autowired
    private ConverterStPlanInfoEtoT converterStPlanInfoEtoT;
    @Autowired
    private ConverterStPlanInfoTtoR converterStPlanInfoTtoR;

    public StPlanInfoServiceImpl(StPlanInfoDao stPlanInfoDao, ConverterStPlanInfoEtoT converterStPlanInfoEtoT, ConverterStPlanInfoTtoR converterStPlanInfoTtoR) {
        super(stPlanInfoDao, converterStPlanInfoEtoT, converterStPlanInfoTtoR);
    }

    /**
     * 查询维护计划列表
     *
     * @param param
     * @return
     */
    @Override
    public PageUtil<StPlanInfoEsrRes> getStPlanInfo(StPlanInfoEsuParam param) {
        int currentPage = param.getCurrentPage();
        int pageSize = param.getPageSize();
        String riverName = param.getRiverName(); //河道名称
        String sttp = param.getSttp(); //测站类型
        String stnm = param.getStnm();
        String name1 = param.getName();//测站名称
        if("".equals(riverName) || null == riverName){
            riverName = null;
        }
        if("".equals(stnm) || null == stnm){
            stnm = null;
        }

        List<StPlanInfoDto> list = stPlanInfoDao.getStPlanInfo(sttp, stnm, riverName, name1);
        List<StPlanInfoEsrRes> returnRes = BeanUtil.copyToList(list, StPlanInfoEsrRes.class);
        for(int i=0;i<returnRes.size();i++){
            StPlanInfoEsrRes stPlanInfoEsrRes = returnRes.get(i);
            Integer sideGateId = stPlanInfoEsrRes.getSideGateId();
            String stnm2 = stPlanInfoEsrRes.getStnm();
            String company = stPlanInfoEsrRes.getCompany();
            String name = stPlanInfoEsrRes.getName();
            //根据条件查询所有相关的设备设施名称和日常维护内容
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sttp",sttp);
            queryWrapper.eq("side_gate_id",sideGateId);
            if (StrUtil.isNotEmpty(stnm)) {
                queryWrapper.like("stnm", stnm);
            }

            queryWrapper.eq("company",company);
            if (StrUtil.isNotEmpty(name1)) {
                queryWrapper.like("name", name1);
            }

            List<StPlanInfoDto> list2 = stPlanInfoDao.selectList(queryWrapper);
            if (CollUtil.isNotEmpty(list2)) {
                returnRes.get(i).setStPlanInfoEsrlist(list2);
            }

        }
        //进行分页
        PageUtil<StPlanInfoEsrRes> pageUtil = new PageUtil(returnRes, currentPage, pageSize);
        return pageUtil;
    }
}
