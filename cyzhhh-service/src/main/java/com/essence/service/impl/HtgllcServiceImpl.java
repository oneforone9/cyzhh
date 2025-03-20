package com.essence.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.HtgllcDao;
import com.essence.dao.ViewOfficeBaseDao;
import com.essence.dao.entity.HtglDto;
import com.essence.dao.entity.HtgllcDto;
import com.essence.dao.entity.ViewOfficeBaseDto;
import com.essence.interfaces.api.HtgllcService;
import com.essence.interfaces.model.HtUserData;
import com.essence.interfaces.model.HtgllcEsr;
import com.essence.interfaces.model.HtgllcEsu;
import com.essence.interfaces.param.HtgllcEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtgllcEtoT;
import com.essence.service.converter.ConverterHtgllcTtoR;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 合同管理历程(Htgllc)业务层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */
@Service
public class HtgllcServiceImpl extends BaseApiImpl<HtgllcEsu, HtgllcEsp, HtgllcEsr, HtgllcDto> implements HtgllcService {
    @Autowired
    private HtgllcDao htgllcDao;
    @Autowired
    private ConverterHtgllcEtoT converterHtgllcEtoT;
    @Autowired
    private ConverterHtgllcTtoR converterHtgllcTtoR;
    @Autowired
    private ViewOfficeBaseDao viewOfficeBaseDao;

    public HtgllcServiceImpl(HtgllcDao htgllcDao, ConverterHtgllcEtoT converterHtgllcEtoT, ConverterHtgllcTtoR converterHtgllcTtoR) {
        super(htgllcDao, converterHtgllcEtoT, converterHtgllcTtoR);
    }

