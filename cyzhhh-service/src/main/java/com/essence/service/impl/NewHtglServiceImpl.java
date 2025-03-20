package com.essence.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.api.HtglfjService;
import com.essence.interfaces.api.HtgllcService;
import com.essence.interfaces.api.NewHtglService;
import com.essence.interfaces.model.HtglEsr;
import com.essence.interfaces.model.HtglEsu;
import com.essence.interfaces.model.HtglEsuData;
import com.essence.interfaces.param.HtglEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtglEtoT;
import com.essence.service.converter.ConverterHtglTtoR;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 合同管理(Htgl)业务层
 *
 * @author majunjie
 * @since 2024-09-09 17:45:30
 */
@Service
@Log4j2
public class NewHtglServiceImpl extends BaseApiImpl<HtglEsu, HtglEsp, HtglEsr, HtglDto> implements NewHtglService {

    @Autowired
    private HtglDao htglDao;
    @Autowired
    private HtgllcDao htgllcDao;
    @Autowired
    private ConverterHtglEtoT converterHtglEtoT;
    @Autowired
    private ConverterHtglTtoR converterHtglTtoR;
    @Autowired
    private HtglfjService htglfjService;
    @Autowired
    private HtglfjDao htglfjDao;

    @Autowired
    private HtgllcService htgllcService;

    @Autowired
    private FileBaseService fileBaseService;

    @Autowired
    private HtglhqdDao htglhqdDao;

    @Autowired
    private StOfficeBaseDao stOfficeBaseDao;

    public NewHtglServiceImpl(HtglDao htglDao, ConverterHtglEtoT converterHtglEtoT, ConverterHtglTtoR converterHtglTtoR) {
        super(htglDao, converterHtglEtoT, converterHtglTtoR);
    }


    @Override
    public HtglEsr searchHqdById(String id) {
        HtglDto htglDto = new HtglDto();
        HtglhqdDto htglhqdDto = htglhqdDao.selectById(id);
        if (htglhqdDto != null) {
            BeanUtils.copyProperties(htglhqdDto, htglDto);
        } else {
            htglDto = htglDao.selectById(id);
        }
        return converterHtglTtoR.toBean(htglDto);
    }

    @Override
    public HtglEsr addHt(HtglEsuData htglEsuData) {
        HtglDto newHt = converterHtglEtoT.toBean(htglEsuData.getHtglEsu());
        // 判断合同编号是否重复
        checkHtbh(newHt);
        if (StringUtils.isNotBlank(newHt.getId())) {
            HtglDto oldHt = htglDao.selectById(newHt.getId());
            // 提交合同了
            if (!oldHt.getZt().equals(newHt.getZt()) && oldHt.getLy() < 1) {
                newHt = htgllcService.updateNewData(newHt, oldHt);
                if (newHt.getZt() == 2) {
                    // 合同增加 水印
                    String fileId = getFileId(newHt.getId());
                    if (StringUtils.isNotBlank(fileId) && StringUtils.isNotBlank(oldHt.getSymc())) {
                        fileBaseService.makeWaterMark(fileId, oldHt.getSymc(),newHt.getHtbh());
                    }
                }
            }
            // 修改合同会签单
            if (oldHt.getZt().equals(ItemConstant.HT__1)) {
                updateHtglhqd(newHt);
            }
            htglDao.updateById(newHt);
        } else {
            // 新增合同
            newHt.setId(UUID.randomUUID().toString().replace("-", ""));
            newHt.setZt(ItemConstant.HT__1);
            // 合同历程
            newHt = htgllcService.saveNewData(newHt);
            htglDao.insert(newHt);
            // 新增合同会签单
            saveHtglhqd(newHt);
        }
        //添加附件
        if (CollectionUtil.isNotEmpty(htglEsuData.getFjData())) {
            htglfjService.saveFjData(htglEsuData.getFjData(), newHt.getId());
        }
        return converterHtglTtoR.toBean(newHt);
    }

    /**
     * 校验合同编号是否重复
     *
     * @param newHt
     */
    private void checkHtbh(HtglDto newHt) {
        if (StringUtils.isNotBlank(newHt.getHtbh())) {
            LambdaQueryWrapper<HtglDto> checkHtbh = new QueryWrapper<HtglDto>().lambda().eq(HtglDto::getHtbh, newHt.getHtbh());
            if (StringUtils.isNotBlank(newHt.getId())) {
                checkHtbh.ne(HtglDto::getId, newHt.getId());
            }
            List<HtglDto> htgls = htglDao.selectList(checkHtbh);
            if (CollectionUtil.isNotEmpty(htgls)) {
                throw new BusinessException("合同编号重复，请更换合同编号");
            }
        }
    }


    /**
     * 新增 合同会签单
     *
     * @param htglDto
     */
    private void saveHtglhqd(HtglDto htglDto) {
        HtglhqdDto htglhqdDto = new HtglhqdDto();
        BeanUtils.copyProperties(htglDto, htglhqdDto);
        htglhqdDao.insert(htglhqdDto);
    }


    /**
     * 修改 合同会签单
     *
     * @param htglDto
     */
    private void updateHtglhqd(HtglDto htglDto) {
        HtglhqdDto htglhqdDto = new HtglhqdDto();
        BeanUtils.copyProperties(htglDto, htglhqdDto);
        htglhqdDao.updateById(htglhqdDto);
    }

