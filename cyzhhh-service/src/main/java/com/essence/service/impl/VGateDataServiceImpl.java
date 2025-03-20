package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.PageUtil;
import com.essence.dao.VGateDataDao;
import com.essence.dao.entity.StGatedamReportDto;
import com.essence.dao.entity.VGateDataDto;
import com.essence.interfaces.api.VGateDataService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.VGateDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterVGateDataEtoT;
import com.essence.service.converter.ConverterVGateDataTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (VGateData)业务层
 * @author majunjie
 * @since 2023-04-20 17:35:41
 */
@Service
public class VGateDataServiceImpl extends BaseApiImpl<VGateDataEsu, VGateDataEsp, VGateDataEsr, VGateDataDto> implements VGateDataService {

    @Autowired
    private VGateDataDao vGateDataDao;
    @Autowired
    private ConverterVGateDataEtoT converterVGateDataEtoT;
    @Autowired
    private ConverterVGateDataTtoR converterVGateDataTtoR;

    public VGateDataServiceImpl(VGateDataDao vGateDataDao, ConverterVGateDataEtoT converterVGateDataEtoT, ConverterVGateDataTtoR converterVGateDataTtoR) {
        super(vGateDataDao, converterVGateDataEtoT, converterVGateDataTtoR);
    }

    /**
     * 闸坝运行工况
     * @param stSideGateEsuParamv
     * @return
     */
    @Override
    public Object selectvGateDataList2(StSideGateEsuParamv stSideGateEsuParamv) {
        PaginatorParam paginatorParam = stSideGateEsuParamv.getPaginatorParam();
        int currentPage = paginatorParam.getCurrentPage();
        int pageSize = paginatorParam.getPageSize();
        String stnm = stSideGateEsuParamv.getStnm();
        String ctime = stSideGateEsuParamv.getCtime();//采集时间
        List<VGateDataDto> list = vGateDataDao.selectvGateDataList2(stnm,ctime);
        //
        if(!"".equals(stnm) && stnm !=null){
            list = list.stream().filter(p -> p.getStnm().contains(stnm)).collect(Collectors.toList());
        }
        //手动进行分页
        PageUtil<VGateDataDto> pageUtil = new PageUtil(list, currentPage, pageSize, null, null);
        return pageUtil;
    }

    /**
     *  获取闸坝运行工况根据 stcd
     * @param stcd
     * @return
     */
    @Override
    public Object selectvGateData(String stcd) {

        //根据 stcd 获取到对应的sn
        String sn = vGateDataDao.selectSn(stcd);

        //获取最新数据时间
        String ctime =vGateDataDao.selectCtime(sn);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("did", sn);
        queryWrapper.eq("ctime", ctime);
        List<VGateDataDto> list = vGateDataDao.selectList(queryWrapper);
        VGateDataDto vGateDataDtoRes = new VGateDataDto();
        for (int i = 0; i < list.size(); i++) {
            VGateDataDto vGateDataDto = list.get(i);
            String addr = vGateDataDto.getAddr();
            vGateDataDtoRes.setCtime(ctime);
            // VD4,b.VD8,c.VD12,d.VD16,e.VD20,f.VD24,g.VD212,h.VD200,i.M00 j.M01,k.M02
            if("VD4".equals(addr)){
                vGateDataDtoRes.setVD4(vGateDataDto.getAddrv());
            }
            if("VD8".equals(addr)){
                vGateDataDtoRes.setVD8(vGateDataDto.getAddrv());
            }
            if("VD12".equals(addr)){
                vGateDataDtoRes.setVD12(vGateDataDto.getAddrv());
            }
            if("VD16".equals(addr)){
                vGateDataDtoRes.setVD16(vGateDataDto.getAddrv());
            }
            if("VD20".equals(addr)){
                vGateDataDtoRes.setVD20(vGateDataDto.getAddrv());
            }
            if("VD24".equals(addr)){
                vGateDataDtoRes.setVD24(vGateDataDto.getAddrv());
            }
            if("VD212".equals(addr)){
                vGateDataDtoRes.setVD212(vGateDataDto.getAddrv());
            }
            if("VD200".equals(addr)){
                vGateDataDtoRes.setVD200(vGateDataDto.getAddrv());
            }
            if("VD220".equals(addr)){
                vGateDataDtoRes.setVD220(vGateDataDto.getAddrv());
            }
            if("M0.0".equals(addr)){
                vGateDataDtoRes.setM00(vGateDataDto.getAddrv());
            }
            if("M0.1".equals(addr)){
                vGateDataDtoRes.setM01(vGateDataDto.getAddrv());
            }
            if("M0.2".equals(addr)){
                vGateDataDtoRes.setM02(vGateDataDto.getAddrv());
            }
        }

        return vGateDataDtoRes;
    }
}
