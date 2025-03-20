package com.essence.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.HtglService;
import com.essence.interfaces.api.HtglfjService;
import com.essence.interfaces.api.HtgllcService;
import com.essence.interfaces.api.HtglysxmService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.HtglEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtglEtoT;
import com.essence.service.converter.ConverterHtglTtoR;
import com.essence.service.impl.listener.HtListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合同管理(Htgl)业务层
 *
 * @author majunjie
 * @since 2024-09-09 17:45:30
 */
@Service
@Log4j2
public class HtglServiceImpl extends BaseApiImpl<HtglEsu, HtglEsp, HtglEsr, HtglDto> implements HtglService {

    @Autowired
    private HtglDao htglDao;
    @Autowired
    private ConverterHtglEtoT converterHtglEtoT;
    @Autowired
    private ConverterHtglTtoR converterHtglTtoR;
    @Autowired
    private HtglfjService htglfjService;
    @Autowired
    private HtglysxmService htglysxmService;
    @Autowired
    private HtgllcService htgllcService;
    @Autowired
    private HtglbhDao htglbhDao;
    @Autowired
    private HtglscDao htglscDao;
    @Autowired
    private ViewOfficeBaseDao viewOfficeBaseDao;

    @Autowired
    private HtglhqdDao htglhqdDao;


    public HtglServiceImpl(HtglDao htglDao, ConverterHtglEtoT converterHtglEtoT, ConverterHtglTtoR converterHtglTtoR) {
        super(htglDao, converterHtglEtoT, converterHtglTtoR);
    }

