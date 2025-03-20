package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.SectionManageDto;
import com.essence.common.dto.SectionWaterQualityDTO;
import com.essence.common.dto.TownCheckDto;
import com.essence.common.enums.SectionWaterQualityLevelEnum;
import com.essence.dao.ReaBaseDao;
import com.essence.dao.SectionBaseDao;
import com.essence.dao.SectionWaterQualityDao;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.ReaBaseService;
import com.essence.interfaces.api.SectionWaterQualityService;
import com.essence.interfaces.entity.PieChartDto;
import com.essence.interfaces.model.SectionWaterQualityEsu;
import com.essence.service.utils.DataUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hpsf.Section;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SectionWaterQualityServiceImpl
 *
 * @author bird
 * @date 2022/11/19 15:28
 * @Description
 */
@Service
public class SectionWaterQualityServiceImpl implements SectionWaterQualityService {

    @Autowired
    private SectionWaterQualityDao sectionWaterQualityDao;

    @Autowired
    private SectionBaseDao sectionBaseDao;
    @Resource
    private ReaBaseDao reaBaseDao;

    @Override
    public List<SectionWaterQualityDTO> queryAllSectionWaterQualityByPeriod(String period) {

        List<SectionWaterQualityEx> sectionWaterQuality = sectionWaterQualityDao.queryAllSectionWaterQualityByPeriod(period);

        List<SectionWaterQualityDTO> returnList = new ArrayList<>();
        sectionWaterQuality.forEach(item ->{
            SectionWaterQualityDTO sectionWaterQualityDTO = new SectionWaterQualityDTO();
            BeanUtils.copyProperties(item,sectionWaterQualityDTO);
            returnList.add(sectionWaterQualityDTO);
        });
        return returnList;
    }

