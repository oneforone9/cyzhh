package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.UuidUtil;
import com.essence.dao.EventCompanyDao;
import com.essence.dao.StPlanInfoDao;
import com.essence.dao.StPlanPersonDao;
import com.essence.dao.entity.EventCompanyDto;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.euauth.entity.PubUserRoleDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.UserSyncFeign;
import com.essence.interfaces.api.EventCompanyService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.EventCompanyEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterEventCompanyEtoT;
import com.essence.service.converter.ConverterEventCompanyTtoR;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第三方服务公司人员配置(TEventCompany)业务层
 *
 * @author majunjie
 * @since 2023-06-05 11:24:43
 */
@Service
public class EventCompanyServiceImpl extends BaseApiImpl<EventCompanyEsu, EventCompanyEsp, EventCompanyEsr, EventCompanyDto> implements EventCompanyService {

    @Autowired
    private EventCompanyDao eventCompanyDao;
    @Autowired
    private ConverterEventCompanyEtoT converterEventCompanyEtoT;
    @Autowired
    private ConverterEventCompanyTtoR converterEventCompanyTtoR;
    @Autowired
    private UserSyncFeign userSyncFeign;
    @Autowired
    private StPlanPersonDao stPlanPersonDao;
    @Autowired
    private StPlanInfoDao stPlanInfoDao;

    public EventCompanyServiceImpl(EventCompanyDao tEventCompanyDao, ConverterEventCompanyEtoT converterTEventCompanyEtoT, ConverterEventCompanyTtoR converterTEventCompanyTtoR) {
        super(tEventCompanyDao, converterTEventCompanyEtoT, converterTEventCompanyTtoR);
    }

