package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.UuidUtil;
import com.essence.dao.ReaBaseDao;
import com.essence.dao.StForeseeProjectDao;
import com.essence.dao.StSideGateDao;
import com.essence.dao.entity.ReaBase;
import com.essence.dao.entity.StForeseeProjectDto;
import com.essence.dao.entity.StSideGateDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForeseeProjectService;
import com.essence.interfaces.model.StForeseeProjectEsr;
import com.essence.interfaces.model.StForeseeProjectEsu;
import com.essence.interfaces.model.StForeseeProjectSelect;
import com.essence.interfaces.param.StForeseeProjectEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStForeseeProjectEtoT;
import com.essence.service.converter.ConverterStForeseeProjectTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 预设调度方案(StForeseeProject)业务层
 *
 * @author majunjie
 * @since 2023-04-24 11:21:07
 */
@Service
public class StForeseeProjectServiceImpl extends BaseApiImpl<StForeseeProjectEsu, StForeseeProjectEsp, StForeseeProjectEsr, StForeseeProjectDto> implements StForeseeProjectService {

    @Autowired
    private StForeseeProjectDao stForeseeProjectDao;
    @Autowired
    private ConverterStForeseeProjectEtoT converterStForeseeProjectEtoT;
    @Autowired
    private ConverterStForeseeProjectTtoR converterStForeseeProjectTtoR;
    @Autowired
    private StSideGateDao stSideGateDao;
    @Autowired
    private ReaBaseDao reaBaseDao;
    public StForeseeProjectServiceImpl(StForeseeProjectDao stForeseeProjectDao, ConverterStForeseeProjectEtoT converterStForeseeProjectEtoT, ConverterStForeseeProjectTtoR converterStForeseeProjectTtoR) {
        super(stForeseeProjectDao, converterStForeseeProjectEtoT, converterStForeseeProjectTtoR);
    }