    @Override
    public List<PieChartDto<SectionWaterQualityDTO>> querySectionWaterQualityByQualityLevel(String  period) {
        List<SectionWaterQualityEx> sectionWaterQuality = getSectionWaterQuality(period);
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)){
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        Map<String, ReaBase> finalRiverMap = riverMap;
        Map<Integer, List<SectionWaterQualityDTO>> sectionWaterQualityMap = sectionWaterQuality.stream()
                .map(item->{
                    SectionWaterQualityDTO dto = new SectionWaterQualityDTO();
                    BeanUtil.copyProperties(item,dto);
                    if (StrUtil.isNotEmpty(item.getRiverId())){
                        ReaBase reaBase = finalRiverMap.get(item.getRiverId());
                        dto.setRiverName(reaBase.getReaName());
                    }
                    return dto;
                })
                .collect(Collectors.groupingBy(SectionWaterQualityDTO::getQualityLevel));

        Integer total = CollectionUtils.isEmpty(sectionWaterQuality) ? 0 : sectionWaterQuality.size();

        List<PieChartDto<SectionWaterQualityDTO>> pieChartDtos = new ArrayList<>();
        for(SectionWaterQualityLevelEnum waterQualityLevelEnum: SectionWaterQualityLevelEnum.values()){
            PieChartDto pieChartDto = new PieChartDto();
            pieChartDto.setType(waterQualityLevelEnum.getText());

            List<SectionWaterQualityDTO> sectionWaterQualityDTOS = sectionWaterQualityMap.get(waterQualityLevelEnum.getType());
            if(!CollectionUtils.isEmpty(sectionWaterQualityDTOS)){
                pieChartDto.setNumber(sectionWaterQualityDTOS.size());
                pieChartDto.setData(sectionWaterQualityDTOS);
                Integer percentageNum = sectionWaterQualityDTOS.size() * 100  / total;
                pieChartDto.setOnlineNum(percentageNum);
            }else{
                pieChartDto.setNumber(0);
                pieChartDto.setOnlineNum(0);
            }
            pieChartDtos.add(pieChartDto);
        }
        return pieChartDtos;
    }

    List<SectionWaterQualityEx>  getSectionWaterQuality (String  period){
        List<SectionWaterQualityEx> sectionWaterQuality = sectionWaterQualityDao.queryAllSectionWaterQualityByPeriod(period);
        while ( CollUtil.isEmpty(sectionWaterQuality)){
            if (period.equals("2001-01")){
                return null;
            }
            String date = period+"-01";
            period = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(date),-1),"yyyy-MM-dd").substring(0,7)  ;
            sectionWaterQuality = getSectionWaterQuality(period);
            if (CollUtil.isNotEmpty(sectionWaterQuality)){
                String finalPeriod = period;
                sectionWaterQuality = sectionWaterQuality.parallelStream().filter(sectionWaterQualityEx -> {
                    if (sectionWaterQualityEx.getUpdateTime() == null){
                        sectionWaterQualityEx.setUpdateTime(finalPeriod);
                    }else {
                        sectionWaterQualityEx.setUpdateTime(sectionWaterQualityEx.getUpdateTime().substring(0,7));
                    }
                    return true;
                }).collect(Collectors.toList());
                return sectionWaterQuality;
            }
        }
        return sectionWaterQuality;
    }

    @Transactional
    @Override
    public void insertUpdateData(List<SectionWaterQualityEsu> importData,String userId){
        // 1. 断面名称与主键 map
        List<SectionBase> sectionBaseList = sectionBaseDao.selectList(new QueryWrapper<SectionBase>());
        Map<String,Integer> sectionNameAndIdMap = sectionBaseList.stream().collect(Collectors.toMap(SectionBase::getSectionName,SectionBase::getId));
        for(SectionWaterQualityEsu esu: importData){
            insertUpdateData(esu,sectionNameAndIdMap,userId);
        }
    }

    /**
     * 根据时间查询断面水质状况
     * @param period
     * @return
     */
    @Override
    public List<SectionWaterQualityDTO> findBytime(String period) {

        List<SectionWaterQualityDTO> sectionWater  = sectionWaterQualityDao.findBytime(period);
        if (CollUtil.isEmpty(sectionWater)){
            if (period.equals("2001-01")){
                return null;
            }
            String date = period+"-01";
            period = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(date),-1),"yyyy-MM-dd").substring(0,7)  ;
            List<SectionWaterQualityDTO>  sectionWaterQuality = findBytime(period);
            if (CollUtil.isNotEmpty(sectionWaterQuality)){
                String finalPeriod = period;
                sectionWaterQuality = sectionWaterQuality.parallelStream().filter(sectionWaterQualityDTO -> {
                    if (sectionWaterQualityDTO.getUpdateTime() == null){
                        sectionWaterQualityDTO.setUpdateTime(finalPeriod);
                    }
                    return true;
                }
                ).collect(Collectors.toList());
                return sectionWaterQuality;
            }
        }
        return sectionWater;
    }

    @Override
    public List<SectionManageDto> getSectionManageList(String period) {
        List<SectionManageDto> resList = new ArrayList<>();
        List<SectionBase> sectionBaseList = sectionBaseDao.selectList(new QueryWrapper<>());
        QueryWrapper wrapper =  new QueryWrapper();
        wrapper.like("quality_period",period);
        List<SectionWaterQuality> sectionWaterQualities = sectionWaterQualityDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(sectionWaterQualities)){
            Map<Integer, List<SectionWaterQuality>> sectionMap = sectionWaterQualities.parallelStream().collect(Collectors.groupingBy(SectionWaterQuality::getSectionId));
            for (SectionBase sectionBase : sectionBaseList) {
                SectionManageDto res = new SectionManageDto();
                res.setSectionName(sectionBase.getSectionName());
                res.setType(sectionBase.getSectionType());
                List<SectionWaterQuality> sectionWaterQualities1 = sectionMap.get(sectionBase.getId());
                if (CollUtil.isNotEmpty(sectionWaterQualities1)){

                    List<String> remark = sectionWaterQualities1.stream().map(sectionWaterQuality -> {
                        String value = StrUtil.isEmpty(sectionWaterQuality.getLevelRemark()) ? "" : sectionWaterQuality.getLevelRemark();
                        return value;
                    }).collect(Collectors.toList());

                    res.setLevel(remark);
                    res.setAvgLevel(sectionWaterQualities1.get(0).getAverageLevel());
                    String format = DateUtil.format(sectionWaterQualities1.get(0).getGmtCreate(), "yyyy-MM-dd");
                    res.setUpdateTime(format);
                }
                resList.add(res);
            }

        }
        return resList;
    }

    @Override
    public void delete(String id) {
        sectionWaterQualityDao.deleteById(id);
    }

    @Override
    public void dealTownsWord(InputStream inputStream) throws IOException {
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
        XWPFParagraph xwpfParagraph = xwpfDocument.getParagraphs().get(0);
        XWPFRun xwpfRun = xwpfParagraph.getRuns().get(0);
        String year = xwpfRun.text().substring(0,4);
        String month = "";
        //根据解析文档我们需要获取文档中第一个表格
        List<XWPFTable> xwpfTables = xwpfDocument.getTables();
        List<TownCheckDto> res = new ArrayList<>();
        if (CollUtil.isNotEmpty(xwpfTables)){
            for (int i = 0; i < xwpfTables.size(); i++) {

                //表示第一列 代表了 断面名称和断面性质
                List<XWPFTableRow> rows = xwpfTables.get(i).getRows();
                for (int r = 1; r < rows.size(); r++) {
                    XWPFTableRow xwpfTableRow = rows.get(r);
                    String text = xwpfTableRow.getCell(0).getText();
                    List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
                    String avg = "";
                    for (int l = 0; l < tableCells.size(); l++) {
                        String sectionName = "";
                        avg = tableCells.get(13).getText() == null ? "" : tableCells.get(13).getText()+"类";
                        if (l == 0){
                            sectionName = tableCells.get(l).getText().substring(0, tableCells.get(l).getText().indexOf("（"));
                        }
                        String text1 = "";

                        if (l == 13){

                        }else {
                            text1 = tableCells.get(l+1).getText() == null ? "" : tableCells.get(l+1).getText();
                        }
                        String date = "";
                        if (l <= 11){
                            month = String.valueOf(l +1).length()<2 ? "0"+String.valueOf(l +1) :String.valueOf(l +1) ;
                            date = year+ month;
                        }
                        QueryWrapper wrapper = new QueryWrapper();
                        wrapper.eq("section_name",sectionName);
                        SectionBase sectionBase = sectionBaseDao.selectOne(wrapper);
                        if (sectionBase != null){
                            QueryWrapper wrapper1 = new QueryWrapper();
                            wrapper1.eq("section_id",sectionBase.getId());
                            wrapper1.eq("quality_period",date);
                            SectionWaterQuality sectionWaterQuality = sectionWaterQualityDao.selectOne(wrapper1);
                            if (sectionWaterQuality == null){
                                SectionWaterQuality sectionWaterQuality1 = new SectionWaterQuality();
                                sectionWaterQuality1.setSectionId(sectionBase.getId());
                                sectionWaterQuality1.setLevelRemark(text1);
                                SectionWaterQualityLevelEnum enumByName = getEnumByName(avg);
                                sectionWaterQuality1.setAverageLevel(enumByName.getType());
                                sectionWaterQuality1.setQualityLevel(enumByName.getType());
                                sectionWaterQuality1.setQualityPeriod(date);
                                sectionWaterQualityDao.insert(sectionWaterQuality1);
                            }else {
                                SectionWaterQuality sectionWaterQuality1 = new SectionWaterQuality();
                                sectionWaterQuality1.setSectionId(sectionBase.getId());
                                sectionWaterQuality1.setLevelRemark(text1);
                                SectionWaterQualityLevelEnum enumByName = getEnumByName(avg);
                                sectionWaterQuality1.setAverageLevel(enumByName.getType());
                                sectionWaterQuality1.setQualityLevel(enumByName.getType());
                                sectionWaterQuality1.setQualityPeriod(date);
                                sectionWaterQualityDao.update(sectionWaterQuality1,wrapper1);
                            }

                        }

                    }
                }

            }
        }
    }

    @Override
    public List<SectionWaterQuality> findYear(String year, String sectionId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("quality_period",year);
        queryWrapper.eq("section_id",sectionId);
        queryWrapper.orderByAsc("quality_period");
        List<SectionWaterQuality> sectionWaterQualities = sectionWaterQualityDao.selectList(queryWrapper);
        return sectionWaterQualities;
    }


    private void insertUpdateData(SectionWaterQualityEsu esu,Map<String,Integer> sectionNameAndIdMap,String userId){
        Integer sectionId = sectionNameAndIdMap.get(esu.getSectionName());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("section_id",sectionId);
        queryWrapper.eq("quality_period",esu.getQualityPeriod());
        SectionWaterQuality getData =  sectionWaterQualityDao.selectOne(queryWrapper);

        SectionWaterQuality sectionWaterQuality = getDataFromExcel(esu);
        sectionWaterQuality.setSectionId(sectionId);
        if(getData == null){
            // 创建人
            sectionWaterQuality.setCreator(userId);
            sectionWaterQuality.setId(UuidUtil.get32UUIDStr());
            sectionWaterQualityDao.insert(sectionWaterQuality);
        }else{
            // 修改人，修改时间
            sectionWaterQuality.setGmtModified(new Date());
            sectionWaterQuality.setUpdater(userId);
            sectionWaterQualityDao.update(sectionWaterQuality,queryWrapper);
        }
    }

    /**
     * 通过 接受excel内容对象，生成对应水质实体对象
     * @param esu
     * @return
     */
    private SectionWaterQuality getDataFromExcel(SectionWaterQualityEsu esu){
        SectionWaterQuality sectionWaterQuality = new SectionWaterQuality();
        sectionWaterQuality.setQualityPeriod(esu.getQualityPeriod());
        SectionWaterQualityLevelEnum waterQualityLevelEnum = getEnumByName(esu.getQualityLevel());
        if(waterQualityLevelEnum != null){
            sectionWaterQuality.setQualityLevel(waterQualityLevelEnum.getType());
            sectionWaterQuality.setLevelRemark(esu.getQualityLevel());
        }
        SectionWaterQualityLevelEnum averageWaterQualityLevelEnum = getEnumByName(esu.getAverageLevel());
        if(averageWaterQualityLevelEnum != null){
            sectionWaterQuality.setAverageLevel(averageWaterQualityLevelEnum.getType());
        }
        return sectionWaterQuality;
    }


    /**
     * 通过水质类别文字，找到对应水质枚举类
     * @param name
     * @return
     */
    private SectionWaterQualityLevelEnum getEnumByName(String name){
        for(SectionWaterQualityLevelEnum waterQualityLevelEnum:SectionWaterQualityLevelEnum.values()){
            if(waterQualityLevelEnum.getText().contains(name)){
                return waterQualityLevelEnum;
            }
        }
        return null;
    }

}
