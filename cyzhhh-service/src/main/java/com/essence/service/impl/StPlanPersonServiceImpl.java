package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.StPlanPersonDao;
import com.essence.dao.entity.EventCompanyDto;
import com.essence.dao.entity.StPlanPersonDto;
import com.essence.euauth.entity.PubUserRoleDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.UserSyncFeign;
import com.essence.interfaces.api.StPlanPersonService;
import com.essence.interfaces.model.StPlanPersonEsr;
import com.essence.interfaces.model.StPlanPersonEsu;
import com.essence.interfaces.param.StPlanPersonEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanPersonEtoT;
import com.essence.service.converter.ConverterStPlanPersonTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * (三方养护人员信息表)业务层
 * @author liwy
 * @since 2023-07-17 14:52:45
 */
@Service
public class StPlanPersonServiceImpl extends BaseApiImpl<StPlanPersonEsu, StPlanPersonEsp, StPlanPersonEsr, StPlanPersonDto> implements StPlanPersonService {
    @Autowired
    private StPlanPersonDao stPlanPersonDao;
    @Autowired
    private StPlanPersonService stPlanPersonService;
    @Autowired
    private ConverterStPlanPersonEtoT converterStPlanPersonEtoT;
    @Autowired
    private ConverterStPlanPersonTtoR converterStPlanPersonTtoR;
    @Autowired
    private UserSyncFeign userSyncFeign;

    public StPlanPersonServiceImpl(StPlanPersonDao stPlanPersonDao, ConverterStPlanPersonEtoT converterStPlanPersonEtoT, ConverterStPlanPersonTtoR converterStPlanPersonTtoR) {
        super(stPlanPersonDao, converterStPlanPersonEtoT, converterStPlanPersonTtoR);
    }

    /**
     * 新增三方养护人员信息
     * @param stPlanPersonEsu
     * @return
     */
    @Override
    public Object addStPlanPerson(StPlanPersonEsu stPlanPersonEsu) {
        stPlanPersonEsu.setStatus("0"); //状态(0启用 1停用)
        //1.先增加基础表
        String baseId = UUID.randomUUID().toString().replace("-", "");
        stPlanPersonEsu.setPersonId(baseId);
        //先判断系统登录用户
        ValidResponse validResponse = checkPhone(stPlanPersonEsu.getPlanPhone());
        if (validResponse.getResult() !=null) {
            stPlanPersonEsu.setPersonId(validResponse.getResult().toString());
            return stPlanPersonService.insert(stPlanPersonEsu);
        } else {
            // 内部人员同步到euauth
            // 1 添加用户
            PubUserSync pubUserSync = new PubUserSync();
            pubUserSync.setUserId(baseId);
            pubUserSync.setUserName(stPlanPersonEsu.getPlanPerson());
            pubUserSync.setLoginName(stPlanPersonEsu.getPlanPhone());
            pubUserSync.setPassword(ItemConstant.USER_PASSSWORD_DEFAUTL);
            Integer isLocked = 0;
            pubUserSync.setIsLocked(isLocked);
            pubUserSync.setMobilephone(stPlanPersonEsu.getPlanPhone());
            pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);
            pubUserSync.setCreateTime(new Date());
            userSyncFeign.addSync(pubUserSync);
            // 2 添加用户与角色关系
            PubUserRoleDTO pubUserRoleDTO = new PubUserRoleDTO();
            pubUserRoleDTO.setUserId(baseId);
            pubUserRoleDTO.setRoleIds(new String[]{ItemConstant.USER_ROLE_DEFAUTL});
            userSyncFeign.addPubUserRoleKeyList(pubUserRoleDTO);
        }
        int a = stPlanPersonService.insert(stPlanPersonEsu);
        return a;
    }

    /**
     *修改三方养护人员信息
     * @param stPlanPersonEsu
     * @return
     */
    @Override
    public Object updateStPlanPerson(StPlanPersonEsu stPlanPersonEsu) {

        // 内部人员同步到euauth
        // 1 添加用户
        PubUserSync pubUserSync = new PubUserSync();
        boolean isupdate = false;
        pubUserSync.setUserId(stPlanPersonEsu.getPersonId());
        Integer isLocked = 0;
        pubUserSync.setIsLocked(isLocked);

        if (!org.springframework.util.StringUtils.isEmpty(stPlanPersonEsu.getPlanPerson())) {
            pubUserSync.setUserName(stPlanPersonEsu.getPlanPerson());
            isupdate = true;
        }
        if (!org.springframework.util.StringUtils.isEmpty(stPlanPersonEsu.getPlanPhone())) {
            pubUserSync.setLoginName(stPlanPersonEsu.getPlanPhone());
            pubUserSync.setMobilephone(stPlanPersonEsu.getPlanPhone());
            isupdate = true;
        }
        if (!StringUtils.isEmpty(stPlanPersonEsu.getUnitId())) {
            pubUserSync.setCorpId(stPlanPersonEsu.getUnitId());
            isupdate = true;
        }
        if (isupdate) {
            userSyncFeign.updateSync(pubUserSync);
        }

        int a = stPlanPersonService.update(stPlanPersonEsu);

        return a;


    }

    /**
     * 获取三方养护人员的手机号等
     * @param orderManager
     */
    @Override
    public List<StPlanPersonDto>  selectStPlanPerson(String orderManager) {
        QueryWrapper<StPlanPersonDto> queryWrapper = new QueryWrapper<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(orderManager)) {
            queryWrapper.lambda().eq(StPlanPersonDto::getPlanPerson, orderManager);
        }
        List<StPlanPersonDto> stPlanPersonDtoList = stPlanPersonDao.selectList(queryWrapper);
        return stPlanPersonDtoList;

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

}