    @Override
    public List<StForeseeProjectEsr> selectStForeseeProject(StForeseeProjectSelect stForeseeProjectSelect) {
        List<StForeseeProjectEsr> stForeseeProjectEsrList=new ArrayList<>();
        List<StSideGateDto> stSideGateDtos = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, stForeseeProjectSelect.getSttp()).isNotNull(StSideGateDto::getSeriaName))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stSideGateDtos)) {
            List<ReaBase> reaBaseList = Optional.ofNullable(reaBaseDao.selectList(new QueryWrapper<ReaBase>().lambda().le(ReaBase::getId, 31))).orElse(Lists.newArrayList());

            stSideGateDtos = stSideGateDtos.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).collect(Collectors.toList());
            if (stForeseeProjectSelect.getSttp().equals("DD")){
                //水闸数据
                for (StSideGateDto stSideGateDto : stSideGateDtos) {
                    StForeseeProjectEsr stForeseeProjectEsr=new StForeseeProjectEsr();
                    stForeseeProjectEsr.setStcd(stSideGateDto.getStcd());
                    stForeseeProjectEsr.setSttp(stSideGateDto.getSttp());
                    stForeseeProjectEsr.setSectionName(stSideGateDto.getSectionName());
                    if (!CollectionUtils.isEmpty(reaBaseList)) {
                        if (null != stSideGateDto.getRiverId()) {
                            stForeseeProjectEsr.setRvid(stSideGateDto.getRiverId().toString());
                            List<ReaBase> reaBases = Optional.ofNullable(reaBaseList.stream().filter(x -> x.getId().equals(stSideGateDto.getRiverId().toString())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            if (!CollectionUtils.isEmpty(reaBases)) {
                                ReaBase reaBase = reaBases.get(0);
                                stForeseeProjectEsr.setRvnm(reaBase.getReaName());
                            }
                        }
                    }
                    stForeseeProjectEsr.setId(stSideGateDto.getId().toString());
                    stForeseeProjectEsr.setHolesG(stSideGateDto.getHolesG());
                    stForeseeProjectEsr.setHolesK(stSideGateDto.getHolesK());
                    stForeseeProjectEsr.setCaseId(stForeseeProjectSelect.getCaseId());
                    stForeseeProjectEsr.setHolesNumber(stSideGateDto.getHolesNumber());
                    stForeseeProjectEsr.setDHeigh(stSideGateDto.getHolesG());
                    stForeseeProjectEsr.setDWide(stSideGateDto.getHolesK());

                    stForeseeProjectEsr.setControlValue(stSideGateDto.getBDefault());
                    stForeseeProjectEsr.setJd(stSideGateDto.getLgtd());
                    stForeseeProjectEsr.setWd(stSideGateDto.getLttd());
                    stForeseeProjectEsr.setSeriaName(stSideGateDto.getSeriaName());
                    stForeseeProjectEsrList.add(stForeseeProjectEsr);
                }
            }else {
                //坝数据
                for (StSideGateDto stSideGateDto : stSideGateDtos) {
                    StForeseeProjectEsr stForeseeProjectEsr=new StForeseeProjectEsr();
                    stForeseeProjectEsr.setStcd(stSideGateDto.getStcd());
                    stForeseeProjectEsr.setSttp(stSideGateDto.getSttp());
                    stForeseeProjectEsr.setSectionName(stSideGateDto.getSectionName());
                    if (!CollectionUtils.isEmpty(reaBaseList)) {
                        if (null != stSideGateDto.getRiverId()) {
                            stForeseeProjectEsr.setRvid(stSideGateDto.getRiverId().toString());
                            List<ReaBase> reaBases = Optional.ofNullable(reaBaseList.stream().filter(x -> x.getId().equals(stSideGateDto.getRiverId().toString())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            if (!CollectionUtils.isEmpty(reaBases)) {
                                ReaBase reaBase = reaBases.get(0);
                                stForeseeProjectEsr.setRvnm(reaBase.getReaName());
                            }
                        }
                    }
                    stForeseeProjectEsr.setBDefault(stSideGateDto.getBDefault());
                    stForeseeProjectEsr.setId(stSideGateDto.getId().toString());
                    stForeseeProjectEsr.setBHigh(stSideGateDto.getBHigh());
                    stForeseeProjectEsr.setSeriaName(stSideGateDto.getSeriaName());
                    stForeseeProjectEsr.setJd(stSideGateDto.getLgtd());
                    stForeseeProjectEsr.setWd(stSideGateDto.getLttd());
                    stForeseeProjectEsr.setBLong(stSideGateDto.getBLong());
                    stForeseeProjectEsr.setBDefault(stSideGateDto.getBDefault());
                    stForeseeProjectEsr.setCaseId(stForeseeProjectSelect.getCaseId());
                    stForeseeProjectEsrList.add(stForeseeProjectEsr);
                }
            }

        }

        //将查询的数据保存数据库一份
        for (StForeseeProjectEsr stForeseeProjectEsr : stForeseeProjectEsrList) {
            stForeseeProjectEsr.setControlType(1);
            StForeseeProjectDto stForeseeProjectDto = new StForeseeProjectDto();
            BeanUtil.copyProperties(stForeseeProjectEsr,stForeseeProjectDto);
            stForeseeProjectDto.setId(UUID.randomUUID().toString().replace("-",""));
            stForeseeProjectDao.insert(stForeseeProjectDto);
        }
        return stForeseeProjectEsrList;
    }

    @Override
    public String saveStForeseeProject(List<StForeseeProjectEsr> stForeseeProjectEsrList) {
        if (!CollectionUtils.isEmpty(stForeseeProjectEsrList)) {
            // 4 组装班组
//            StForeseeProjectEsr stForeseeProjectEsr = stForeseeProjectEsrList.get(0);
//            stForeseeProjectDao.deleteStForeseeProject(stForeseeProjectEsr.getCaseId());
            List<StForeseeProjectDto> stForeseeProjectDtoList=stForeseeProjectEsrList.stream().map(x->{
                StForeseeProjectDto stForeseeProjectDto=new StForeseeProjectDto();
                BeanUtils.copyProperties(x, stForeseeProjectDto);
                stForeseeProjectDto.setId(UuidUtil.get32UUIDStr());
                return stForeseeProjectDto;
            }).collect(Collectors.toList());

            //通过caseid  seria_name 进行新增或者更新
            for (StForeseeProjectDto stForeseeProjectDto : stForeseeProjectDtoList) {
                String caseId = stForeseeProjectDto.getCaseId();
                String seriaName = stForeseeProjectDto.getSeriaName();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("case_id",caseId);
                wrapper.eq("seria_name",seriaName);
                List<StForeseeProjectDto> stForeseeProjectDtos = stForeseeProjectDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stForeseeProjectDtos)){
                    stForeseeProjectDao.update(stForeseeProjectDto,wrapper);
                }else {
                    stForeseeProjectDao.insert(stForeseeProjectDto);
                }
            }

//            stForeseeProjectDao.saveStForeseeProject(stForeseeProjectDtoList);
        }
        return "保存成功!";
    }
}
