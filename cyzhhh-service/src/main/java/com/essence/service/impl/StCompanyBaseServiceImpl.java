package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.EventCompanyDao;
import com.essence.dao.PersonBaseDao;
import com.essence.dao.StCompanyBaseDao;
import com.essence.dao.StCompanyBaseRelationDao;
import com.essence.dao.entity.*;
import com.essence.euauth.entity.PubUserRoleDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.UserSyncFeign;
import com.essence.interfaces.api.EventBaseService;
import com.essence.interfaces.api.EventCompanyService;
import com.essence.interfaces.api.StCompanyBaseService;
import com.essence.interfaces.api.WorkorderNewestService;
import com.essence.interfaces.dot.StCompanyBaseDtos;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StCompanyBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCompanyBaseEtoT;
import com.essence.service.converter.ConverterStCompanyBaseTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 第三方服务公司基础表(StCompanyBase)业务层
 *
 * @author BINX
 * @since 2023-02-16 11:57:39
 */
@Service
public class StCompanyBaseServiceImpl extends BaseApiImpl<StCompanyBaseEsu, StCompanyBaseEsp, StCompanyBaseEsr, StCompanyBaseDto> implements StCompanyBaseService {

    @Autowired
    private StCompanyBaseDao stCompanyBaseDao;
    @Autowired
    private StCompanyBaseRelationDao stCompanyBaseRelationDao;
    @Autowired
    private PersonBaseDao personBaseDao;
    @Autowired
    private UserSyncFeign userSyncFeign;
    @Autowired
    private ConverterStCompanyBaseEtoT converterStCompanyBaseEtoT;
    @Autowired
    private ConverterStCompanyBaseTtoR converterStCompanyBaseTtoR;

    @Autowired
    private StCompanyBaseService stCompanyBaseService;
    @Autowired
    private EventCompanyService eventCompanyService;
    @Autowired
    private EventCompanyDao eventCompanyDao;

    @Autowired
    private EventBaseService eventBaseService;
    @Autowired
    private WorkorderNewestService workorderNewestService;

    public StCompanyBaseServiceImpl(StCompanyBaseDao stCompanyBaseDao, ConverterStCompanyBaseEtoT converterStCompanyBaseEtoT, ConverterStCompanyBaseTtoR converterStCompanyBaseTtoR) {
        super(stCompanyBaseDao, converterStCompanyBaseEtoT, converterStCompanyBaseTtoR);
    }