    private String getFileId(String htid) {
        String id = null;
        List<HtglfjDto> htglfjDtos = htglfjDao.selectList(new QueryWrapper<HtglfjDto>().lambda().eq(HtglfjDto::getHtid, htid).eq(HtglfjDto::getLx, 0));
        if (CollectionUtil.isNotEmpty(htglfjDtos)) {
            String fileid = htglfjDtos.get(0).getFileid();
            JSONObject jsonObject = new JSONObject(fileid);
            id = jsonObject.getString("id");
        }
        return id;
    }

    @Override
    public int returnPrevious(String userId, List<String> returnList) {
        for (String htid : returnList) {
            HtglDto htglDto = htglDao.selectById(htid);
            if (htglDto == null || htglDto.getZt() < 0 || !userId.equals(htglDto.getDqry())) {
                continue;
            }
            returnPrevious(htglDto);
        }
        return 1;
    }

    @Override
    public int backPrevious(HtglEsu esu) {
        int result = 0;
        HtglDto htglDto = htglDao.selectById(esu.getId());
        if (htglDto == null || !esu.getXyczry().equals(htglDto.getXyczry()) || htglDto.getZt() == 0) {
            return result;
        }
        returnPrevious(htglDto);
        htglDto.setHtyj(esu.getHtyj());
        htglDao.updateById(htglDto);
        result = 1;
        return result;
    }

    /**
     * 根据合同主键，返回上一个步骤
     *
     * @param htglDto
     */
    private void returnPrevious(HtglDto htglDto) {
        List<HtgllcDto> htgllcDtos = htgllcDao.selectList(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()));

        if (CollectionUtil.isNotEmpty(htgllcDtos)) {
            // 更新子表
            List<HtgllcDto> currentLc = htgllcDtos.stream().filter(item -> item.getZt() == htglDto.getZt()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(currentLc)) {
                HtgllcDto htgllcDto = currentLc.get(0);
                htgllcDto.setBlzt(0);
                htgllcDto.setDblrq(null);

                LambdaUpdateWrapper<HtgllcDto> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(HtgllcDto::getId, htgllcDto.getId()).set(HtgllcDto::getBlzt, htgllcDto.getBlzt()).set(HtgllcDto::getDblrq, htgllcDto.getDblrq());
                htgllcDao.update(null, updateWrapper);
            }
            List<HtgllcDto> previousLc = htgllcDtos.stream().filter(item -> item.getZt() == htglDto.getZt() - 1).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(previousLc)) {
                HtgllcDto htgllcDto = previousLc.get(0);
                htgllcDto.setBlzt(0);
                htgllcDto.setBlwcrq(null);

                LambdaUpdateWrapper<HtgllcDto> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(HtgllcDto::getId, htgllcDto.getId()).set(HtgllcDto::getBlzt, htgllcDto.getBlzt()).set(HtgllcDto::getBlwcrq, htgllcDto.getBlwcrq());
                htgllcDao.update(null, updateWrapper);
            }
            htglDto.setZt(htglDto.getZt() - 1);
            if (htglDto.getZt() > -1) {
                List<HtgllcDto> current = htgllcDtos.stream().filter(item -> item.getZt() == htglDto.getZt()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(current)) {
                    HtgllcDto htgllcDto = current.get(0);
                    htglDto.setXyczrymc(htgllcDto.getJbrmc());
                    htglDto.setXyczry(htgllcDto.getJbr());
                }
                List<HtgllcDto> previous = htgllcDtos.stream().filter(item -> item.getZt() == htglDto.getZt() - 1).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(previous)) {
                    HtgllcDto htgllcDto = previous.get(0);
                    htglDto.setDqrymc(htgllcDto.getJbrmc());
                    htglDto.setDqry(htgllcDto.getJbr());
                }
            }
            htglDao.updateById(htglDto);
        }
    }


    @Override
    public boolean isJjg(String ksName, String ksId) {
        boolean isJjg = false;
        // 虚拟科室，属于局机关
        if ("xuni".equals(ksId)) {
            return true;
        }
        // 非虚拟科室，监管中心；不属于局机关
        if ("建管中心".equals(ksName)) {
            return false;
        }
        // 局机关，属于局机关
        List<StOfficeBaseDto> stOfficeBaseDtos = stOfficeBaseDao.selectList(new QueryWrapper<StOfficeBaseDto>().lambda().eq(StOfficeBaseDto::getSfJjg, 1).and(j -> j.eq(StOfficeBaseDto::getDeptName, ksName).or().eq(StOfficeBaseDto::getDepartmentName, ksName)));
        if (CollectionUtil.isNotEmpty(stOfficeBaseDtos)) {
            isJjg = true;
        }
        return isJjg;
    }

    @Override
    public Boolean judgeIsHaveWts(String htId) {
        Boolean isHaveWts = false;
        HtglDto htglDto = htglDao.selectById(htId);
        // 走局里流程，需要授权委托书 （建管中心，排查在外）
        if (htglDto != null && !"建管中心".equals(htglDto.getQdks())) {
            // 签订科室，是否局机关
            isHaveWts = isJjg(htglDto.getQdks(), htglDto.getQdksId());
        }
        return isHaveWts;
    }

}
