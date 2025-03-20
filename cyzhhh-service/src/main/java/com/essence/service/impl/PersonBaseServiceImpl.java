package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.PersonBaseDao;
import com.essence.dao.entity.PersonBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.euauth.entity.PubUserRoleDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.UserSyncFeign;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.PersonBaseService;
import com.essence.interfaces.api.RosteringInfoService;
import com.essence.interfaces.model.PersonBaseEsr;
import com.essence.interfaces.model.PersonBaseEsu;
import com.essence.interfaces.param.PersonBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterPersonBaseEtoT;
import com.essence.service.converter.ConverterPersonBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class PersonBaseServiceImpl extends BaseApiImpl<PersonBaseEsu, PersonBaseEsp, PersonBaseEsr, PersonBase> implements PersonBaseService {

    @Autowired
    private PersonBaseDao personBaseDao;
    @Autowired
    private ConverterPersonBaseEtoT converterPersonBaseEtoT;
    @Autowired
    private ConverterPersonBaseTtoR converterPersonBaseTtoR;

    @Autowired
    private UserSyncFeign userSyncFeign;
    @Autowired
    private RosteringInfoService rosteringInfoService;

    public PersonBaseServiceImpl(PersonBaseDao personBaseDao, ConverterPersonBaseEtoT converterPersonBaseEtoT, ConverterPersonBaseTtoR converterPersonBaseTtoR) {
        super(personBaseDao, converterPersonBaseEtoT, converterPersonBaseTtoR);
    }

    @Transactional
    @Override
    public int insert(PersonBaseEsu personBaseEsu) {

        if (checkPhone(personBaseEsu.getTelephone())) {
            throw new BusinessException("联系方式已存在");
        }
        // 主键
        String id = UuidUtil.get32UUIDStr();
        personBaseEsu.setId(id);
        int insert = super.insert(personBaseEsu);

        if (insert < 1 || !ItemConstant.PERSON_TYPE_INSIDE.equals(personBaseEsu.getType())) {
            return insert;
        }
        // 内部人员同步到euauth
        // 1 添加用户
        PubUserSync pubUserSync = new PubUserSync();
        pubUserSync.setUserId(id);

        pubUserSync.setUserName(personBaseEsu.getName());

        pubUserSync.setLoginName(personBaseEsu.getTelephone());

        pubUserSync.setPassword(ItemConstant.USER_PASSSWORD_DEFAUTL);

        Integer isLocked;
        try {
            isLocked = Integer.parseInt(personBaseEsu.getStatus());
        } catch (NumberFormatException e) {
            isLocked = 1;
        }

        pubUserSync.setIsLocked(isLocked);

        pubUserSync.setMobilephone(personBaseEsu.getTelephone());

        pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);

        pubUserSync.setCreateTime(new Date());

        pubUserSync.setCorpId(personBaseEsu.getUnitId());

        userSyncFeign.addSync(pubUserSync);
        // 2 添加用户与角色关系
        PubUserRoleDTO pubUserRoleDTO = new PubUserRoleDTO();
        pubUserRoleDTO.setUserId(id);
        pubUserRoleDTO.setRoleIds(new String[]{ItemConstant.USER_ROLE_DEFAUTL});
        userSyncFeign.addPubUserRoleKeyList(pubUserRoleDTO);
        return insert;
    }

    @Transactional
    @Override
    public int update(PersonBaseEsu personBaseEsu) {
        // 检查
//        if (!StringUtils.isEmpty(personBaseEsu.getTelephone()) && checkPhone(personBaseEsu.getTelephone())) {
//            throw new BusinessException("联系方式已存在");
//        }
        // 更新
        int update = super.update(personBaseEsu);
        //  同步euauth
        if (update < 1 || !ItemConstant.PERSON_TYPE_INSIDE.equals(personBaseEsu.getType())) {
            return update;
        }
        // 内部人员同步到euauth
        // 1 添加用户
        PubUserSync pubUserSync = new PubUserSync();
        boolean isupdate = false;
        pubUserSync.setUserId(personBaseEsu.getId());

        if (!StringUtils.isEmpty(personBaseEsu.getName())) {
            pubUserSync.setUserName(personBaseEsu.getName());
            isupdate = true;
            // 修改值班表中的名称
            rosteringInfoService.updateByPersonId(personBaseEsu.getId(), personBaseEsu.getName());
        }

        if (!StringUtils.isEmpty(personBaseEsu.getTelephone())) {
            pubUserSync.setLoginName(personBaseEsu.getTelephone());
            pubUserSync.setMobilephone(personBaseEsu.getTelephone());
            isupdate = true;

        }

        if (!StringUtils.isEmpty(personBaseEsu.getStatus())) {
            Integer isLocked;
            try {
                isLocked = Integer.parseInt(personBaseEsu.getStatus());
            } catch (NumberFormatException e) {
                isLocked = 1;
            }

            pubUserSync.setIsLocked(isLocked);
            isupdate = true;
        }


        if (!StringUtils.isEmpty(personBaseEsu.getUnitId())) {
            pubUserSync.setCorpId(personBaseEsu.getUnitId());
            isupdate = true;
        }
        if (isupdate) {
            userSyncFeign.updateSync(pubUserSync);
        }
        return update;
    }

    /**
     * 检查手机号是否存在
     *
     * @param telephone
     * @return true 存在 false 不存在
     */
    private boolean checkPhone(String telephone) {
        QueryWrapper<PersonBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telephone", telephone);
        List<PersonBase> personBases = personBaseDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(personBases)) {
            return true;
        }
        // 查询euauth是否有账号为此手机号
        ValidResponse validResponse = userSyncFeign.loginNameCheck(telephone);
        return !(Boolean) validResponse.getResult();
    }

    @Transactional
    @Override
    public int deleteById(Serializable id) {
        // 查询用户
        PersonBase personBase = personBaseDao.selectById(id);
        if (null == personBase) {
            throw new BusinessException("人员不存在");
        }
        Date nextDay = DateUtil.getNextDay(personBase.getGmtCreate(), 1);
        if (nextDay.compareTo(new Date()) < 1) {
            throw new BusinessException("只可以删除24小时内创建的人员");
        }

        int delete = super.deleteById(id);
        if (delete < 1) {
            return delete;
        }
        // 同步euauth
        PubUserSync pubUserSync = new PubUserSync();
        pubUserSync.setUserId((String) id);
        userSyncFeign.delSync(pubUserSync);
        return delete;
    }

    @Override
    public List<PersonBaseEsr> findInsideUsing() {
        QueryWrapper<PersonBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", ItemConstant.PERSON_TYPE_INSIDE);
        queryWrapper.eq("status", ItemConstant.PERSON_STATUS_USING);
        List<PersonBase> personBases = personBaseDao.selectList(queryWrapper);
        return converterPersonBaseTtoR.toList(personBases);
    }
}