    /**
     * 新增第三方服务公司基础表管理
     *
     * @param stCompanyBaseEsu
     * @return
     */
    @Transactional
    @Override
    public Object addStCompanyBase(StCompanyBaseEsu stCompanyBaseEsu) {
        //先增加基础表
        StCompanyBaseDto stCompanyBase = new StCompanyBaseDto();
        stCompanyBase.setCompany(stCompanyBaseEsu.getCompany());
        stCompanyBase.setManageName(stCompanyBaseEsu.getManageName());
        stCompanyBase.setManagePhone(stCompanyBaseEsu.getManagePhone());
        stCompanyBase.setUnitId(stCompanyBaseEsu.getUnitId());
        stCompanyBase.setUnitName(stCompanyBaseEsu.getUnitName());
        stCompanyBase.setGmtCreate(new Date());
        stCompanyBase.setRemark(stCompanyBaseEsu.getRemark());
        stCompanyBase.setServiceYear(stCompanyBaseEsu.getServiceYear());
        String baseId = UUID.randomUUID().toString().replace("-", "");
        stCompanyBase.setId(baseId);
        int a = stCompanyBaseDao.insert(stCompanyBase);
        //再增加关联表
        //服务类型
        List serviceType = stCompanyBaseEsu.getServiceType();
        if (serviceType != null && serviceType.size() > 0) {
            for (int i = 0; i < serviceType.size(); i++) {
                StCompanyBaseRelationEsu esu = (StCompanyBaseRelationEsu) serviceType.get(i);
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setDataId(esu.getDataId());
                stCompanyBaseRelationDto.setDataName(esu.getDataName());
                stCompanyBaseRelationDto.setStCompanyBaseId(baseId);
                stCompanyBaseRelationDto.setType(esu.getType());
                stCompanyBaseRelationDto.setGmtCreate(new Date());
                stCompanyBaseRelationDao.insert(stCompanyBaseRelationDto);
            }

        }
        //管护河段
        List manageRiver = stCompanyBaseEsu.getManageRiver();
        if (manageRiver != null && manageRiver.size() > 0) {
            for (int i = 0; i < manageRiver.size(); i++) {
                StCompanyBaseRelationEsu esu = (StCompanyBaseRelationEsu) manageRiver.get(i);
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setDataId(esu.getDataId());
                stCompanyBaseRelationDto.setDataName(esu.getDataName());
                stCompanyBaseRelationDto.setStCompanyBaseId(baseId);
                stCompanyBaseRelationDto.setType(esu.getType());
                stCompanyBaseRelationDto.setGmtCreate(new Date());
                stCompanyBaseRelationDao.insert(stCompanyBaseRelationDto);
            }
        }

        //再增加系统登录用户
        if (checkPhone(stCompanyBaseEsu.getManagePhone())) {
            return a;
        } else {
            // 内部人员同步到euauth
            // 1 添加用户
            PubUserSync pubUserSync = new PubUserSync();
            pubUserSync.setUserId(baseId);
            pubUserSync.setUserName(stCompanyBaseEsu.getManageName());
            pubUserSync.setLoginName(stCompanyBaseEsu.getManagePhone());
            pubUserSync.setPassword(ItemConstant.USER_PASSSWORD_DEFAUTL);
            Integer isLocked = 0;
            pubUserSync.setIsLocked(isLocked);
            pubUserSync.setMobilephone(stCompanyBaseEsu.getManagePhone());
            pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);
            pubUserSync.setCreateTime(new Date());
            userSyncFeign.addSync(pubUserSync);
            // 2 添加用户与角色关系
            PubUserRoleDTO pubUserRoleDTO = new PubUserRoleDTO();
            pubUserRoleDTO.setUserId(baseId);
            pubUserRoleDTO.setRoleIds(new String[]{ItemConstant.USER_ROLE_DEFAUTL});
            userSyncFeign.addPubUserRoleKeyList(pubUserRoleDTO);
        }
        return a;
    }

    /**
     * 修改
     *
     * @param stCompanyBaseEsu
     * @return
     */
    @Transactional
    @Override
    public Object updateStCompanyBase(StCompanyBaseEsu stCompanyBaseEsu) {

        //先增加基础表
        StCompanyBaseDto stCompanyBase = new StCompanyBaseDto();
        stCompanyBase.setCompany(stCompanyBaseEsu.getCompany());
        stCompanyBase.setManageName(stCompanyBaseEsu.getManageName());
        stCompanyBase.setManagePhone(stCompanyBaseEsu.getManagePhone());
        stCompanyBase.setUnitId(stCompanyBaseEsu.getUnitId());
        stCompanyBase.setUnitName(stCompanyBaseEsu.getUnitName());
        stCompanyBase.setGmtCreate(new Date());
        stCompanyBase.setRemark(stCompanyBaseEsu.getRemark());
        stCompanyBase.setServiceYear(stCompanyBaseEsu.getServiceYear());
        String baseId = stCompanyBaseEsu.getId();
        stCompanyBase.setId(baseId);
        int a = stCompanyBaseDao.updateById(stCompanyBase);

        //先删除该三方公司下的服务类型 和管护河段
        String stCompanyBaseId = stCompanyBaseEsu.getId();
        QueryWrapper<StCompanyBaseRelationDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("st_company_base_id", baseId);
        int delete = stCompanyBaseRelationDao.delete(queryWrapper);

        //再增加关联表
        //服务类型
        List serviceType = stCompanyBaseEsu.getServiceType();
        if (serviceType != null && serviceType.size() > 0) {
            for (int i = 0; i < serviceType.size(); i++) {
                StCompanyBaseRelationEsu esu = (StCompanyBaseRelationEsu) serviceType.get(i);
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setDataId(esu.getDataId());
                stCompanyBaseRelationDto.setStCompanyBaseId(baseId);
                stCompanyBaseRelationDto.setType(esu.getType());
                stCompanyBaseRelationDto.setGmtCreate(new Date());
                stCompanyBaseRelationDto.setDataName(esu.getDataName());
                stCompanyBaseRelationDao.insert(stCompanyBaseRelationDto);
            }

        }

        //增加管护河段
        List manageRiver = stCompanyBaseEsu.getManageRiver();
        if (manageRiver != null && manageRiver.size() > 0) {
            for (int i = 0; i < manageRiver.size(); i++) {
                StCompanyBaseRelationEsu esu = (StCompanyBaseRelationEsu) manageRiver.get(i);
                StCompanyBaseRelationDto stCompanyBaseRelationDto = new StCompanyBaseRelationDto();
                stCompanyBaseRelationDto.setDataId(esu.getDataId());
                stCompanyBaseRelationDto.setStCompanyBaseId(baseId);
                stCompanyBaseRelationDto.setType(esu.getType());
                stCompanyBaseRelationDto.setGmtCreate(new Date());
                stCompanyBaseRelationDto.setDataName(esu.getDataName());
                stCompanyBaseRelationDao.insert(stCompanyBaseRelationDto);
            }
        }

        //再修改系统登录用户
//        if (checkPhone(stCompanyBaseEsu.getManagePhone())) {
//            return a;
//        } else {
        // 内部人员同步到euauth
        // 1 添加用户
        PubUserSync pubUserSync = new PubUserSync();
        boolean isupdate = false;
        pubUserSync.setUserId(stCompanyBaseEsu.getId());
        Integer isLocked = 0;
        pubUserSync.setIsLocked(isLocked);

        if (!org.springframework.util.StringUtils.isEmpty(stCompanyBaseEsu.getManageName())) {
            pubUserSync.setUserName(stCompanyBaseEsu.getManageName());
            isupdate = true;
        }
        if (!org.springframework.util.StringUtils.isEmpty(stCompanyBaseEsu.getManagePhone())) {
            pubUserSync.setLoginName(stCompanyBaseEsu.getManagePhone());
            pubUserSync.setMobilephone(stCompanyBaseEsu.getManagePhone());
            isupdate = true;
        }
        if (!StringUtils.isEmpty(stCompanyBaseEsu.getUnitId())) {
            pubUserSync.setCorpId(stCompanyBaseEsu.getUnitId());
            isupdate = true;
        }
        if (isupdate) {
            userSyncFeign.updateSync(pubUserSync);
        }
//        }
        return a;
    }

    /**
     * 根据idx查询第三方服务公司基础表管理
     *
     * @param id
     * @return
     */
    @Override
    public Object selectById(String id) {

        StCompanyBaseDto stCompanyBaseDto = stCompanyBaseDao.selectById(id);
        StCompanyBaseEsu stCompanyBaseEsu = new StCompanyBaseEsu();
        stCompanyBaseEsu.setId(stCompanyBaseDto.getId());
        stCompanyBaseEsu.setCompany(stCompanyBaseDto.getCompany());
        stCompanyBaseEsu.setCreator(stCompanyBaseDto.getCreator());
        stCompanyBaseEsu.setGmtCreate(stCompanyBaseDto.getGmtCreate());
        stCompanyBaseEsu.setManageName(stCompanyBaseDto.getManageName());
        stCompanyBaseEsu.setManagePhone(stCompanyBaseDto.getManagePhone());
        stCompanyBaseEsu.setRemark(stCompanyBaseDto.getRemark());
        stCompanyBaseEsu.setServiceYear(stCompanyBaseDto.getServiceYear());
        stCompanyBaseEsu.setUnitId(stCompanyBaseDto.getUnitId());
        stCompanyBaseEsu.setUnitName(stCompanyBaseDto.getUnitName());
        //服务类型
        String stCompanyBaseId = stCompanyBaseEsu.getId();
        QueryWrapper<StCompanyBaseRelationDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("st_company_base_id", stCompanyBaseId);
        queryWrapper.eq("type", 1);
        List<StCompanyBaseRelationDto> list1 = stCompanyBaseRelationDao.selectList(queryWrapper);

        //管护河段
        QueryWrapper<StCompanyBaseRelationDto> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("st_company_base_id", stCompanyBaseId);
        queryWrapper2.eq("type", 2);
        List<StCompanyBaseRelationDto> list2 = stCompanyBaseRelationDao.selectList(queryWrapper2);

        stCompanyBaseEsu.setServiceType(list1);
        stCompanyBaseEsu.setManageRiver(list2);
        return stCompanyBaseEsu;
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @Override
    public Paginator<StCompanyBaseEsr> searchAll(PaginatorParam param, String orderType) {

        if (!"".equals(orderType) && orderType != null) {
            List<StCompanyBaseRelationDto> list = new ArrayList<>();
            //绿化 保洁的
            if (ItemConstant.ORDER_TYPE_BJ.equals(orderType) || ItemConstant.ORDER_TYPE_LH.equals(orderType)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("type", 1);
                queryWrapper.eq("data_id", 1);
                list = stCompanyBaseRelationDao.selectList(queryWrapper);
            }
            //闸坝运行养护的
            if (ItemConstant.ORDER_TYPE_WB.equals(orderType) || ItemConstant.ORDER_TYPE_YX.equals(orderType)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("type", 1);
                queryWrapper.eq("data_id", 2);
                list = stCompanyBaseRelationDao.selectList(queryWrapper);
            }
            List<String> classIds = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                StCompanyBaseRelationDto stCompanyBaseRelationDto = list.get(i);
                String stCompanyBaseId = stCompanyBaseRelationDto.getStCompanyBaseId();
                classIds.add(stCompanyBaseId);
            }
            if (classIds.size() > 0) {
                List currency = new ArrayList<>();
                Criterion criterion = new Criterion();
                criterion.setFieldName("id");
                criterion.setOperator(Criterion.IN);
                criterion.setValue(classIds);
                currency.add(criterion);
                param.setCurrency(currency);
            }
        }


        Paginator<StCompanyBaseEsr> p = stCompanyBaseService.findByPaginator(param);
        List<StCompanyBaseEsr> items = p.getItems();
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                StCompanyBaseEsr stCompanyBaseEsr = items.get(i);
                String stCompanyBaseId = stCompanyBaseEsr.getId();
                QueryWrapper<StCompanyBaseRelationDto> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("st_company_base_id", stCompanyBaseId);
                queryWrapper.eq("type", 1);
                List<StCompanyBaseRelationDto> list1 = stCompanyBaseRelationDao.selectList(queryWrapper);
                List<StCompanyBaseRelationEsu> serviceType = new ArrayList<>();
                for (int j = 0; j < list1.size(); j++) {
                    StCompanyBaseRelationDto dto = list1.get(j);
                    StCompanyBaseRelationEsu esu = new StCompanyBaseRelationEsu();
                    esu.setCreator(dto.getCreator());
                    esu.setDataId(dto.getDataId());
                    esu.setDataName(dto.getDataName());
                    esu.setGmtCreate(dto.getGmtCreate());
                    esu.setId(dto.getId());
                    esu.setRemark(dto.getRemark());
                    esu.setStCompanyBaseId(dto.getStCompanyBaseId());
                    esu.setType(dto.getType());
                    serviceType.add(esu);
                }
                stCompanyBaseEsr.setServiceType(serviceType);

                //管护河段
                QueryWrapper<StCompanyBaseRelationDto> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("st_company_base_id", stCompanyBaseId);
                queryWrapper2.eq("type", 2);
                List<StCompanyBaseRelationDto> list2 = stCompanyBaseRelationDao.selectList(queryWrapper2);
                List<StCompanyBaseRelationEsu> manageRiver = new ArrayList<>();
                for (int k = 0; k < list2.size(); k++) {
                    StCompanyBaseRelationDto dto = list2.get(k);
                    StCompanyBaseRelationEsu esu = new StCompanyBaseRelationEsu();
                    esu.setCreator(dto.getCreator());
                    esu.setDataId(dto.getDataId());
                    esu.setDataName(dto.getDataName());
                    esu.setGmtCreate(dto.getGmtCreate());
                    esu.setId(dto.getId());
                    esu.setRemark(dto.getRemark());
                    esu.setStCompanyBaseId(dto.getStCompanyBaseId());
                    esu.setType(dto.getType());
                    manageRiver.add(esu);
                }
                stCompanyBaseEsr.setManageRiver(manageRiver);
            }

        }

        return p;
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @Override
    public Paginator<EventCompanyEsr> searchAllR(PaginatorParam param, String orderType, String userId) {
        List<EventCompanyDto> list = new ArrayList<>();
        if (!"".equals(orderType) && orderType != null) {
            //绿化 保洁的
            if (ItemConstant.ORDER_TYPE_BJ.equals(orderType) || ItemConstant.ORDER_TYPE_LH.equals(orderType)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("type", 1);
                queryWrapper.last("GROUP BY company_id,company,unit_id,name ");

                list = eventCompanyDao.selectList(queryWrapper);
            }
            //闸坝运行养护的
            if (ItemConstant.ORDER_TYPE_WB.equals(orderType) || ItemConstant.ORDER_TYPE_YX.equals(orderType)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("type", 2);
                queryWrapper.last("GROUP BY company_id,company,unit_id,name ");
                list = eventCompanyDao.selectList(queryWrapper);
            }
        }else{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", userId);
            queryWrapper.last("GROUP BY company_id,company,unit_id,name,type ");
            
            list = eventCompanyDao.selectList(queryWrapper);
        }

        Paginator<EventCompanyEsr> p = new Paginator();

        List<EventCompanyEsr> res = BeanUtil.copyToList(list, EventCompanyEsr.class);
        if (res != null && res.size() > 0) {
            for (int i = 0; i < res.size(); i++) {
                EventCompanyEsr eventCompanyEsr = res.get(i);
                eventCompanyEsr.setManageName(eventCompanyEsr.getName() == null ? "" : eventCompanyEsr.getName());
                eventCompanyEsr.setManagePhone(eventCompanyEsr.getPhone() == null ? "" : eventCompanyEsr.getPhone());
                eventCompanyEsr.setId(eventCompanyEsr.getCompanyId() == null ? "" : eventCompanyEsr.getCompanyId());
                String stCompanyBaseId = eventCompanyEsr.getCompanyId();
                String unitId = eventCompanyEsr.getUnitId();
                String name = eventCompanyEsr.getName();

                //管护河段
                QueryWrapper<EventCompanyDto> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("company_id", stCompanyBaseId);
                queryWrapper2.eq("unit_id", unitId);
                queryWrapper2.eq("name", name);
                if (!"".equals(orderType) && orderType != null) {
                    //绿化 保洁的
                    if (ItemConstant.ORDER_TYPE_BJ.equals(orderType) || ItemConstant.ORDER_TYPE_LH.equals(orderType)) {
                        queryWrapper2.eq("type", 1);
                    }
                    //闸坝运行养护的
                    if (ItemConstant.ORDER_TYPE_WB.equals(orderType) || ItemConstant.ORDER_TYPE_YX.equals(orderType)) {
                        queryWrapper2.eq("type", 2);
                    }
                }

                List<EventCompanyDto> list2 = eventCompanyDao.selectList(queryWrapper2);
                List<StCompanyBaseRelationEsu> manageRiver = new ArrayList<>();
                for (int k = 0; k < list2.size(); k++) {
                    EventCompanyDto dto = list2.get(k);
                    StCompanyBaseRelationEsu esu = new StCompanyBaseRelationEsu();
                    esu.setDataId(dto.getRiverId());
                    esu.setDataName(dto.getRvnm());
                    esu.setGmtCreate(dto.getGmtCreate());

                    esu.setStCompanyBaseId(dto.getCompanyId());
                    esu.setType(dto.getType()!=null?"":dto.getType().toString());
                    manageRiver.add(esu);
                }
                eventCompanyEsr.setManageRiver(manageRiver);
            }
        }

        p.setTotalCount(res.size());
        p.setCurrentPage(1);
        p.setItems(res);
        return p;
    }

    /**
     * 河湖管理-河道绿化保洁（绿化、保洁工单统计）
     *
     * @param unitId
     * @return
     */
    @Override
    public List<StCompanyBaseRelationDto> searchCount(String unitId) {
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        List<StCompanyBaseRelationDto> list = stCompanyBaseDao.searchCount(unitId, null);

        //获取当年
        Date start = TimeUtil.getYearStartDate(new Date());
        Date end = TimeUtil.getYearEndDate(new Date());
        for (int i = 0; i < list.size(); i++) {
            String stCompanyBaseId = list.get(i).getStCompanyBaseId();
            //2 保洁 3 绿化
            //统计公司当年保洁工单数
            String orderType = "2";
            Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
            //统计公司当年绿化工单数
            orderType = "3";
            Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
            list.get(i).setCount2(count2);
            list.get(i).setCount3(count3);
            //获取该公司的服务河段

            //管护河段
            QueryWrapper<StCompanyBaseRelationDto> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("st_company_base_id", stCompanyBaseId);
            queryWrapper2.eq("type", 2);
            List<StCompanyBaseRelationDto> list2 = stCompanyBaseRelationDao.selectList(queryWrapper2);
            List<StCompanyBaseRelationEsu> manageRiver = new ArrayList<>();
            for (int k = 0; k < list2.size(); k++) {
                StCompanyBaseRelationDto dto = list2.get(k);
                StCompanyBaseRelationEsu esu = new StCompanyBaseRelationEsu();
                esu.setCreator(dto.getCreator());
                esu.setDataId(dto.getDataId());
                esu.setDataName(dto.getDataName());
                esu.setGmtCreate(dto.getGmtCreate());
                esu.setId(dto.getId());
                esu.setRemark(dto.getRemark());
                esu.setStCompanyBaseId(dto.getStCompanyBaseId());
                esu.setType(dto.getType());
                manageRiver.add(esu);
            }
            list.get(i).setManageRiver(manageRiver);


        }
        return list;
    }

    @Override
    public List<StCompanyBaseRelationDto> searchStCompanyBaseNew(StCompanyBaseDtos stCompanyBaseDtos) {
        String company = stCompanyBaseDtos.getCompany();
        if ("".equals(company) || company == null || "null".equals(company)) {
            company = null;
        }
        //标记  1- 查询绿化保洁的   2-查询运行维保的标记  1- 查询绿化保洁的   2-查询运行维保的
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();

        //默认查当年
        Date start = null;
        Date end = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
        }
        //按月
        if (dateType.equals(2)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM");
            start = DateUtil.beginOfMonth(dateTime);
            end = DateUtil.endOfMonth(dateTime);
        }
        //按日
        if (dateType.equals(3)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM-dd");
            start = DateUtil.beginOfDay(dateTime);
            end = DateUtil.endOfDay(dateTime);
        }
        //按周
        if (dateType.equals(4)) {
            start = stCompanyBaseDtos.getStartTime();
            end = stCompanyBaseDtos.getEndTime();
        }

        //标记  1- 查询绿化保洁的   2-查询运行维保的
        String flag = stCompanyBaseDtos.getFlag();
        List<StCompanyBaseRelationDto> list1 = null;
        List<StCompanyBaseRelationDto> list2 = null;
        List<StCompanyBaseRelationDto> list3 = null;
        //查询工单集合
        List<WorkorderNewest> workorderBases = workorderNewestService.selectWorkorderBase(flag, start, end);
        Map<String, List<WorkorderNewest>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(workorderBases)) {
            map = workorderBases.stream().collect(Collectors.groupingBy(WorkorderNewest::getCompanyId));
        }
        //绿化保洁的
        if ("1".equals(flag)) {
            //查询三方公司
            list1 = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, company, stCompanyBaseDtos.getFlag(), start)).orElse(Lists.newArrayList());
            for (int i = 0; i < list1.size(); i++) {
                Integer workBJSize = 0;
                Integer workLHSize = 0;
                Integer workSize = 0;
                String stCompanyBaseId = list1.get(i).getStCompanyBaseId();
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workBJ = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workLH = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("3")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workBJSize = workBJ.size();
                    workLHSize = workLH.size();
                    List<WorkorderNewest> workBJCount = Optional.ofNullable(workBJ.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workSize = workBJCount.size();
                    List<WorkorderNewest> workLHCount = Optional.ofNullable(workBJ.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workSize += workLHCount.size();
                }
                //获取公司下绿化保洁已完成工单总数（待审核和已归档的）
                list1.get(i).setCount2(workBJSize);
                list1.get(i).setCount3(workLHSize);
                list1.get(i).setTotal(workBJSize + workLHSize);
                list1.get(i).setDealCount(workSize);
                list1.get(i).setStart(stCompanyBaseDtos.getDate());
                list1.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            return list1;
        } else if ("2".equals(flag)) {
            //运行维保的
            //查询三方公司
            list2 = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, company, stCompanyBaseDtos.getFlag(), start)).orElse(Lists.newArrayList());
            //
            for (int i = 0; i < list2.size(); i++) {
                String stCompanyBaseId = list2.get(i).getStCompanyBaseId();
                //4维保 5运行
                Integer workWBSize = 0;
                Integer workYXSize = 0;
                Integer workCountSize = 0;
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workWB = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("4")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workYX = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workWBSize = workWB.size();
                    workYXSize = workYX.size();

                    List<WorkorderNewest> workWBount = Optional.ofNullable(workWB.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workCountSize = workWBount.size();
                    List<WorkorderNewest> workYXCount = Optional.ofNullable(workYX.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workCountSize += workYXCount.size();

                }
                list2.get(i).setCount2(workWBSize);
                list2.get(i).setCount3(workYXSize);
                list2.get(i).setTotal(workWBSize + workYXSize);
                list2.get(i).setDealCount(workCountSize);
                list2.get(i).setStart(stCompanyBaseDtos.getDate());
                list2.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            return list2;
        } else {
            list1 = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, company, "1", start)).orElse(Lists.newArrayList());
            for (int i = 0; i < list1.size(); i++) {
                Integer workBJSize = 0;
                Integer workLHSize = 0;
                Integer workSize = 0;
                String stCompanyBaseId = list1.get(i).getStCompanyBaseId();
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workBJ = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workLH = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("3")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workBJSize = workBJ.size();
                    workLHSize = workLH.size();
                    List<WorkorderNewest> workBJCount = Optional.ofNullable(workBJ.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workSize = workBJCount.size();
                    List<WorkorderNewest> workLHCount = Optional.ofNullable(workBJ.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workSize += workLHCount.size();
                }
                //获取公司下绿化保洁已完成工单总数（待审核和已归档的）
                list1.get(i).setCount2(workBJSize);
                list1.get(i).setCount3(workLHSize);
                list1.get(i).setTotal(workBJSize + workLHSize);
                list1.get(i).setDealCount(workSize);
                list1.get(i).setStart(stCompanyBaseDtos.getDate());
                list1.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            //运行维保的
            //查询三方公司
            list2 = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, company,"2", start)).orElse(Lists.newArrayList());
            //
            for (int i = 0; i < list2.size(); i++) {
                String stCompanyBaseId = list2.get(i).getStCompanyBaseId();
                //4维保 5运行
                Integer workWBSize = 0;
                Integer workYXSize = 0;
                Integer workCountSize = 0;
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workWB = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("4")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workYX = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workWBSize = workWB.size();
                    workYXSize = workYX.size();

                    List<WorkorderNewest> workWBount = Optional.ofNullable(workWB.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workCountSize = workWBount.size();
                    List<WorkorderNewest> workYXCount = Optional.ofNullable(workYX.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workCountSize += workYXCount.size();

                }
                list2.get(i).setCount2(workWBSize);
                list2.get(i).setCount3(workYXSize);
                list2.get(i).setTotal(workWBSize + workYXSize);
                list2.get(i).setDealCount(workCountSize);
                list2.get(i).setStart(stCompanyBaseDtos.getDate());
                list2.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            list3 = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list1)) {
                list3.addAll(list1);
            }
            if (!CollectionUtils.isEmpty(list2)) {
                list3.addAll(list2);
            }
            return list3;
        }
    }

    /**
     * 管河成效-第三方服务公司处理工单统计
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @Override
    public List<StCompanyBaseRelationDto> searchStCompanyBase(StCompanyBaseDtos stCompanyBaseDtos) {
        String company = stCompanyBaseDtos.getCompany();
        if ("".equals(company) || company == null || "null".equals(company)) {
            company = null;
        }
        //标记  1- 查询绿化保洁的   2-查询运行维保的标记  1- 查询绿化保洁的   2-查询运行维保的
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();

        //默认查当年
        Date start = null;
        Date end = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
        }
        //按月
        if (dateType.equals(2)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM");
            start = DateUtil.beginOfMonth(dateTime);
            end = DateUtil.endOfMonth(dateTime);
        }
        //按日
        if (dateType.equals(3)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM-dd");
            start = DateUtil.beginOfDay(dateTime);
            end = DateUtil.endOfDay(dateTime);
        }
        //按周
        if (dateType.equals(4)) {
            start = stCompanyBaseDtos.getStartTime();
            end = stCompanyBaseDtos.getEndTime();
        }

        //标记  1- 查询绿化保洁的   2-查询运行维保的
        String flag = stCompanyBaseDtos.getFlag();
        List<StCompanyBaseRelationDto> list1 = null;
        List<StCompanyBaseRelationDto> list2 = null;
        List<StCompanyBaseRelationDto> list3 = null;
        //绿化保洁的
        if ("1".equals(flag)) {
            //  list1 = stCompanyBaseDao.searchCount(unitId, company);
            //todo--------------------------------------
            list1 = eventCompanyService.searchCount(unitId, company);
            for (int i = 0; i < list1.size(); i++) {
                String stCompanyBaseId = list1.get(i).getStCompanyBaseId();
                //2 保洁 3 绿化
                //统计公司当年保洁工单数
                String orderType = "2";
                //todo--------------------------------------
                //    Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count2 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //统计公司当年绿化工单数
                orderType = "3"; //todo--------------------------------------

                //  Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count3 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //获取公司下绿化保洁已完成工单总数（待审核和已归档的）
                //todo--------------------------------------
                //   Integer count4 = stCompanyBaseDao.searchByStCompanyBaseId5(stCompanyBaseId, start, end);
                Integer count4 = eventCompanyService.searchByStCompanyBaseId5(stCompanyBaseId, start, end);
                list1.get(i).setCount2(count2);
                list1.get(i).setCount3(count3);
                list1.get(i).setTotal(count2 + count3);
                list1.get(i).setDealCount(count4);
                list1.get(i).setStart(stCompanyBaseDtos.getDate());
                list1.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            return list1;
        } else if ("2".equals(flag)) {
            //运行维保的
            //  list2 = stCompanyBaseDao.searchCount2(unitId, company);
            list2 = eventCompanyService.searchCount2(unitId, company);
            //
            for (int i = 0; i < list2.size(); i++) {
                String stCompanyBaseId = list2.get(i).getStCompanyBaseId();
                //4维保 5运行
                //统计公司当年维保工单数
                String orderType = "4";
                //todo--------------------------------------
                //  Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count2 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //统计公司当年运行工单数
                orderType = "5";
                //todo--------------------------------------
                //    Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count3 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //获取公司下运行维保已完成工单总数（待审核和已归档的）
                //todo--------------------------------------
                //  Integer count4 = stCompanyBaseDao.searchByStCompanyBaseId6(stCompanyBaseId, start, end);
                Integer count4 = eventCompanyService.searchByStCompanyBaseId6(stCompanyBaseId, start, end);
                list2.get(i).setCount2(count2);
                list2.get(i).setCount3(count3);
                list2.get(i).setTotal(count2 + count3);
                list2.get(i).setDealCount(count4);
                list2.get(i).setStart(stCompanyBaseDtos.getDate());
                list2.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            return list2;
        } else {
            //  list1 = stCompanyBaseDao.searchCount(unitId, company);
            list1 = eventCompanyService.searchCount(unitId, company);
            for (int i = 0; i < list1.size(); i++) {
                String stCompanyBaseId = list1.get(i).getStCompanyBaseId();
                //2 保洁 3 绿化
                //统计公司当年保洁工单数
                String orderType = "2";
                //todo--------------------------------------
                //  Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count2 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //统计公司当年绿化工单数
                orderType = "3";
                //todo--------------------------------------
                //  Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count3 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //获取公司下绿化保洁已完成工单总数（待审核和已归档的）
                //todo--------------------------------------
                //    Integer count4 = stCompanyBaseDao.searchByStCompanyBaseId5(stCompanyBaseId, start, end);
                Integer count4 = eventCompanyService.searchByStCompanyBaseId5(stCompanyBaseId, start, end);
                list1.get(i).setCount2(count2);
                list1.get(i).setCount3(count3);
                list1.get(i).setTotal(count2 + count3);
                list1.get(i).setDealCount(count4);
                list1.get(i).setStart(stCompanyBaseDtos.getDate());
                list1.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            //运行维保的
            //  list2 = stCompanyBaseDao.searchCount2(unitId, company);
            list2 = eventCompanyService.searchCount2(unitId, company);
            for (int i = 0; i < list2.size(); i++) {
                String stCompanyBaseId = list2.get(i).getStCompanyBaseId();
                //4维保 5运行
                //统计公司当年维保工单数
                String orderType = "4";
                //todo--------------------------------------
                //   Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count2 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //统计公司当年运行工单数
                orderType = "5";
                //todo--------------------------------------
                // Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                Integer count3 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                //获取公司下运行维保已完成工单总数（待审核和已归档的）
                //todo--------------------------------------
                // Integer count4 = stCompanyBaseDao.searchByStCompanyBaseId6(stCompanyBaseId, start, end);
                Integer count4 = eventCompanyService.searchByStCompanyBaseId6(stCompanyBaseId, start, end);
                list2.get(i).setCount2(count2);
                list2.get(i).setCount3(count3);
                list2.get(i).setTotal(count2 + count3);
                list2.get(i).setDealCount(count4);
                list2.get(i).setStart(stCompanyBaseDtos.getDate());
                list2.get(i).setEnd(stCompanyBaseDtos.getDate());
            }
            list3 = new ArrayList<>();
            list3.addAll(list1);
            list3.addAll(list2);
            return list3;
        }

    }

    @Override
    public List<StCompanyBaseRelationDto> searchStCompanyBaseDealNew(StCompanyBaseDtos stCompanyBaseDtos) {
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();
        //默认查当年
        Date start = null;
        Date end = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
        }
        //按月
        if (dateType.equals(2)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM");
            start = DateUtil.beginOfMonth(dateTime);
            end = DateUtil.endOfMonth(dateTime);
        }
        //按日
        if (dateType.equals(3)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM-dd");
            start = DateUtil.beginOfDay(dateTime);
            end = DateUtil.endOfDay(dateTime);
        }
        //按周
        if (dateType.equals(4)) {
            start = stCompanyBaseDtos.getStartTime();
            end = stCompanyBaseDtos.getEndTime();
        }
        //查询公司数据
        Map<String, List<EventBase>> map = new HashMap<>();
        List<StCompanyBaseRelationDto> list = Optional.ofNullable(eventCompanyService.selectCompany(unitId, stCompanyBaseDtos.getFlag(), start)).orElse(Lists.newArrayList());
        List<EventBase> eventBasesZ = Optional.ofNullable(eventBaseService.selectEventBase(start, end, stCompanyBaseDtos.getFlag())).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(eventBasesZ)) {
            map = eventBasesZ.stream().filter(eventBase -> {
                return StrUtil.isNotEmpty(eventBase.getCompanyId()) ;}).collect(Collectors.groupingBy(EventBase::getCompanyId));
        }
        for (int i = 0; i < list.size(); i++) {
            String stCompanyBaseId = list.get(i).getStCompanyBaseId();
            List<EventBase> eventBases = map.get(stCompanyBaseId);
            if (!CollectionUtils.isEmpty(eventBases)) {
                list.get(i).setTotal(eventBases.size());
                List<EventBase> eventBaseList = Optional.ofNullable(eventBases.stream().filter(x -> x.getStatus().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                list.get(i).setDealCount(eventBaseList.size());
            } else {
                list.get(i).setTotal(0);
                list.get(i).setDealCount(0);
            }
        }
        return list;
    }

    /**
     * 管河成效-第三方服务公司案件处理效能统计
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @Override
    public List<StCompanyBaseRelationDto> searchStCompanyBaseDeal(StCompanyBaseDtos stCompanyBaseDtos) {
        //标记  1- 查询绿化保洁的   2-查询运行维保的标记  1- 查询绿化保洁的   2-查询运行维保的
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }

        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();

        //默认查当年
        Date start = null;
        Date end = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
        }
        //按月
        if (dateType.equals(2)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM");
            start = DateUtil.beginOfMonth(dateTime);
            end = DateUtil.endOfMonth(dateTime);
        }
        //按日
        if (dateType.equals(3)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM-dd");
            start = DateUtil.beginOfDay(dateTime);
            end = DateUtil.endOfDay(dateTime);
        }
        //按周
        if (dateType.equals(4)) {
            start = stCompanyBaseDtos.getStartTime();
            end = stCompanyBaseDtos.getEndTime();
        }

        //获取所有的公司
        //List<StCompanyBaseRelationDto> list = stCompanyBaseDao.searchCount3(unitId);
        //todo
        List<StCompanyBaseRelationDto> list = eventCompanyService.searchCount3(unitId);
        for (int i = 0; i < list.size(); i++) {
            String stCompanyBaseId = list.get(i).getStCompanyBaseId();
            //获取公司下工单总数
            //todo  ---------
            //    Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId2(stCompanyBaseId, start, end);
            Integer count2 = eventCompanyService.searchByStCompanyBaseId2(stCompanyBaseId, start, end);
            //获取公司下已完成工单总数（待审核和已归档的）
            //todo  ---------
            //  Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId3(stCompanyBaseId, start, end);
            Integer count3 = eventCompanyService.searchByStCompanyBaseId3(stCompanyBaseId, start, end);
            list.get(i).setTotal(count2);
            list.get(i).setDealCount(count3);
        }
        //1- 查询绿化保洁的   2-查询运行维保的
        String flag = stCompanyBaseDtos.getFlag();
        if (!"".equals(flag) && flag != null) {
            list = list.stream().filter(p -> p.getType().equals(flag)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 第三方服务公司管河成效统计-绿化保洁工单
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @Override
    public List<StCompanyBaseRelationDto> searchStCompanyBaseCount(StCompanyBaseDtos stCompanyBaseDtos) {
        //标记  1- 查询绿化保洁的   2-查询运行维保的标记  1- 查询绿化保洁的   2-查询运行维保的
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();
        //默认查当年
        Date start = null;
        Date end = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
        }
        //按月
        if (dateType.equals(2)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM");
            start = DateUtil.beginOfMonth(dateTime);
            end = DateUtil.endOfMonth(dateTime);
        }
        //按日
        if (dateType.equals(3)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy-MM-dd");
            start = DateUtil.beginOfDay(dateTime);
            end = DateUtil.endOfDay(dateTime);
        }
        //按周
        if (dateType.equals(4)) {
            start = stCompanyBaseDtos.getStartTime();
            end = stCompanyBaseDtos.getEndTime();
        }

        //标记  1- 查询绿化保洁的   2-查询运行维保的
        String flag = stCompanyBaseDtos.getFlag();
        List<StCompanyBaseRelationDto> list = null;
        //查询工单集合
        List<WorkorderNewest> workorderBases = Optional.ofNullable(workorderNewestService.selectWorkorderBase(flag, start, end)).orElse(Lists.newArrayList());
        Map<String, List<WorkorderNewest>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(workorderBases)) {
            map = workorderBases.stream().collect(Collectors.groupingBy(WorkorderNewest::getCompanyId));
        }
        //绿化保洁的
        if ("1".equals(flag)) {
            //todo
            //  list = stCompanyBaseDao.searchCount(unitId, null);
            // list = eventCompanyService.searchCount(unitId, null);
            list = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, null, flag, start)).orElse(Lists.newArrayList());
            //查询公司
            //  Integer allCount = stCompanyBaseDao.searchByStCompanyBaseId4("2", "3", start, end);
            //Integer allCount = eventCompanyService.searchByStCompanyBaseId4("2", "3", start, end);
            Integer allCount = 0;
            if (!CollectionUtils.isEmpty(workorderBases)) {
                List<WorkorderNewest> workBJCount = Optional.ofNullable(workorderBases.stream().filter(x -> x.getOrderType().equals("2") || x.getOrderType().equals("3")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                allCount = workBJCount.size();
            }

            for (int i = 0; i < list.size(); i++) {
                String stCompanyBaseId = list.get(i).getStCompanyBaseId();

                Integer workBJSize = 0;
                Integer workLHSize = 0;
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workBJ = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workLH = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("3")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workBJSize = workBJ.size();
                    workLHSize = workLH.size();
                }
                //2 保洁 3 绿化
                Integer total = workBJSize + workLHSize;
                list.get(i).setCount2(workBJSize);
                list.get(i).setCount3(workLHSize);
                list.get(i).setAllCount(allCount);
                list.get(i).setTotal(workBJSize + workLHSize);
                String percent = "";
                if (allCount == 0) {
                    percent = "0.00%";
                } else {
                    percent = this.getPercent(total, allCount);
                }
                list.get(i).setPercent(percent);
            }
        }
        if ("2".equals(flag)) {
            //运行维保的
            //todo
            //  list = stCompanyBaseDao.searchCount2(unitId, null);
            // list = eventCompanyService.searchCount2(unitId, null);
            list = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, null, flag, start)).orElse(Lists.newArrayList());
            //    Integer allCount = stCompanyBaseDao.searchByStCompanyBaseId4("4", "5", start, end);
            Integer allCount = 0;
            if (!CollectionUtils.isEmpty(workorderBases)) {
                List<WorkorderNewest> workBJCount = Optional.ofNullable(workorderBases.stream().filter(x -> x.getOrderStatus().equals("4") || x.getOrderStatus().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                allCount = workBJCount.size();
            }
            for (int i = 0; i < list.size(); i++) {
                String stCompanyBaseId = list.get(i).getStCompanyBaseId();
                //4维保 5运行
                //统计公司当年维保工单数
                //4维保 5运行
                Integer workWBSize = 0;
                Integer workYXSize = 0;
                Integer workCountSize = 0;
                List<WorkorderNewest> workorderBases1 = Optional.ofNullable(map.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(workorderBases1)) {
                    //2 保洁 3 绿化
                    List<WorkorderNewest> workWB = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("4")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    List<WorkorderNewest> workYX = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("5")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    workWBSize = workWB.size();
                    workYXSize = workYX.size();
                }
                Integer total = workWBSize + workYXSize;
                list.get(i).setCount2(workWBSize);
                list.get(i).setCount3(workYXSize);
                list.get(i).setAllCount(allCount);
                list.get(i).setTotal(workWBSize + workYXSize);
                String percent = "";
                if (allCount == 0) {
                    percent = "0.00%";
                } else {
                    percent = this.getPercent(total, allCount);
                }
                list.get(i).setPercent(percent);
            }

        }

        return list;
    }

    /**
     * 第三方服务公司处理工单统计 每年的12个月
     *
     * @param stCompanyBaseDtos
     * @return
     */
    @Override
    public List searchStCompanyBaseMouth(StCompanyBaseDtos stCompanyBaseDtos) {
        List resList = new ArrayList();
        Map<String, List<StCompanyBaseRelationDto>> map = new HashMap<>();
        //标记  1- 查询绿化保洁的   2-查询运行维保的标记  1- 查询绿化保洁的   2-查询运行维保的
        String unitId = stCompanyBaseDtos.getUnitId();
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //1 年  2 月  3 日 4周
        Integer dateType = stCompanyBaseDtos.getDateType();
        //默认查当年
        Date start = null;
        Date end = null;
        List<DateTime> mouthList = null;
        //按年
        if (dateType.equals(1)) {
            DateTime dateTime = DateUtil.parse(stCompanyBaseDtos.getDate(), "yyyy");
            start = DateUtil.beginOfYear(dateTime);
            end = DateUtil.endOfYear(dateTime);
            mouthList = DateUtil.rangeToList(start, end, DateField.MONTH);

        }

        //标记  1- 查询绿化保洁的   2-查询运行维保的
        String flag = stCompanyBaseDtos.getFlag();
        List<StCompanyBaseRelationDto> list = null;
        //查询工单集合
        List<WorkorderNewest> workorderBases = Optional.ofNullable(workorderNewestService.selectWorkorderBase(flag, start, end)).orElse(Lists.newArrayList());
        Map<String, List<WorkorderNewest>> mapWork = new HashMap<>();
        if (!CollectionUtils.isEmpty(workorderBases)) {
            mapWork = workorderBases.stream().collect(Collectors.groupingBy(WorkorderNewest::getCompanyId));
        }
        if ("1".equals(flag)) {
            //循环获取到所有的月份，进行查询
            list = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, null, flag, start)).orElse(Lists.newArrayList());
            for (int k = 0; k < mouthList.size(); k++) {
                DateTime dateTime = mouthList.get(k);
                String preDayDate = TimeUtil.getPreDayDateMouth(dateTime);
                DateTime aaa = DateUtil.parse(preDayDate, "yyyy-MM");
                Date startTime = DateUtil.beginOfMonth(aaa);
                Date endTime = DateUtil.endOfMonth(aaa);
                List<StCompanyBaseRelationDto>stCompanyBaseRelationDtoList=new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    StCompanyBaseRelationDto stCompanyBaseRelationDto = list.get(i);
                    StCompanyBaseRelationDto stCompanyBaseRelation=new StCompanyBaseRelationDto();
                    BeanUtils.copyProperties(stCompanyBaseRelationDto,stCompanyBaseRelation);
                    String stCompanyBaseId = stCompanyBaseRelationDto.getStCompanyBaseId();
                    //2 保洁 3 绿化

                    Integer workBJSize = 0;
                    Integer workLHSize = 0;
                    List<WorkorderNewest> workorderBases1 = Optional.ofNullable(mapWork.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(workorderBases1)) {
                        //2 保洁 3 绿化
                        List<WorkorderNewest> workBJ = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("2") && x.getGmtCreate().getTime() >= startTime.getTime() && x.getGmtCreate().getTime() <= endTime.getTime()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        List<WorkorderNewest> workLH = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("3") && x.getGmtCreate().getTime() >= startTime.getTime() && x.getGmtCreate().getTime() <= endTime.getTime()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        workBJSize = workBJ.size();
                        workLHSize = workLH.size();
                    }
                    //统计公司当年保洁工单数
                    /*String orderType = "2";
                    //  Integer count2 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                    Integer count2 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                    //统计公司当年绿化工单数
                    orderType = "3";
                    //  Integer count3 = stCompanyBaseDao.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);
                    Integer count3 = eventCompanyService.searchByStCompanyBaseId(stCompanyBaseId, orderType, start, end);*/
                    stCompanyBaseRelation.setCount2(workBJSize);
                    stCompanyBaseRelation.setCount3(workLHSize);
                    stCompanyBaseRelation.setTotal(workBJSize + workLHSize);
                    stCompanyBaseRelation.setPreDayDate(DateUtil.format(aaa, "yyyy-MM"));
                    stCompanyBaseRelationDtoList.add(stCompanyBaseRelation);
                }
                resList.add(stCompanyBaseRelationDtoList);
            }
        } else {
            list = Optional.ofNullable(eventCompanyService.selectCompanys(unitId, null, flag, start)).orElse(Lists.newArrayList());
            for (int k = 0; k < mouthList.size(); k++) {
                DateTime dateTime = mouthList.get(k);
                String preDayDate = TimeUtil.getPreDayDateMouth(dateTime);
                DateTime aaa = DateUtil.parse(preDayDate, "yyyy-MM");
                Date startTime = DateUtil.beginOfMonth(aaa);
                Date endTime = DateUtil.endOfMonth(aaa);
                List<StCompanyBaseRelationDto>stCompanyBaseRelationDtoList=new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    StCompanyBaseRelationDto stCompanyBaseRelationDto = list.get(i);
                    StCompanyBaseRelationDto stCompanyBaseRelation=new StCompanyBaseRelationDto();
                    BeanUtils.copyProperties(stCompanyBaseRelationDto,stCompanyBaseRelation);
                    String stCompanyBaseId = list.get(i).getStCompanyBaseId();
                    //4维保 5运行

                    Integer workWBSize = 0;
                    Integer workYHSize = 0;
                    List<WorkorderNewest> workorderBases1 = Optional.ofNullable(mapWork.get(stCompanyBaseId)).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(workorderBases1)) {
                        //2 保洁 3 绿化
                        List<WorkorderNewest> workWB = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("4") && x.getGmtCreate().getTime() >= startTime.getTime() && x.getGmtCreate().getTime() <= endTime.getTime()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        List<WorkorderNewest> workYH = Optional.ofNullable(workorderBases1.stream().filter(x -> x.getOrderType().equals("5") && x.getGmtCreate().getTime() >= startTime.getTime() && x.getGmtCreate().getTime() <= endTime.getTime()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        workWBSize = workWB.size();
                        workYHSize = workYH.size();
                    }
                    stCompanyBaseRelation.setCount2(workWBSize);
                    stCompanyBaseRelation.setCount3(workYHSize);
                    stCompanyBaseRelation.setTotal(workWBSize + workYHSize);
                    stCompanyBaseRelation.setPreDayDate(DateUtil.format(aaa, "yyyy-MM"));
                    stCompanyBaseRelationDtoList.add(stCompanyBaseRelation);
                }
                resList.add(stCompanyBaseRelationDtoList);
            }
        }
        return resList;
    }

    /**
     * 获取百分比
     *
     * @param total
     * @param allCount
     * @return
     */
    private String getPercent(Integer total, Integer allCount) {
        String percent = "";
        if (new BigDecimal(allCount).compareTo(BigDecimal.ZERO) == 0) {
            percent = BigDecimal.ZERO.toString();
        }
        BigDecimal res = new BigDecimal(total).multiply(new BigDecimal(100)).divide(new BigDecimal(allCount), 2, RoundingMode.HALF_UP);
        percent = res.toString();
        return percent + "%";
    }

    /**
     * 检查手机号是否存在
     *
     * @param telephone
     * @return true 存在 false 不存在
     */
    private boolean checkPhone(String telephone) {
        // 查询euauth是否有账号为此手机号
        ValidResponse validResponse = userSyncFeign.loginNameCheck(telephone);
        return !(Boolean) validResponse.getResult();
    }
}