    @Override
    public String addTCompanyBase(EventCompanySave eventCompanySave) {
        //1.去获取公司得主键id
        String companyId = eventCompanyDao.selectEventCompanyId(eventCompanySave.getCompany());
        if (StringUtils.isNotBlank(companyId)) {
            eventCompanySave.setCompanyId(companyId);
        } else {
            eventCompanySave.setCompanyId(UuidUtil.get32UUIDStr());
        }
        //2.获取用户id
        ValidResponse validResponse = checkPhone(eventCompanySave.getPhone());
        if (validResponse.getResult() != null) {
            eventCompanySave.setUserId(validResponse.getResult().toString());
        } else {
            String baseId = UUID.randomUUID().toString().replace("-", "");
            // 内部人员同步到euauth
            // 1 添加用户
            PubUserSync pubUserSync = new PubUserSync();
            pubUserSync.setUserId(baseId);
            pubUserSync.setUserName(eventCompanySave.getName());
            pubUserSync.setLoginName(eventCompanySave.getPhone());
            pubUserSync.setPassword(ItemConstant.USER_PASSSWORD_DEFAUTL);
            Integer isLocked = 0;
            pubUserSync.setIsLocked(isLocked);
            pubUserSync.setMobilephone(eventCompanySave.getPhone());
            pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);
            pubUserSync.setCreateTime(new Date());
            userSyncFeign.addSync(pubUserSync);
            // 2 添加用户与角色关系
            PubUserRoleDTO pubUserRoleDTO = new PubUserRoleDTO();
            pubUserRoleDTO.setUserId(baseId);
            pubUserRoleDTO.setRoleIds(new String[]{ItemConstant.USER_ROLE_DEFAUTL});
            userSyncFeign.addPubUserRoleKeyList(pubUserRoleDTO);
            eventCompanySave.setUserId(baseId);
        }
        EventCompanyDto eventCompanyDto = new EventCompanyDto();
        BeanUtils.copyProperties(eventCompanySave, eventCompanyDto);
        //河段id
        List<String> riverIdList = eventCompanySave.getRiverIdList();
        List<String> rvnmList = eventCompanySave.getRvnmList();
        for (int i = 0; i < riverIdList.size(); i++) {
            eventCompanyDto.setRiverId(riverIdList.get(i));
            eventCompanyDto.setRvnm(rvnmList.get(i));
            eventCompanyDto.setGmtCreate(new Date());
            eventCompanyDto.setId(UuidUtil.get32UUIDStr());
            eventCompanyDao.insert(eventCompanyDto);
        }
        return "成功";
    }

    /**
     * 检查手机号是否存在
     *
     * @param telephone
     * @return true 存在 false 不存在
     */
    private ValidResponse checkPhone(String telephone) {
        // 查询euauth是否有账号为此手机号
        ValidResponse validResponse = userSyncFeign.loginNameCheckNew(telephone);
        return validResponse;
    }

    @Override
    public String deleteTCompanyBase(EventCompanyList eventCompanyList) {
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, eventCompanyList.getServiceYear());
        queryWrapper.lambda().eq(EventCompanyDto::getName, eventCompanyList.getName());
        queryWrapper.lambda().eq(EventCompanyDto::getUnitId, eventCompanyList.getUnitId());
        eventCompanyDao.delete(queryWrapper);
        return "成功！";
    }

    @Override
    public String updateTCompanyBase(EventCompanySave eventCompanySave) {
        String companyIdPre = eventCompanySave.getCompanyId(); //修改之前公司的id
        String userId = eventCompanySave.getUserId(); //修改之前负责人的id
        //1.去获取公司得主键id
        String companyId = eventCompanyDao.selectEventCompanyId(eventCompanySave.getCompany());
        String uuidStr = UuidUtil.get32UUIDStr(); //公司id
        if (StringUtils.isNotBlank(companyId)) {
            eventCompanySave.setCompanyId(companyId);
        } else {
            //eventCompanySave.setCompanyId(uuidStr);
            eventCompanySave.setCompanyId(companyIdPre);
        }
        //20230817
        //2.获取用户id
        ValidResponse validResponse = checkPhone(eventCompanySave.getPhone());
        if (validResponse.getResult() != null) {
            eventCompanySave.setUserId(validResponse.getResult().toString());

        }else{
            //String baseId = UUID.randomUUID().toString().replace("-", "");

            //2.修改用户昵称
            PubUserSync pubUserSync = new PubUserSync();
            boolean isupdate = false;
            pubUserSync.setUserId(eventCompanySave.getUserId());
            Integer isLocked = 0;
            pubUserSync.setIsLocked(isLocked);

            if (StringUtils.isNotBlank(eventCompanySave.getName())) {
                pubUserSync.setUserName(eventCompanySave.getName());
                isupdate = true;
            }
            if (StringUtils.isNotBlank(eventCompanySave.getPhone())) {
                pubUserSync.setLoginName(eventCompanySave.getPhone());
                pubUserSync.setMobilephone(eventCompanySave.getPhone());
                isupdate = true;
            }
            if (StringUtils.isNotBlank(eventCompanySave.getUnitId())) {
                pubUserSync.setCorpId(eventCompanySave.getUnitId());
                isupdate = true;
            }
            if (isupdate) {
                userSyncFeign.updateSync(pubUserSync);
            }
        }
        //20230817


        //再数据进行保存
        EventCompanyDto eventCompanyDto = new EventCompanyDto();
        BeanUtils.copyProperties(eventCompanySave, eventCompanyDto);
        //河段id
        List<String> riverIdList = eventCompanySave.getRiverIdList();
        List<String> rvnmList = eventCompanySave.getRvnmList();
        for (int i = 0; i < riverIdList.size(); i++) {
            eventCompanyDto.setRiverId(riverIdList.get(i));
            eventCompanyDto.setRvnm(rvnmList.get(i));
            eventCompanyDto.setGmtCreate(new Date());
            eventCompanyDto.setId(UuidUtil.get32UUIDStr());
            eventCompanyDao.insert(eventCompanyDto);
        }
        //同步修改养护人员表中该三方公司和三方负责人信息
        Integer res = stPlanPersonDao.updateStPlanPersonByCondition(eventCompanySave.getCompanyId(),eventCompanySave.getCompany(),eventCompanySave.getUnitId(),
                eventCompanySave.getUserId(),eventCompanySave.getName(),eventCompanySave.getPhone(),userId);
        //同步修改养护人员表中该三方公司和三方负责人信息

        //同步修改闸坝养护计划中三方公司的负责人信息
        Integer res2 = stPlanInfoDao.updateStPlanInfoByConditionb(eventCompanySave.getCompanyId(),eventCompanySave.getCompany(),eventCompanySave.getUnitId(),
                eventCompanySave.getUserId(),eventCompanySave.getName(),eventCompanySave.getPhone(),userId);
        //同步修改闸坝养护计划中三方公司的负责人信息

        return "成功";
    }


    @Override
    public PageUtil<EventCompanyList> selectTCompanyBaseList(EventCompanySelect eventCompanySelect) {
        List<EventCompanyList> eventCompanyLists = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(eventCompanySelect.getCompany())) {
            queryWrapper.lambda().like(EventCompanyDto::getCompany, eventCompanySelect.getCompany());
        }
        if (StringUtils.isNotBlank(eventCompanySelect.getServiceYear())) {
            queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, eventCompanySelect.getServiceYear());
        }
        if (StringUtils.isNotBlank(eventCompanySelect.getUnitName())) {
            queryWrapper.lambda().like(EventCompanyDto::getUnitName, eventCompanySelect.getUnitName());
        }
        List<EventCompanyDto> eventCompanyDtos = eventCompanyDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            //1.服务类型
            Map<Integer, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getType));
            for (Map.Entry<Integer, List<EventCompanyDto>> entry : map.entrySet()) {
                List<EventCompanyDto> list = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(list)) {
                    //20230817增加按管理单位分组
                    //2.2 所属单位
                    Map<String, List<EventCompanyDto>> unitIdList = list.stream().collect(Collectors.groupingBy(EventCompanyDto::getUnitId));
                    for (Map.Entry<String, List<EventCompanyDto>> mapunitId : unitIdList.entrySet()) {
                        List<EventCompanyDto> AList = Optional.ofNullable(mapunitId.getValue()).orElse(Lists.newArrayList());
                    //2.人名
                    Map<String, List<EventCompanyDto>> nameList = AList.stream().collect(Collectors.groupingBy(EventCompanyDto::getName));
                    //Map<String, List<EventCompanyDto>> nameList = list.stream().collect(Collectors.groupingBy(EventCompanyDto::getName));
                    for (Map.Entry<String, List<EventCompanyDto>> mapName : nameList.entrySet()) {

                            //3.年代
                            List<EventCompanyDto> yearList = Optional.ofNullable(mapName.getValue()).orElse(Lists.newArrayList());
                            if (!CollectionUtils.isEmpty(yearList)) {
                                //4.公司
                                Map<String, List<EventCompanyDto>> companyList = yearList.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
                                for (Map.Entry<String, List<EventCompanyDto>> mapCompany : companyList.entrySet()) {
                                    EventCompanyList eventCompanyList = new EventCompanyList();
                                    eventCompanyList.setCompany(mapCompany.getKey());
                                    List<EventCompanyDto> value = Optional.ofNullable(mapCompany.getValue()).orElse(Lists.newArrayList());
                                    EventCompanyDto eventCompanyDto = value.get(0);
                                    eventCompanyList.setServiceYear(eventCompanyDto.getServiceYear());
                                    if (eventCompanySelect.getType() != null) {
                                        // 进行对服务类型进行过滤 不为空 如果不等于 这个类型的话 就跳过这个循环
                                        if (!eventCompanySelect.getType().equals(eventCompanyDto.getType())) {
                                            continue;
                                        }
                                    }
                                    eventCompanyList.setType(eventCompanyDto.getType());
                                    eventCompanyList.setCompanyId(eventCompanyDto.getCompanyId());
                                    //河道
                                    List<String> river = new ArrayList<>();
                                    List<String> rvnm = new ArrayList<>();
                                    for (EventCompanyDto event : value) {
                                        river.add(event.getRiverId());
                                        rvnm.add(event.getRvnm());
                                    }
                                    eventCompanyList.setRiverIdList(river);
                                    eventCompanyList.setRvnmList(rvnm);
                                    eventCompanyList.setUnitId(eventCompanyDto.getUnitId());
                                    eventCompanyList.setUnitName(eventCompanyDto.getUnitName());
                                    eventCompanyList.setName(eventCompanyDto.getName());
                                    eventCompanyList.setUserId(eventCompanyDto.getUserId());
                                    eventCompanyList.setPhone(eventCompanyDto.getPhone());
                                    eventCompanyLists.add(eventCompanyList);
                                }
                            }
                        }
                    }
                }
            }
        }
        PageUtil<EventCompanyList> pageUtil = new PageUtil(eventCompanyLists, eventCompanySelect.getCurrent(), eventCompanySelect.getSize());
        return pageUtil;
    }

    @Override
    public Map<String, List<EventCompanyPerson>> selectTCompanyByRiverId(EventCompanyQuery eventCompanyQuery) {
        Map<String, List<EventCompanyPerson>> map = new HashMap<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(eventCompanyQuery.getRiverId())) {
            queryWrapper.lambda().eq(EventCompanyDto::getRiverId, eventCompanyQuery.getRiverId());
        }
        if (null != eventCompanyQuery.getType()) {
            queryWrapper.lambda().eq(EventCompanyDto::getType, eventCompanyQuery.getType());
        }
        if (StringUtils.isNotBlank(eventCompanyQuery.getUnitId())) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, eventCompanyQuery.getUnitId());
        }
        Integer year = DateUtil.year(new Date());
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());
        List<EventCompanyDto> eventCompanyDtos = eventCompanyDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map1 = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> mapCompany : map1.entrySet()) {
                List<EventCompanyDto> value = Optional.ofNullable(mapCompany.getValue()).orElse(Lists.newArrayList());
                List<EventCompanyPerson> eventCompanyPersonList = Optional.ofNullable(value.stream().map(x -> {
                    EventCompanyPerson eventCompanyPerson = new EventCompanyPerson();
                    eventCompanyPerson.setName(x.getName());
                    eventCompanyPerson.setPhone(x.getPhone());
                    eventCompanyPerson.setUserId(x.getUserId());
                    eventCompanyPerson.setCompanyId(x.getCompanyId());
                    return eventCompanyPerson;
                }).collect(Collectors.toList())).orElse(Lists.newArrayList());
                map.put(mapCompany.getKey(), eventCompanyPersonList);
            }
        }
        return map;
    }

    @Override
    public List<EventCompanyDto> selectEventCompany(String userId) {
        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(new QueryWrapper<EventCompanyDto>().lambda().eq(EventCompanyDto::getUserId, userId))).orElse(Lists.newArrayList());
        return eventCompanyDtos;
    }

    //管河成效-第三方服务公司处理工单统计-绿化保洁的
    @Override
    public List<StCompanyBaseRelationDto> searchCount(String unitId, String company) {
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtoList = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, unitId);
        }
        Integer year = DateUtil.year(new Date());
        queryWrapper.lambda().eq(EventCompanyDto::getType, 1);
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());
        if (StringUtils.isNotBlank(company)) {
            queryWrapper.lambda().like(EventCompanyDto::getCompany, company);
        }
        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> entry : map.entrySet()) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setCompany(entry.getKey());
                List<EventCompanyDto> value = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(value)) {
                    EventCompanyDto eventCompanyDto = value.get(0);
                    stCompanyBaseRelationDto.setStCompanyBaseId(eventCompanyDto.getCompanyId());
                    stCompanyBaseRelationDto.setType(eventCompanyDto.getType().toString());
                    stCompanyBaseRelationDto.setDataId(eventCompanyDto.getRiverId());
                    stCompanyBaseRelationDto.setGmtCreate(eventCompanyDto.getGmtCreate());
                    stCompanyBaseRelationDto.setDataName("河道绿化保洁");
                }
                stCompanyBaseRelationDtoList.add(stCompanyBaseRelationDto);
            }
        }
        return stCompanyBaseRelationDtoList;
    }

    @Override
    public Integer searchByStCompanyBaseId(String stCompanyBaseId, String orderType, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
    }

    @Override
    public Integer searchByStCompanyBaseId5(String stCompanyBaseId, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId5(stCompanyBaseId, start, end);
    }

    @Override
    public Integer searchByStCompanyBaseId6(String stCompanyBaseId, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId6(stCompanyBaseId, start, end);
    }

    //运行维保的
    @Override
    public List<StCompanyBaseRelationDto> searchCount2(String unitId, String company) {
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtoList = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, unitId);
        }
        Integer year = DateUtil.year(new Date());
        queryWrapper.lambda().eq(EventCompanyDto::getType, 2);
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());
        if (StringUtils.isNotBlank(company)) {
            queryWrapper.lambda().like(EventCompanyDto::getCompany, company);
        }
        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> entry : map.entrySet()) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setCompany(entry.getKey());
                List<EventCompanyDto> value = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(value)) {
                    EventCompanyDto eventCompanyDto = value.get(0);
                    stCompanyBaseRelationDto.setStCompanyBaseId(eventCompanyDto.getCompanyId());
                    stCompanyBaseRelationDto.setType(eventCompanyDto.getType().toString());
                    stCompanyBaseRelationDto.setDataId(eventCompanyDto.getRiverId());
                    stCompanyBaseRelationDto.setGmtCreate(eventCompanyDto.getGmtCreate());
                    stCompanyBaseRelationDto.setDataName("闸坝运行养护");
                }
                stCompanyBaseRelationDtoList.add(stCompanyBaseRelationDto);
            }
        }
        return stCompanyBaseRelationDtoList;
    }

    @Override
    public List<StCompanyBaseRelationDto> searchCount3(String unitId) {
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtoList = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, unitId);
        }
        Integer year = DateUtil.year(new Date());
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());

        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> entry : map.entrySet()) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setCompany(entry.getKey());
                List<EventCompanyDto> value = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(value)) {
                    EventCompanyDto eventCompanyDto = value.get(0);
                    stCompanyBaseRelationDto.setStCompanyBaseId(eventCompanyDto.getCompanyId());
                    stCompanyBaseRelationDto.setType(eventCompanyDto.getType().toString());
                    stCompanyBaseRelationDto.setDataId(eventCompanyDto.getRiverId());
                    stCompanyBaseRelationDto.setGmtCreate(eventCompanyDto.getGmtCreate());
                    stCompanyBaseRelationDto.setDataName(eventCompanyDto.getType().equals(1)?"河道绿化保洁":"闸坝运行养护");
                }
                stCompanyBaseRelationDtoList.add(stCompanyBaseRelationDto);
            }
        }
        return stCompanyBaseRelationDtoList;
    }

    @Override
    public List<StCompanyBaseRelationDto> selectCompany(String unitId, String flag,Date start) {
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtoList = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, unitId);
        }
        if (StringUtils.isNotBlank(flag)) {
            queryWrapper.lambda().eq(EventCompanyDto::getType, flag);
        }
        Integer year = DateUtil.year(start);
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());

        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> entry : map.entrySet()) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setCompany(entry.getKey());
                List<EventCompanyDto> value = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(value)) {
                    EventCompanyDto eventCompanyDto = value.get(0);
                    stCompanyBaseRelationDto.setStCompanyBaseId(eventCompanyDto.getCompanyId());
                    stCompanyBaseRelationDto.setType(eventCompanyDto.getType().toString());
                    stCompanyBaseRelationDto.setDataId(eventCompanyDto.getRiverId());
                    stCompanyBaseRelationDto.setGmtCreate(eventCompanyDto.getGmtCreate());
                    stCompanyBaseRelationDto.setDataName(eventCompanyDto.getType().equals(1)?"河道绿化保洁":"闸坝运行养护");
                }
                stCompanyBaseRelationDtoList.add(stCompanyBaseRelationDto);
            }
        }
        return stCompanyBaseRelationDtoList;
    }

    @Override
    public List<StCompanyBaseRelationDto> selectCompanys(String unitId, String company, String flag, Date start) {
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtoList = new ArrayList<>();
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(unitId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUnitId, unitId);
        }
        if (StringUtils.isNotBlank(flag)) {
            queryWrapper.lambda().eq(EventCompanyDto::getType, flag);
        }
        if (StringUtils.isNotBlank(company)) {
            queryWrapper.lambda().eq(EventCompanyDto::getCompany, company);
        }
        Integer year = DateUtil.year(start);
        queryWrapper.lambda().eq(EventCompanyDto::getServiceYear, year.toString());

        List<EventCompanyDto> eventCompanyDtos = Optional.ofNullable(eventCompanyDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventCompanyDtos)) {
            Map<String, List<EventCompanyDto>> map = eventCompanyDtos.stream().collect(Collectors.groupingBy(EventCompanyDto::getCompany));
            for (Map.Entry<String, List<EventCompanyDto>> entry : map.entrySet()) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setCompany(entry.getKey());
                List<EventCompanyDto> value = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(value)) {
                    EventCompanyDto eventCompanyDto = value.get(0);
                    stCompanyBaseRelationDto.setStCompanyBaseId(eventCompanyDto.getCompanyId());
                    stCompanyBaseRelationDto.setType(eventCompanyDto.getType().toString());
                    stCompanyBaseRelationDto.setDataId(eventCompanyDto.getRiverId());
                    stCompanyBaseRelationDto.setGmtCreate(eventCompanyDto.getGmtCreate());
                    stCompanyBaseRelationDto.setDataName(eventCompanyDto.getType().equals(1)?"河道绿化保洁":"闸坝运行养护");
                }
                stCompanyBaseRelationDtoList.add(stCompanyBaseRelationDto);
            }
        }
        return stCompanyBaseRelationDtoList;
    }

    @Override
    public Integer searchByStCompanyBaseId2(String stCompanyBaseId, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId2(stCompanyBaseId, start, end);
    }

    @Override
    public Integer searchByStCompanyBaseId3(String stCompanyBaseId, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId3(stCompanyBaseId, start, end);
    }

    @Override
    public Integer searchByStCompanyBaseId4(String orderType1, String orderType2, Date start, Date end) {
        return eventCompanyDao.searchByStCompanyBaseId4( orderType1, orderType2, start, end);
    }

    /**
     * 根据三方人员userId获取功能手机号等
     * @param userId
     * @return
     */
    @Override
    public Object selectTCompanyBase(String userId) {
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.lambda().eq(EventCompanyDto::getUserId, userId);
        }
        List<EventCompanyDto> eventCompanyDtos = eventCompanyDao.selectList(queryWrapper);
        return eventCompanyDtos;
    }

    /**
     * 获取第三方公司下拉列表
     * @param eventCompanyEsu
     * @return
     */
    @Override
    public List<EventCompanyRes> selectEventCompanyList(EventCompanyEsu eventCompanyEsu) {
        //服务类型（1-河道绿化保洁  2-闸坝运行养护）
        Integer type = eventCompanyEsu.getType();
        if("".equals(type) || null == type){
            type = null;
        }
        //所属单位主键
        String unitId = eventCompanyEsu.getUnitId();
        if("".equals(unitId) || null == unitId){
            unitId = null;
        }
        List<EventCompanyDto> list = eventCompanyDao.selectEventCompanyList(type,unitId);
        List<EventCompanyRes> a = BeanUtil.copyToList(list, EventCompanyRes.class);
        for (int i = 0; i < a.size(); i++) {
            String companyId = a.get(i).getCompanyId();
            Integer type1 = a.get(i).getType();
            String unitId1 = a.get(i).getUnitId();
            List<EventCompanyDto> eventCompanyRList = eventCompanyDao.selectUserBycompanyId(companyId, type1, unitId1);
            List<EventCompanyR> UserRes = BeanUtil.copyToList(eventCompanyRList, EventCompanyR.class);
            a.get(i).setCompanUserList(UserRes);
        }
        return a;
    }

    /**
     * 获取三方养护人员的手机号等
     * @param orderManager
     * @return
     */
    @Override
    public List<EventCompanyDto> selectManagerPhone(String orderManager) {
        QueryWrapper<EventCompanyDto> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(orderManager)) {
            queryWrapper.lambda().eq(EventCompanyDto::getName, orderManager);
        }
        List<EventCompanyDto> eventCompanyDtos = eventCompanyDao.selectList(queryWrapper);
        return eventCompanyDtos;
    }
}