    private HtUserData findUserData(String userId) {
        HtUserData htUserData = new HtUserData();
        List<ViewOfficeBaseDto> viewOfficeBaseDtos = Optional.ofNullable(viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda().eq(ViewOfficeBaseDto::getUserId, userId))).orElse(Lists.newArrayList());
        if (CollectionUtil.isNotEmpty(viewOfficeBaseDtos)) {
            ViewOfficeBaseDto viewOfficeBaseDto = viewOfficeBaseDtos.get(0);
            htUserData.setName(StringUtils.isNotBlank(viewOfficeBaseDto.getJob()) ? viewOfficeBaseDto.getUserName() + "(" + viewOfficeBaseDto.getJob() + ")" : viewOfficeBaseDto.getUserName());
            htUserData.setKs(viewOfficeBaseDto.getDeptName());
            htUserData.setUseId(viewOfficeBaseDto.getUserId());
        }
        return htUserData;
    }


    @Override
    public void updateData(HtglDto htglDto) {
        if (htglDto.getSqlx() < 1) {
            //有预审得
            switch (String.valueOf(htglDto.getZt())) {
                case "1":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    HtUserData userData = findUserData(htglDto.getLsid());
                    htgllcDao.updateDataWbName(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date(), userData.getName(), userData.getKs(), userData.getUseId());
                    break;
                case "2":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "3":
                    //选择了科长需要添加流程数据
                    htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_3));
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    //添加科长
                    HtUserData userDatakz = findUserData(htglDto.getKzid());
                    HtgllcDto htgllcDto3k = findData(htglDto, ItemConstant.HT_3);
                    htgllcDto3k.setBlzt(ItemConstant.HT_LCWB);
                    htgllcDto3k.setDblrq(new Date());
                    htgllcDto3k.setJbr(userDatakz.getUseId());
                    htgllcDto3k.setJbrmc(userDatakz.getName());
                    htgllcDto3k.setJbrks(userDatakz.getKs());
                    htgllcDao.insert(htgllcDto3k);
                    break;
                case "4":
                    htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_4));
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    //添加主管领导
                    HtUserData userDataZgld = findUserData(htglDto.getZgldid());
                    HtgllcDto htgllcDtoZgld = findData(htglDto, ItemConstant.HT_4);
                    htgllcDtoZgld.setBlzt(ItemConstant.HT_LCWB);
                    htgllcDtoZgld.setDblrq(new Date());
                    htgllcDtoZgld.setJbr(userDataZgld.getUseId());
                    htgllcDtoZgld.setJbrmc(userDataZgld.getName());
                    htgllcDtoZgld.setJbrks(userDataZgld.getKs());
                    htgllcDao.insert(htgllcDtoZgld);
                    break;
                case "5":
                    if ((null != htglDto.getHtshlc() && htglDto.getHtshlc() < 2) || (null != htglDto.getSfys() && htglDto.getSfys() == 1)) {
                        htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    } else {
                        htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 3, new Date());
                    }
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "6":
                    //选择了科长需要添加流程数据
                    htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_6));
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    List<HtgllcDto> list = new ArrayList<>();
                    //添加科长
                    HtUserData userDatakz6 = findUserData(htglDto.getKzid());
                    HtgllcDto htgllcDto6K = findData(htglDto, ItemConstant.HT_6);
                    htgllcDto6K.setBlzt(ItemConstant.HT_LCWB);
                    htgllcDto6K.setDblrq(new Date());
                    htgllcDto6K.setJbr(userDatakz6.getUseId());
                    htgllcDto6K.setJbrmc(userDatakz6.getName());
                    htgllcDto6K.setJbrks(userDatakz6.getKs());
                    list.add(htgllcDto6K);
                    if (StringUtils.isNotBlank(htglDto.getKzids())) {
                        //添加组长
                        HtUserData userDataZz = findUserData(htglDto.getKzids());
                        HtgllcDto htgllcDto6z = findData(htglDto, ItemConstant.HT_6);
                        htgllcDto6z.setBlzt(ItemConstant.HT_LCWB);
                        htgllcDto6z.setDblrq(new Date());
                        htgllcDto6z.setJbr(userDataZz.getUseId());
                        htgllcDto6z.setJbrmc(userDataZz.getName());
                        htgllcDto6z.setJbrks(userDataZz.getKs());
                        list.add(htgllcDto6z);
                    }
                    if (StringUtils.isNotBlank(htglDto.getKzidss())) {
                        //添加组长
                        HtUserData userDataZz = findUserData(htglDto.getKzidss());
                        HtgllcDto htgllcDto6z = findData(htglDto, ItemConstant.HT_6);
                        htgllcDto6z.setBlzt(ItemConstant.HT_LCWB);
                        htgllcDto6z.setDblrq(new Date());
                        htgllcDto6z.setJbr(userDataZz.getUseId());
                        htgllcDto6z.setJbrmc(userDataZz.getName());
                        htgllcDto6z.setJbrks(userDataZz.getKs());
                        list.add(htgllcDto6z);
                    }
                    htgllcDao.saveData(list);
                    break;
                case "7":
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "8":
                    //办公室和财务室
                    htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_8));
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    //添加财务室
                    if (StringUtils.isNotBlank(htglDto.getCwsid())) {
                        HtUserData userDatacws = findUserData(htglDto.getCwsid());
                        HtgllcDto htgllcDtocws = findData(htglDto, ItemConstant.HT_8);
                        htgllcDtocws.setBlzt(ItemConstant.HT_LCWB);
                        htgllcDtocws.setDblrq(new Date());
                        htgllcDtocws.setJbr(userDatacws.getUseId());
                        htgllcDtocws.setJbrmc(userDatacws.getName());
                        htgllcDtocws.setJbrks(userDatacws.getKs());
                        htgllcDao.insert(htgllcDtocws);
                    }
                    //添加办公室
                    if (StringUtils.isNotBlank(htglDto.getBgsid())) {
                        HtUserData userDatakbgs = findUserData(htglDto.getBgsid());
                        HtgllcDto htgllcDtobgs = findData(htglDto, ItemConstant.HT_8B);
                        htgllcDtobgs.setBlzt(ItemConstant.HT_LCWB);
                        htgllcDtobgs.setDblrq(new Date());
                        htgllcDtobgs.setJbr(userDatakbgs.getUseId());
                        htgllcDtobgs.setJbrmc(userDatakbgs.getName());
                        htgllcDtobgs.setJbrks(userDatakbgs.getKs());
                        htgllcDao.insert(htgllcDtobgs);
                    }
                    break;
                case "9":
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "10":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    //填写主管领导数据
                    HtUserData userDatakbgs = findUserData(htglDto.getZgldid());
                    HtgllcDto htgllcDtobgs = findData(htglDto, ItemConstant.HT_10);
                    htgllcDtobgs.setBlzt(ItemConstant.HT_LCWB);
                    htgllcDtobgs.setDblrq(new Date());
                    htgllcDtobgs.setJbr(userDatakbgs.getUseId());
                    htgllcDtobgs.setJbrmc(userDatakbgs.getName());
                    htgllcDtobgs.setJbrks(userDatakbgs.getKs());
                    htgllcDao.insert(htgllcDtobgs);

                    List<HtgllcDto> htgllcDtos = Optional.ofNullable(htgllcDao.selectList(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_11))).orElse(Lists.newArrayList());
                    if (null != htglDto.getQfldlx() && htglDto.getQfldlx() > 0) {
                        htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, ItemConstant.HT_11));
                    } else if (null != htglDto.getQfldlx() && htglDto.getQfldlx() < 1 && htgllcDtos.size() < 1) {
                        //新增主要领导
                        HtgllcDto htgllcDto11 = findData(htglDto, ItemConstant.HT_11);
                        List<ViewOfficeBaseDto> jz = viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda().eq(ViewOfficeBaseDto::getJob, "局长"));
                        if (CollectionUtil.isNotEmpty(jz)) {
                            ViewOfficeBaseDto viewOfficeBaseDto = jz.get(0);
                            htgllcDto11.setJbrmc(StringUtils.isNotBlank(viewOfficeBaseDto.getJob()) ? viewOfficeBaseDto.getUserName() + "(" + viewOfficeBaseDto.getJob() + ")" : viewOfficeBaseDto.getUserName());
                            htgllcDto11.setJbrks(viewOfficeBaseDto.getDeptName());
                        }
                        htgllcDao.insert(htgllcDto11);
                    }
                    break;
                case "11":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "12":
                    if (null != htglDto.getQfldlx() && htglDto.getQfldlx() > 0) {
                        htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 2, new Date());
                    } else {
                        htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    }
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "13":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "14":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    break;
                default:
                    break;
            }
        } else {
            //其他文件
            switch (String.valueOf(htglDto.getZt())) {
                case "1":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    HtUserData userData = findUserData(htglDto.getLsid());
                    htgllcDao.updateDataWbName(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date(), userData.getName(), userData.getKs(), userData.getUseId());
                    break;
                case "2":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt() - 1, new Date());
                    htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
                    break;
                case "14":
                    htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, ItemConstant.HT_2, new Date());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void updateDataCws(HtglDto newHt, String userId, Integer zt) {
        htgllcDao.updateDataWcKs(newHt.getId(), ItemConstant.HT_LCYB, zt, new Date(), userId);
    }

    @Override
    public void saveData(HtglDto htglDto) {
        List<HtgllcDto> list = new ArrayList<>();

        List<ViewOfficeBaseDto> viewOfficeBaseDtos = viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda().eq(ViewOfficeBaseDto::getUserId, htglDto.getJbr()));
        HtgllcDto htgllcDto0 = findData(htglDto, ItemConstant.HT_0);
        HtgllcDto htgllcDto1 = findData(htglDto, ItemConstant.HT_1);
        HtgllcDto htgllcDto2 = findData(htglDto, ItemConstant.HT_2);
        HtgllcDto htgllcDto3 = findData(htglDto, ItemConstant.HT_3);
        HtgllcDto htgllcDto4 = findData(htglDto, ItemConstant.HT_4);
        HtgllcDto htgllcDto5 = findData(htglDto, ItemConstant.HT_5);
        HtgllcDto htgllcDto6 = findData(htglDto, ItemConstant.HT_6);
        HtgllcDto htgllcDto7 = findData(htglDto, ItemConstant.HT_7);
        HtgllcDto htgllcDto8 = findData(htglDto, ItemConstant.HT_8);
        HtgllcDto htgllcDto8B = findData(htglDto, ItemConstant.HT_8B);
        HtgllcDto htgllcDto9 = findData(htglDto, ItemConstant.HT_9);
        HtgllcDto htgllcDto10 = findData(htglDto, ItemConstant.HT_10);
        HtgllcDto htgllcDto11 = findData(htglDto, ItemConstant.HT_11);
        HtgllcDto htgllcDto12 = findData(htglDto, ItemConstant.HT_12);
        HtgllcDto htgllcDto13 = findData(htglDto, ItemConstant.HT_13);
        //0.申请人保存提交
        if (CollectionUtil.isNotEmpty(viewOfficeBaseDtos)) {
            ViewOfficeBaseDto viewOfficeBaseDto = viewOfficeBaseDtos.get(0);
            htgllcDto0.setJbrmc(StringUtils.isNotBlank(viewOfficeBaseDto.getJob()) ? htgllcDto0.getJbrmc() + "(" + viewOfficeBaseDto.getJob() + ")" : htgllcDto0.getJbrmc());
            htgllcDto0.setJbrks(viewOfficeBaseDto.getDeptName());
            htgllcDto0.setJbr(viewOfficeBaseDto.getUserId());
        }
        htgllcDto0.setDblrq(new Date());
        //1.律师
        if (htglDto.getZt() < 1) {
            htgllcDto0.setBlzt(ItemConstant.HT_LCWB);
        } else {
            htgllcDto0.setBlzt(ItemConstant.HT_LCYB);
            htgllcDto0.setBlwcrq(new Date());
            htgllcDto0.setMs(ItemConstant.HT_MS0);
            htgllcDto1.setBlzt(ItemConstant.HT_LCWB);
            htgllcDto1.setDblrq(htgllcDto0.getBlwcrq());

        }
        list.add(htgllcDto0);
        //填充律师得数据
        htgllcDto1.setJbrks("(局法律顾问)");
        list.add(htgllcDto1);
        //2.申请人确认
        htgllcDto2.setJbrmc(htgllcDto0.getJbrmc());
        htgllcDto2.setJbrks(htgllcDto0.getJbrks());
        htgllcDto2.setJbr(htgllcDto0.getJbr());
        list.add(htgllcDto2);
        //合同文件
        if (htglDto.getSqlx() < 1) {
            if ((null != htglDto.getHtshlc() && htglDto.getHtshlc() < 2) || (null != htglDto.getSfys() && htglDto.getSfys() == 1)) {
                //3.科长
                list.add(htgllcDto3);
                //4.主管领导
                list.add(htgllcDto4);
            }
            //5.申请人确认
            htgllcDto5.setJbrmc(htgllcDto0.getJbrmc());
            htgllcDto5.setJbrks(htgllcDto0.getJbrks());
            htgllcDto5.setJbr(htgllcDto0.getJbr());
            list.add(htgllcDto5);
            //6.科长待确认会签单
            list.add(htgllcDto6);
            //7.申请人确认
            htgllcDto7.setJbrmc(htgllcDto0.getJbrmc());
            htgllcDto7.setJbrks(htgllcDto0.getJbrks());
            htgllcDto7.setJbr(htgllcDto0.getJbr());
            list.add(htgllcDto7);
            //8.财务科确认
            htgllcDto8.setJbrks("财务科");
            list.add(htgllcDto8);
            //8.办公室确认
            htgllcDto8B.setJbrks("办公室");
            list.add(htgllcDto8B);
            //9.申请人确认
            htgllcDto9.setJbrmc(htgllcDto0.getJbrmc());
            htgllcDto9.setJbrks(htgllcDto0.getJbrks());
            htgllcDto9.setJbr(htgllcDto0.getJbr());
            list.add(htgllcDto9);
            //10.主管领导确认
            list.add(htgllcDto10);
            //11.主要领导确认
            List<ViewOfficeBaseDto> jz = viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda().eq(ViewOfficeBaseDto::getJob, "局长"));
            if (CollectionUtil.isNotEmpty(jz)) {
                ViewOfficeBaseDto viewOfficeBaseDto = jz.get(0);
                htgllcDto11.setJbrmc(StringUtils.isNotBlank(viewOfficeBaseDto.getJob()) ? viewOfficeBaseDto.getUserName() + "(" + viewOfficeBaseDto.getJob() + ")" : viewOfficeBaseDto.getUserName());
                htgllcDto11.setJbrks(viewOfficeBaseDto.getDeptName());
                htgllcDto11.setJbr(viewOfficeBaseDto.getUserId());
            }
            list.add(htgllcDto11);
            //12.办公室确认
            htgllcDto12.setJbrks("办公室");
            list.add(htgllcDto12);
            //13.申请人确认
            htgllcDto13.setJbrmc(htgllcDto0.getJbrmc());
            htgllcDto13.setJbrks(htgllcDto0.getJbrks());
            htgllcDto13.setJbr(htgllcDto0.getJbr());
            list.add(htgllcDto13);
        }
        htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()));
        htgllcDao.saveData(list);
    }

    private HtgllcDto findData(HtglDto htglDto, Integer zt) {
        HtgllcDto htgllcDto = new HtgllcDto();
        htgllcDto.setId(UUID.randomUUID().toString().replace("-", ""));
        htgllcDto.setHtid(htglDto.getId());
        htgllcDto.setZt(zt);
        htgllcDto.setJbrks(htglDto.getJbrbm());
        return htgllcDto;
    }

    @Override
    public void deleteData(List<String> ids) {
        htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().in(HtgllcDto::getHtid, ids));
    }


    @Override
    public HtglDto updateNewData(HtglDto htglDto, HtglDto oldVO) {
        if (htglDto.getZt() != 4) {
            htglDto = transferRy(htglDto, oldVO);
            htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt()-1, new Date());
            htgllcDao.updateDataWb(htglDto.getId(), ItemConstant.HT_LCWB, htglDto.getZt(), new Date());
        } else {
            htgllcDao.updateDataWc(htglDto.getId(), ItemConstant.HT_LCYB, htglDto.getZt()-1, new Date());
        }
        return htglDto;
    }

    private HtglDto transferRy(HtglDto htglDto, HtglDto oldVO) {
        List<HtgllcDto> htgllcDtos = htgllcDao.selectList(new QueryWrapper<HtgllcDto>().lambda()
                .eq(HtgllcDto::getHtid, htglDto.getId()).eq(HtgllcDto::getZt, htglDto.getZt()));

        if(CollectionUtil.isNotEmpty(htgllcDtos)){
            HtgllcDto htgllcDto = htgllcDtos.get(0);
            htglDto.setXyczry(htgllcDto.getJbr());
            htglDto.setXyczrymc(htgllcDto.getJbrmc());
        }
        htglDto.setDqry(oldVO.getXyczry());
        htglDto.setDqrymc(oldVO.getXyczrymc());
        return htglDto;
    }

    @Override
    public HtglDto saveNewData(HtglDto htglDto) {
        List<HtgllcDto> list = new ArrayList<>();
        ViewOfficeBaseDto baseDto = findHtUserByKs(htglDto.getQdks(),htglDto.getQdksId());
        if (baseDto == null) {
            throw new BusinessException("签订科室，没有对应的合同人员，请维护该科室合同人员之后，再新增合同");
        }
        // 后期规则若多，可增加个字段维护
        // 水印改成按照选择科室给予水印，选择的是局机关的科室，水印为北京市朝阳区水务局；选择科室为基层单位，水印为基层单位全称
        if("xuni".equals(htglDto.getQdksId())){
            htglDto.setSymc("北京市朝阳区水务建设管理中心");
        }else if(baseDto.getSfJjg()!= null && baseDto.getSfJjg() == 1){
            htglDto.setSymc("北京市朝阳区水务局");
        }else{
            htglDto.setSymc("北京市朝阳" + baseDto.getDepartmentName());
        }
        htglDto.setLrsj(new Date());
        htglDto.setDqry(htglDto.getJbr());
        htglDto.setDqrymc(htglDto.getJbrmc());
        htglDto.setXyczry(htglDto.getJbr());
        htglDto.setXyczrymc(htglDto.getJbrmc());
        // 申请人
        HtgllcDto htgllcDto11 = findData(htglDto, ItemConstant.HT__1);
        htgllcDto11.setBlzt(1);
        htgllcDto11.setDblrq(htglDto.getLrsj());
        htgllcDto11.setJbr(htglDto.getJbr());
        htgllcDto11.setJbrmc(htglDto.getJbrmc());
        list.add(htgllcDto11);
        // 申请人
        HtgllcDto htgllcDto0 = findData(htglDto, ItemConstant.HT_0);
        htgllcDto0.setBlzt(0);
        htgllcDto0.setJbr(htglDto.getJbr());
        htgllcDto0.setJbrmc(htglDto.getJbrmc());
        list.add(htgllcDto0);
        // 合同人
        HtgllcDto htgllcDto1 = findData(htglDto, ItemConstant.HT_1);
        htgllcDto1.setBlzt(0);
        htgllcDto1.setJbr(baseDto.getUserId());
        htgllcDto1.setJbrmc(baseDto.getUserName());
        htgllcDto1.setJbrks(baseDto.getDeptName());
        list.add(htgllcDto1);
        // 申请人
        HtgllcDto htgllcDto2 = findData(htglDto, ItemConstant.HT_2);
        htgllcDto2.setBlzt(0);
        htgllcDto2.setJbr(htglDto.getJbr());
        htgllcDto2.setJbrmc(htglDto.getJbrmc());
        list.add(htgllcDto2);
        // 合同人 改成 申请人
        HtgllcDto htgllcDto3 = findData(htglDto, ItemConstant.HT_3);
        htgllcDto3.setBlzt(0);
        htgllcDto0.setJbr(htglDto.getJbr());
        htgllcDto0.setJbrmc(htglDto.getJbrmc());
        list.add(htgllcDto3);

        htgllcDao.delete(new QueryWrapper<HtgllcDto>().lambda().eq(HtgllcDto::getHtid, htglDto.getId()));
        htgllcDao.saveData(list);
        return htglDto;
    }
    

    private ViewOfficeBaseDto findHtUserByKs(String ksName,String ksId) {
        List<ViewOfficeBaseDto> viewOfficeBaseDtos = null;
        // xuni 为虚拟科室，直接找办公室
        if(!"xuni".equals(ksId)){
            viewOfficeBaseDtos = Optional.ofNullable(viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda()
                            .eq(ViewOfficeBaseDto::getSfJjg, 0)
                            .eq(ViewOfficeBaseDto::getIsHtGly, 1)
                            .and(j -> j.eq(ViewOfficeBaseDto::getDeptName, ksName).or().eq(ViewOfficeBaseDto::getDepartmentName, ksName))))
                    .orElse(Lists.newArrayList());
        }
        ViewOfficeBaseDto viewOfficeBaseDto = null;
        if (CollectionUtil.isNotEmpty(viewOfficeBaseDtos)) {
            viewOfficeBaseDto = viewOfficeBaseDtos.get(0);
        }else{
            //针对科室 ，没有找到合同人员，找办公室
            List<ViewOfficeBaseDto> baseDtoList = viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda()
                    .eq(ViewOfficeBaseDto::getDeptName, "办公室")
                    .eq(ViewOfficeBaseDto::getSfJjg, 1)
                    .eq(ViewOfficeBaseDto::getIsHtGly, 1));
            if(CollectionUtil.isNotEmpty(baseDtoList)){
                viewOfficeBaseDto = baseDtoList.get(0);
            }
        }
        return viewOfficeBaseDto;
    }


}