    private Date findDateD(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String year = time.substring(0, 4);
            String month = time.substring(time.indexOf(".") + 1, time.lastIndexOf("."));
            if (Integer.valueOf(month) < 10) {
                month = "0" + month;
            }
            String day = time.substring(time.lastIndexOf(".") + 1, time.length());
            if (Integer.valueOf(day) < 10) {
                day = "0" + day;
            }
            return sdf.parse(year + "-" + month + "-" + day + " 00:00:00");
        } catch (Exception e) {
            log.error("导入计划日期异常" + e + time);
            return null;
        }
    }

    private Date findDateY(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String year = time.substring(0, 4);
            String month = time.substring(time.indexOf("年") + 1, time.lastIndexOf("月"));
            if (Integer.valueOf(month) < 10) {
                month = "0" + month;
            }
            String day = time.substring(time.lastIndexOf("月") + 1, time.length() - 1);
            if (Integer.valueOf(day) < 10) {
                day = "0" + day;
            }
            return sdf.parse(year + "-" + month + "-" + day + " 00:00:00");
        } catch (Exception e) {
            log.error("导入计划日期异常" + e + time);
            return null;
        }
    }

    @Override
    public String deleteHt(HtDel htDel) {
        String type = "成功";
        try {
            htglDao.delete(new QueryWrapper<HtglDto>().lambda().in(HtglDto::getId, htDel.getIds()));
            //会签单（合同草稿）
            htglhqdDao.delete(new QueryWrapper<HtglhqdDto>().lambda().in(HtglhqdDto::getId, htDel.getIds()));
            //历程
            htgllcService.deleteData(htDel.getIds());
            //附件
            htglfjService.deleteData(htDel.getIds());
//            //项目内容
//            htglysxmService.deleteData(htDel.getIds());
//            //收藏
//            htglscDao.delete(new QueryWrapper<HtglscDto>().lambda().in(HtglscDto::getHtid, htDel.getIds()));
        } catch (Exception e) {
            type = "";
            log.error("合同删除失败" + e);
        }
        return type;
    }

    @Override
    public String importHt(MultipartFile file, String userId) {
        String type = "";
        try {
            List<HtglDto> htglDtoList = new ArrayList<>();
            HtListener htListener = new HtListener();
            EasyExcel.read(file.getInputStream(), HtglExcept.class, htListener).headRowNumber(4)  // 设置表头占用的行数
                    .sheet().doRead();
            List<HtglExcept> dataList = htListener.getRecords();

            HtListener htListener2 = new HtListener();
            EasyExcel.read(file.getInputStream(), HtglExcept.class, htListener2).headRowNumber(5)  // 设置表头占用的行数
                    .sheet().doRead();
            List<HtglExcept> dataList2 = htListener2.getRecords();

            if (CollectionUtil.isNotEmpty(dataList)) {
                int i = 0;
                for (HtglExcept htglExcept : dataList) {
                    if (i == 0) {
                        continue;
                    }
                    HtglExcept htglExcept1 = dataList2.get(i);
                    i++;
                    HtglDto htglDto = new HtglDto();
                    htglDto.setId(UUID.randomUUID().toString().replace("-", ""));
                    htglDto.setHtbh(htglExcept.getHtbh());
                    htglDto.setHtmc(htglExcept.getHtmc());
                    htglDto.setHytlqk(htglExcept.getHytlqk());
                    htglDto.setShrq(htglExcept.getShrq());
                    htglDto.setShnr(htglExcept.getShnr());
                    htglDto.setHtnr(htglExcept.getHtnr());

                    htglDto.setHtjf(htglExcept1.getHtjf());
                    htglDto.setHtyf(htglExcept1.getHtyf());
                    htglDto.setQtf(htglExcept1.getQtf());
                    htglDto.setQszxrq(htglExcept1.getQszxrq());
                    htglDto.setZzzxrq(htglExcept1.getZzzxrq());

                    htglDto.setHtje(htglExcept.getHtje());
                    htglDto.setHtqdrq(htglExcept.getHtqdrq());
                    htglDto.setTdsj(new Date());
                    htglDto.setSrklx(2);
                    htglDto.setSrklxSh(2);

                    htglDto.setQdks(htglExcept.getQdks());
                    htglDto.setJbrmc(htglExcept.getJbrmc());
                    List<ViewOfficeBaseDto> viewOfficeBaseDtos = viewOfficeBaseDao.selectList(new QueryWrapper<ViewOfficeBaseDto>().lambda().eq(ViewOfficeBaseDto::getUserName, htglDto.getJbrmc()).eq(ViewOfficeBaseDto::getDeptName, htglDto.getJbrbm()));
                    if (CollectionUtil.isNotEmpty(viewOfficeBaseDtos)) {
                        htglDto.setJbr(viewOfficeBaseDtos.get(0).getUserId());
                    } else {
                        htglDto.setJbr(userId);
                    }
                    htglDto.setZt(4);
                    htglDto.setJbrbm(htglExcept.getQdks());
                    // 来源0自动1手动导入
                    htglDto.setLy(1);
                    htglDto.setHtsmjsczt(0);
                    htglDto.setHtshlc(0);
                    htglDto.setLrsj(new Date());
                    htglDtoList.add(htglDto);
                }
            }
            if (CollectionUtil.isNotEmpty(htglDtoList)) {
                List<List<HtglDto>> f = Lists.partition(htglDtoList, 3000);
                List<List<HtglDto>> list = f.parallelStream().peek(x -> {
                    htglDao.saveData(x);
                }).collect(Collectors.toList());
            }
            type = "成功";
        } catch (Exception e) {
            log.error("合同导入失败", e);
        }
        return type;
    }


    @Override
    public Paginator<HtglfjEsr> htfjSearch(PaginatorParam param) {
        return htglfjService.findByPaginator(param);
    }

    @Override
    public Paginator<HtglysxmEsr> htysxmSearch(PaginatorParam param) {
        return htglysxmService.findByPaginator(param);
    }

    @Override
    public Paginator<HtgllcEsr> htlxSearch(PaginatorParam param) {
        return htgllcService.findByPaginator(param);
    }

    public String findHtbh(String htbh) {
        if (StringUtils.isNotBlank(htbh)) {
            String type = htbh.substring(htbh.indexOf("-") + 1, htbh.lastIndexOf("-"));
            String year = htbh.substring(htbh.lastIndexOf("-") + 1, htbh.length());
            if (year.length() < 5) {
                //添加编号
                htbh = findHtbhStr(htbh, type);
            }
        }
        return htbh;
    }

    public synchronized String findHtbhStr(String htbh, String type) {
        HtglbhDto htglbhDto = htglbhDao.selectById(type);
        if (null != htglbhDto) {
            Integer xh = htglbhDto.getXh() + 1;
            htglbhDto.setXh(xh);
            htglbhDao.updateById(htglbhDto);
            String xhStr = xh > 99 ? String.valueOf(xh) : (xh > 9 ? "0" + String.valueOf(xh) : "00" + String.valueOf(xh));
            htbh = htbh + xhStr;
        } else {
            HtglbhDto htglbhDtos = new HtglbhDto();
            htglbhDtos.setXh(1);
            htglbhDto.setLx(type);
            htglbhDao.insert(htglbhDto);
            htbh = htbh + "001";
        }
        return htbh;
    }

    @Override
    public HtglEsr addHtByTz(HtglEsu htglEsu) {
        HtglDto htglDto = converterHtglEtoT.toBean(htglEsu);
        htglDto.setId(UUID.randomUUID().toString().replace("-", ""));
        htglDto.setLy(1);
        htglDao.insert(htglDto);
        return converterHtglTtoR.toBean(htglDto);
    }

    @Override
    public HtglEsr addHt(HtglEsuData htglEsuData) {
        HtglDto newHt = converterHtglEtoT.toBean(htglEsuData.getHtglEsu());
        if (StringUtils.isNotBlank(newHt.getId())) {
            HtglDto oldHt = htglDao.selectById(newHt.getId());
            if (!String.valueOf(oldHt.getZt()).equals(newHt.getZt()) && newHt.getLy() < 1) {
                if (oldHt.getZt() > newHt.getZt() && newHt.getZt() == ItemConstant.HT_5) {
                    newHt.setKeshqr(0);
                    newHt.setKeshqrs(0);
                    newHt.setKeshqrss(0);
                    newHt.setBgshqqr(0);
                    newHt.setCwshqqr(0);
                }
                switch (String.valueOf(newHt.getZt())) {
                    case "5":
                        newHt.setTdsjhq(new Date());
                        break;
                    case "4":
                        newHt.setTdslys(new Date());
                    case "13":
                        newHt.setHtbh(findHtbh(newHt.getHtbh()));
                    default:
                        break;
                }
                htgllcService.updateData(newHt);
            }
            htglDao.updateById(newHt);
        } else {
            newHt.setId(UUID.randomUUID().toString().replace("-", ""));
            htglDao.insert(newHt);
        }
        //添加附件
        if (CollectionUtil.isNotEmpty(htglEsuData.getFjData())) {
            htglfjService.saveFjData(htglEsuData.getFjData(), newHt.getId());
        }
        //添加预审项目内容
        if (CollectionUtil.isNotEmpty(htglEsuData.getYsxmData())) {
            htglysxmService.saveYsxnData(htglEsuData.getYsxmData(), newHt.getId());
        }

        return converterHtglTtoR.toBean(newHt);
    }

    @Override
    public HtglEsr updateHqdKz(HtglEsuDatas htglEsuDatas) {
        HtglDto newHt = converterHtglEtoT.toBean(htglEsuDatas.getHtglEsu());

        if (newHt.getKzid().equals(htglEsuDatas.getUserId())) {
            //科长1
            newHt.setKeshqr(1);
            htgllcService.updateDataCws(newHt, htglEsuDatas.getUserId(), ItemConstant.HT_6);
            if (newHt.getKeshqrs() == 1 && newHt.getKeshqrss() == 1) {
                newHt.setZt(ItemConstant.HT_7);
                htgllcService.updateData(newHt);
            }
        } else if (newHt.getKzids().equals(htglEsuDatas.getUserId())) {
            //科长1
            newHt.setKeshqrs(1);
            htgllcService.updateDataCws(newHt, htglEsuDatas.getUserId(), ItemConstant.HT_6);
            if (newHt.getKeshqr() == 1 && newHt.getKeshqrss() == 1) {
                newHt.setZt(ItemConstant.HT_7);
                htgllcService.updateData(newHt);
            }
        } else if (newHt.getKzidss().equals(htglEsuDatas.getUserId())) {
            //科长1
            newHt.setKeshqrss(1);
            htgllcService.updateDataCws(newHt, htglEsuDatas.getUserId(), ItemConstant.HT_6);
            if (newHt.getKeshqrs() == 1 && newHt.getKeshqrss() == 1) {
                newHt.setZt(ItemConstant.HT_7);
                htgllcService.updateData(newHt);
            }
        }
        htglDao.updateById(newHt);
        return converterHtglTtoR.toBean(newHt);
    }

    @Override
    public HtglEsr updateHqdks(HtglEsuDatas htglEsuDatas) {
        HtglDto newHt = converterHtglEtoT.toBean(htglEsuDatas.getHtglEsu());
        if (newHt.getCwsid().equals(htglEsuDatas.getUserId())) {
            //财务室
            newHt.setCwshqqr(1);
            htgllcService.updateDataCws(newHt, htglEsuDatas.getUserId(), ItemConstant.HT_8);
            if (newHt.getBgshqqr() == 1) {
                newHt.setZt(ItemConstant.HT_9);
                htgllcService.updateData(newHt);
            }
        } else {
            //办公室
            newHt.setBgshqqr(1);
            htgllcService.updateDataCws(newHt, htglEsuDatas.getUserId(), ItemConstant.HT_8);
            if (newHt.getCwshqqr() == 1) {
                newHt.setZt(ItemConstant.HT_9);
                htgllcService.updateData(newHt);
            }
        }
        htglDao.updateById(newHt);
        return converterHtglTtoR.toBean(newHt);
    }

    @Override
    public HtglEsr addHtHsz(Hthsz hthsz) {
        HtglDto htglDto = htglDao.selectById(hthsz.getId());
        htglDto.setHsz(1);
        htglDao.updateById(htglDto);
        return converterHtglTtoR.toBean(htglDto);
    }

    @Override
    public HtglEsr addHtGq(Hthsz hthsz) {
        HtglDto htglDto = htglDao.selectById(hthsz.getId());
        htglDto.setGq(1);
        htglDao.updateById(htglDto);
        return converterHtglTtoR.toBean(htglDto);
    }

    @Override
    public HtglEsr addHtSc(Hthsz hthsz) {
        List<HtglscDto> htglscDtos = Optional.ofNullable(htglscDao.selectList(new QueryWrapper<HtglscDto>().lambda().eq(HtglscDto::getHtid, hthsz.getId()).eq(HtglscDto::getUserId, hthsz.getUserId()))).orElse(Lists.newArrayList());
        if (CollectionUtil.isEmpty(htglscDtos)) {
            HtglscDto htglscDto = new HtglscDto();
            htglscDto.setId(UUID.randomUUID().toString().replace("-", ""));
            htglscDto.setHtid(hthsz.getId());
            htglscDto.setUserId(hthsz.getUserId());
            htglscDao.insert(htglscDto);
        }
        return null;
    }

    @Override
    public HtglEsr addHtScd(HtglScd htglScd) {
        HtglDto htglDto = new HtglDto();
        if (StringUtils.isNotBlank(htglScd.getId())) {
            htglDto = htglDao.selectById(htglScd.getId());
            htglDto.setSqlx(htglScd.getSqlx());
            htglDto.setHtmc(htglScd.getHtmc());
            htglDto.setHtnr(htglScd.getHtnr());
            htglDto.setHtjf(htglScd.getHtjf());
            htglDto.setHtyf(htglScd.getHtyf());
            htglDto.setHtshlc(htglScd.getHtshlc());
            htglDto.setQtf(htglScd.getQtf());
            htglDto.setHtje(htglScd.getHtje());
            htglDto.setQdks(htglScd.getQdks());
            htglDto.setJbr(htglScd.getJbr());
            htglDto.setJbrmc(htglScd.getJbrmc());

            htglDto.setJbrbm(htglScd.getJbrbm());
            htglDto.setFlgwsc(htglScd.getFlgwsc());
            htglDto.setTdrqrscd(htglScd.getTdrqrscd());
            htglDto.setQt(htglScd.getQt());
            htglDto.setZt(htglScd.getZt());

            htglDao.updateById(htglDto);
        } else {
            htglDto.setId(UUID.randomUUID().toString().replace("-", ""));
            htglDto.setSqlx(htglScd.getSqlx());
            htglDto.setHtmc(htglScd.getHtmc());
            htglDto.setHtnr(htglScd.getHtnr());
            htglDto.setHtjf(htglScd.getHtjf());
            htglDto.setHtyf(htglScd.getHtyf());
            htglDto.setQtf(htglScd.getQtf());
            htglDto.setHtje(htglScd.getHtje());
            htglDto.setQdks(htglScd.getQdks());
            htglDto.setJbr(htglScd.getJbr());
            htglDto.setHtshlc(htglScd.getHtshlc());
            htglDto.setJbrmc(htglScd.getJbrmc());
            htglDto.setJbrbm(htglScd.getJbrbm());
            htglDto.setFlgwsc(htglScd.getFlgwsc());
            htglDto.setTdrqrscd(htglScd.getTdrqrscd());
            htglDto.setQt(htglScd.getQt());
            htglDto.setZt(htglScd.getZt());
            htglDto.setTdsj(new Date());
            htglDto.setTh(0);
            htglDto.setLy(0);
            htglDto.setCwshqqr(0);
            htglDto.setBgshqqr(0);
            htglDto.setKeshqr(0);
            htglDto.setKeshqrs(0);
            htglDto.setKeshqrss(0);
            htglDto.setHsz(0);
            htglDao.insert(htglDto);
        }
        //添加历程
        htgllcService.saveData(htglDto);
        //添加附件
        if (CollectionUtil.isNotEmpty(htglScd.getFjData())) {
            htglfjService.saveFjData(htglScd.getFjData(), htglDto.getId());
        }
        return converterHtglTtoR.toBean(htglDto);
    }

    private static Integer findStrCount(String str, String ch) {
        Pattern pattern = Pattern.compile(ch);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        String s = "abcd-12345-qwer";
        String ss = "-";
        System.out.println(findStrCount(s, ss));
        if (findStrCount(s, ss) > 2) {
            //截取3个
            System.out.println(s.substring(0, s.lastIndexOf("-")));
        } else {
            //截取2个
            System.out.println(s.substring(0, s.lastIndexOf("-") + 4));
        }
    }

    @Override
    public String importHtFj(HtglFjImports htglFjImports) {
        Map<String, List<String>> htbhMap = new HashMap<>();
        String type = "";
        try {
            String ch = "-";
            if (CollectionUtil.isNotEmpty(htglFjImports.getHtglFjImports())) {
                for (HtglFjImport htglFjImport : htglFjImports.getHtglFjImports()) {
                    String htbh = "";
                    if (findStrCount(htglFjImport.getWjmc(), ch) > 2) {
                        //截取3个
                        htbh = htglFjImport.getWjmc().substring(0, htglFjImport.getWjmc().lastIndexOf("-"));
                    } else {
                        //截取2个
                        htbh = htglFjImport.getWjmc().substring(0, htglFjImport.getWjmc().lastIndexOf("-") + 4);
                    }
                    List<String> list = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(htbhMap.get(htbh))) {
                        list = htbhMap.get(htbh);
                    }
                    list.add(htglFjImport.getId());
                    htbhMap.put(htbh, list);
                }
                if (CollectionUtil.isNotEmpty(htbhMap)) {
                    Set<String> htbhData = htbhMap.keySet();
                    List<HtglDto> htglDtoList = Optional.ofNullable(htglDao.selectList(new QueryWrapper<HtglDto>().lambda().in(HtglDto::getHtbh, htbhData))).orElse(Lists.newArrayList());
                    if (CollectionUtil.isNotEmpty(htglDtoList)) {
                        Map<String, String> map = Optional.ofNullable(htglDtoList.stream().collect(Collectors.toMap(HtglDto::getHtbh, HtglDto::getId, (o1, o2) -> o2))).orElse(Maps.newHashMap());
                        List<HtglfjEsu> fjData = new ArrayList<>();
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            List<String> list = htbhMap.get(entry.getKey());
                            for (String fileStr : list) {
                                HtglfjEsu htglfjEsu = new HtglfjEsu();
                                htglfjEsu.setId(UUID.randomUUID().toString().replace("-", ""));
                                htglfjEsu.setHtid(entry.getValue());
                                htglfjEsu.setLx(1);
                                htglfjEsu.setFileid(fileStr);
                                htglfjEsu.setTjsj(new Date());
                                fjData.add(htglfjEsu);
                            }
                        }
                        if (CollectionUtil.isNotEmpty(fjData)) {
                            htglfjService.saveFjDatas(fjData);
                        }
                    }
                }
            }
            type = "成功";
        } catch (Exception e) {
            log.error("合同附件导入失败" + e);
        }
        return type;
    }
}
