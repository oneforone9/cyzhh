package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.XjRyxxDao;
import com.essence.dao.entity.XjRyxxDto;

import com.essence.euauth.entity.PubUserRoleDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.UserSyncFeign;
import com.essence.interfaces.api.XjRyxxService;
import com.essence.interfaces.model.XjRyxQuery;
import com.essence.interfaces.model.XjRyxxEsr;
import com.essence.interfaces.model.XjRyxxEsu;
import com.essence.interfaces.param.XjRyxxEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjRyxxEtoT;
import com.essence.service.converter.ConverterXjRyxxTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 设备巡检人员信息(XjRyxx)业务层
 *
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */
@Service
public class XjRyxxServiceImpl extends BaseApiImpl<XjRyxxEsu, XjRyxxEsp, XjRyxxEsr, XjRyxxDto> implements XjRyxxService {

    @Autowired
    private XjRyxxDao xjRyxxDao;
    @Autowired
    private ConverterXjRyxxEtoT converterXjRyxxEtoT;
    @Autowired
    private ConverterXjRyxxTtoR converterXjRyxxTtoR;
    @Autowired
    private UserSyncFeign userSyncFeign;

    public XjRyxxServiceImpl(XjRyxxDao xjRyxxDao, ConverterXjRyxxEtoT converterXjRyxxEtoT, ConverterXjRyxxTtoR converterXjRyxxTtoR) {
        super(xjRyxxDao, converterXjRyxxEtoT, converterXjRyxxTtoR);
    }

    @Override
    public XjRyxxEsr updateRyxx(XjRyxxEsu xjRyxxEsu) {

        // 内部人员同步到euauth
        // 1 添加用户
        PubUserSync pubUserSync = new PubUserSync();
        boolean isupdate = false;
        pubUserSync.setUserId(xjRyxxEsu.getId());
        Integer isLocked = 0;
        pubUserSync.setIsLocked(isLocked);
        pubUserSync.setCorpId(ItemConstant.USER_XJ_PUT_DEFAUTL);
        if (!org.springframework.util.StringUtils.isEmpty(xjRyxxEsu.getName())) {
            pubUserSync.setUserName(xjRyxxEsu.getName());
            isupdate = true;
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(xjRyxxEsu.getLxfs())) {
            pubUserSync.setLoginName(xjRyxxEsu.getLxfs());
            pubUserSync.setMobilephone(xjRyxxEsu.getLxfs());
            isupdate = true;
        }
        if (!StringUtils.isEmpty(xjRyxxEsu.getBmid())) {
            pubUserSync.setCorpId(xjRyxxEsu.getBmid());
            isupdate = true;
        }
        if (isupdate) {
            userSyncFeign.updateSync(pubUserSync);
        }
        XjRyxxDto xjRyxxDto = converterXjRyxxEtoT.toBean(xjRyxxEsu);
        if (xjRyxxEsu.getLx() > 0) {
            xjRyxxDao.updateLx(0, xjRyxxDto.getBzId());
        }
        xjRyxxDao.updateById(xjRyxxDto);
        return converterXjRyxxTtoR.toBean(xjRyxxDto);
    }

    @Override
    public XjRyxxEsr addRyxx(XjRyxxEsu xjRyxxEsu) {
        //1.先增加基础表
        String id = "";
        //先判断系统登录用户
        ValidResponse validResponse = checkPhone(xjRyxxEsu.getLxfs());
        if (validResponse.getResult() != null) {
            id = validResponse.getResult().toString();
        } else {
            // 内部人员同步到euauth
            // 1 添加用户
            id = UUID.randomUUID().toString().replace("-", "");
            PubUserSync pubUserSync = new PubUserSync();
            pubUserSync.setUserId(id);
            pubUserSync.setUserName(xjRyxxEsu.getName());
            pubUserSync.setLoginName(xjRyxxEsu.getLxfs());
            pubUserSync.setPassword(ItemConstant.USER_PASSSWORD_DEFAUTL);
            Integer isLocked = 0;
            pubUserSync.setIsLocked(isLocked);
            pubUserSync.setMobilephone(xjRyxxEsu.getLxfs());
            pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);
            pubUserSync.setCreateTime(new Date());
            pubUserSync.setCorpId(ItemConstant.USER_XJ_PUT_DEFAUTL);
            userSyncFeign.addSync(pubUserSync);
            // 2 添加用户与角色关系
            PubUserRoleDTO pubUserRoleDTO = new PubUserRoleDTO();
            pubUserRoleDTO.setUserId(id);
            pubUserRoleDTO.setRoleIds(new String[]{ItemConstant.USER_XJ_ROLE_DEFAUTL});
            userSyncFeign.addPubUserRoleKeyList(pubUserRoleDTO);
        }
        xjRyxxEsu.setId(id);
        xjRyxxEsu.setTime(new Date());
        XjRyxxDto xjRyxxDto = converterXjRyxxEtoT.toBean(xjRyxxEsu);
        if (xjRyxxEsu.getLx() > 0) {
            xjRyxxDao.updateLx(0, xjRyxxDto.getBzId());
        }
        xjRyxxDao.insert(xjRyxxDto);
        return converterXjRyxxTtoR.toBean(xjRyxxDto);
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
    public List<XjRyxxEsr> searchRyxxById(XjRyxQuery xjRyxQuery) {
        List<XjRyxxEsr> list = new ArrayList<>();
        XjRyxxDto xjRyxxDto = xjRyxxDao.selectById(xjRyxQuery.getId());
        if (null != xjRyxxDto && xjRyxxDto.getLx() > 0) {
            List<XjRyxxDto> xjRyxxDtos = xjRyxxDao.selectList(new QueryWrapper<XjRyxxDto>().lambda().eq(XjRyxxDto::getBzId, xjRyxxDto.getBzId()));
            list = Optional.ofNullable(converterXjRyxxTtoR.toList(xjRyxxDtos)).orElse(new ArrayList<>());
        }
        return list;
    }

    @Override
    public List<XjRyxxEsr> searchRyxxByIds(XjRyxQuery xjRyxQuery) {
        List<XjRyxxEsr> list = new ArrayList<>();
        XjRyxxDto xjRyxxDto = xjRyxxDao.selectById(xjRyxQuery.getId());
        if (null != xjRyxxDto ) {
            List<XjRyxxDto> xjRyxxDtos = xjRyxxDao.selectList(new QueryWrapper<XjRyxxDto>().lambda().eq(XjRyxxDto::getBzId, xjRyxxDto.getBzId()));
            list = Optional.ofNullable(converterXjRyxxTtoR.toList(xjRyxxDtos)).orElse(new ArrayList<>());
        }
        return list;
    }
}
