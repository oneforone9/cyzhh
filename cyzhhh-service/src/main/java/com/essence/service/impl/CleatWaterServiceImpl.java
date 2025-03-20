package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.TownCheckDto;
import com.essence.common.dto.clear.ClearWeekCheckDTO;
import com.essence.common.dto.clear.ClearWeekStatisticDTO;
import com.essence.common.dto.clear.TownCheckStatisticDTO;
import com.essence.common.dto.health.AreaHealthDataInfoDto;
import com.essence.common.dto.health.HealthRequestDto;
import com.essence.common.enums.SectionWaterQualityLevelEnum;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.health.AreaHealthDataInfoEntity;
import com.essence.interfaces.api.CleatWaterService;
import com.essence.interfaces.dot.OrganismRiverInfosDto;
import com.essence.interfaces.dot.OrganismRiverRecordDto;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CleatWaterServiceImpl implements CleatWaterService {
    @Resource
    private ClearWeekCheckDao clearWeekCheckDao ;
    @Resource
    private TownCheckDao townCheckDao;
    @Resource
    private OrganismRiverRecordDao organismRiverRecordDao;

    @Resource
    private AreaHealthDataInfoDao areaHealthDataInfoDao;
    @Resource
    private TownCheckDataDao townCheckDataDao;
    @Resource
    private ReaBaseDao reaBaseDao;
    @Resource
    private SectionBaseDao  sectionBaseDao;
    @Override
    public void delExcelData( InputStream inputStream) throws IOException {
        List<ClearWeekCheckDTO> list = new ArrayList<>();

        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

        Double nh4St = 0d;
        Double codSt = 0d;
        //磷
        Double pSt = 0d;
        //透明度
        Double transmissionSt = 0d;
        String rvnm = "";
        String stringCellValue1 = sheet.getRow(0).getCell(0).getStringCellValue();
        String week = stringCellValue1.substring(stringCellValue1.indexOf("第")+1,stringCellValue1.indexOf("第")+3);
        for (int i = 1; i < physicalNumberOfRows; i++) {
            int type = 1;
            ClearWeekCheckDTO clearWeekCheckDTO = new ClearWeekCheckDTO();
            if (i == 1 || i == 2){
                continue;
            }
            XSSFRow row = sheet.getRow(i);
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();

            if (i == 3){
                //氨氮
                nh4St = row.getCell(4) == null ? null : row.getCell(4).getNumericCellValue();
                codSt = row.getCell(5) == null ? null : row.getCell(5).getNumericCellValue();
                //磷
                pSt = row.getCell(6) == null ? null : row.getCell(6).getNumericCellValue();
                //透明度
                transmissionSt = row.getCell(7) == null ? null : row.getCell(7).getNumericCellValue();
                continue;
            }
            if (StrUtil.isNotEmpty(row.getCell(1).getStringCellValue()) ){
                rvnm = row.getCell(1).getStringCellValue();
            }
            String split = "";
            if (row.getCell(0)!= null){
                try {
                    double value = row.getCell(0).getNumericCellValue();
                } catch (Exception e) {
                    split = row.getCell(0).getStringCellValue();
                }
            }
            if (split.contains("朝阳区考核断面水质检")){
                i = i +3;
                type = 2;
                continue;
            }
            clearWeekCheckDTO.setNh4Stander(nh4St == null ? null: new BigDecimal(nh4St));
            clearWeekCheckDTO.setCodStander(codSt == null ? null: new BigDecimal(codSt));
            clearWeekCheckDTO.setPStander(pSt == null ? null: new BigDecimal(pSt) );
            clearWeekCheckDTO.setTransmissionStander(transmissionSt == null ? null: new BigDecimal(transmissionSt));
            String exampleName = row.getCell(2).getStringCellValue();
            Date dateCellValue = null;
            try {
                dateCellValue = row.getCell(3).getDateCellValue();
            } catch (Exception e) {
                String stringCellValue = row.getCell(3).getStringCellValue();
                if (stringCellValue.contains("/")){
                    continue;
                }else {
                    throw new RuntimeException(e);
                }
            }
            String time = DateUtil.format(dateCellValue,"yyyy-MM-dd");
            //氨氮
            Double nh4 = null;
            try {
                nh4 = row.getCell(4) == null ? 0d : row.getCell(4).getNumericCellValue();
            } catch (Exception e) {
                nh4 = row.getCell(4) == null ? 0d : Double.valueOf(row.getCell(4).getStringCellValue().substring(1)) ;
            }
            Double cod = null;
            try {
                cod = row.getCell(5) == null ? 0d : row.getCell(5).getNumericCellValue();
            } catch (Exception e) {
                cod = row.getCell(5) == null ? 0d : Double.valueOf(row.getCell(5).getStringCellValue().substring(1)) ;
            }
            //磷
            Double p = null;
            try {
                p = row.getCell(6) == null ? 0d : row.getCell(6).getNumericCellValue();
            } catch (Exception e) {
                p = row.getCell(6) == null ? 0d : Double.valueOf(row.getCell(6).getStringCellValue().substring(1)) ;
            }
            //透明度
            Double transmission = null;
            try {
                transmission = row.getCell(7) == null ? 0d : row.getCell(7).getNumericCellValue();
            } catch (Exception e) {
                transmission = row.getCell(7) == null ? 0d : Double.valueOf(row.getCell(7).getStringCellValue().substring(1)) ;
            }

            clearWeekCheckDTO.setNh4(nh4 == null ? null: new BigDecimal(nh4));
            clearWeekCheckDTO.setCod(cod == null ? null: new BigDecimal(cod));
            clearWeekCheckDTO.setP(p == null ? null: new BigDecimal(p) );
            clearWeekCheckDTO.setTransmission(transmission == null ? null: new BigDecimal(transmission));
            clearWeekCheckDTO.setRvnm(rvnm);
            clearWeekCheckDTO.setName(exampleName);
            clearWeekCheckDTO.setTime(time);
            clearWeekCheckDTO.setYear(time.substring(0,4));
//            clearWeekCheckDTO.setType(type);
            DateTime parse = DateUtil.parse(time);
            int weekOfYear = DateUtil.weekOfYear(parse);
            clearWeekCheckDTO.setWeekOfYear(Integer.valueOf(week) );
            list.add(clearWeekCheckDTO);

        }
        if (CollUtil.isNotEmpty(list)){
            for (ClearWeekCheckDTO clearWeekCheckDTO : list) {
                ClearWeekCheckEntity clearWeekCheckEntity = new ClearWeekCheckEntity();
                clearWeekCheckEntity.setUpdate_time(new Date());
                BeanUtil.copyProperties(clearWeekCheckDTO,clearWeekCheckEntity);
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("time",clearWeekCheckEntity.getTime());
                wrapper.eq("name",clearWeekCheckEntity.getName());
                wrapper.eq("week_of_year",clearWeekCheckEntity.getWeekOfYear());
                ClearWeekCheckEntity clearWeekCheck = clearWeekCheckDao.selectOne(wrapper);
                if (clearWeekCheck != null){
                    clearWeekCheckDao.update(clearWeekCheckEntity ,wrapper);
                }else {
                    clearWeekCheckDao.insert(clearWeekCheckEntity);
                }
            }
        }
    }

    @Override
    public ClearWeekStatisticDTO getWeekStatistic(ClearWeekStatisticDTO clearWeekStatisticDTO) {
        String time1 = clearWeekStatisticDTO.getTime();
        DateTime parse1 = DateUtil.parse(time1);
        int weekOfYear1 = DateUtil.weekOfYear(parse1);
        clearWeekStatisticDTO.setWeek(time1.substring(0,4)+"-"+weekOfYear1);

        ClearWeekStatisticDTO res = new ClearWeekStatisticDTO();
        String[] split = clearWeekStatisticDTO.getWeek().split("-");
        QueryWrapper<ClearWeekCheckEntity> wrapper = new QueryWrapper();
        wrapper.eq("year",split[0]);
        wrapper.eq("week_of_year",split[1]);
        List<ClearWeekCheckEntity> list = clearWeekCheckDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)){
            //水质数量 = 检测河流 数量
            int size = list.size();
            Integer okCheeked = getOkCheeked(list);
            //计算 达标占比
            BigDecimal riverPercent = new BigDecimal(okCheeked).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

            String time = list.get(0).getTime();
            DateTime parse = DateUtil.parse(time);
            //上周
            DateTime dateTime = DateUtil.offsetWeek(parse, -1);
            String substring = DateUtil.format(dateTime, "yyyy-MM-dd").substring(0, 4);
            int weekOfYear = DateUtil.weekOfYear(dateTime);

            QueryWrapper<ClearWeekCheckEntity> wrapperLast = new QueryWrapper();
            wrapperLast.eq("year",substring);
            wrapperLast.eq("week_of_year",weekOfYear);
            List<ClearWeekCheckEntity> lastWeekList = clearWeekCheckDao.selectList(wrapperLast);
            Integer lastOkCheeked = getOkCheeked(lastWeekList);
            //计算环比
            BigDecimal subtract = new BigDecimal(okCheeked).subtract(new BigDecimal(lastOkCheeked));
            BigDecimal waterPercent = subtract.divide(new BigDecimal(okCheeked), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            ClearWeekCheckEntity mvWater = getMvWater(list);
            res.setMvWater(mvWater.getName());
            res.setMvRiver(mvWater.getRvnm());
            res.setRiverNum(size);
            res.setWaterNum(size);
            res.setRiverPercent(riverPercent);
            res.setWaterPercent(waterPercent);
        }
        return res;
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
                for (int r = 0; r < xwpfTables.get(i).getRows().size(); r++) {
                    TownCheckDto townCheckEntity = new TownCheckDto();
                    if (r == 0){
                        XWPFTableRow row = xwpfTables.get(i).getRow(i);
                        month = row.getCell(5).getText().substring(0, 1);
                        if (month.length() == 1){
                            month = "0"+month;
                        }
                        continue;
                    }
                    if (r == 1){
                        continue;
                    }
                    XWPFTableRow row = xwpfTables.get(i).getRow(r);
                    String area = row.getCell(0).getText();
                    String street = row.getCell(1).getText();
                    String river = row.getCell(2).getText();
                    String selection = row.getCell(3).getText();

                    String waterStatus = row.getCell(6).getText();
                    //规划目标
                    String target = row.getCell(4).getText();
                    //水质类别
                    String waterType = row.getCell(5).getText();

                    //污染物
                    String pollute = row.getCell(7).getText();
                    //平均水质类别
                    String avgType = row.getCell(8).getText();
                    //平均水质 达标情况
                    String avgStatus = row.getCell(9).getText();
                    // 0 无水 1 达标 2 不达标
                    int  status = 0;
                    if (waterStatus.contains("-")){
                        status = 0;
                    }else if (waterStatus.contains("不达标")){
                        status = 2;
                    }else {
                        status = 1;
                    }
                    townCheckEntity.setTarget(target);
                    townCheckEntity.setWaterType(waterType);
                    townCheckEntity.setPollute(pollute);
                    townCheckEntity.setAvgType(avgType);
                    townCheckEntity.setAvgStatus(avgStatus);
                    townCheckEntity.setArea(area);
                    townCheckEntity.setStreet(street);
                    townCheckEntity.setRiver(river);
                    townCheckEntity.setSelection(selection);
                    townCheckEntity.setStatus(status);
                    townCheckEntity.setTime(year+"-"+month);
                    res.add(townCheckEntity);
                }
            }
        }
        if (CollUtil.isNotEmpty(res)){
            for (TownCheckDto re : res) {
                String selectionId = "";
                QueryWrapper<TownCheckEntity> townWrapper = new QueryWrapper<>();
                townWrapper.eq("street",re.getStreet());
//                townWrapper.eq("time",re.getTime());
                townWrapper.eq("river",re.getRiver());
                townWrapper.eq("selection",re.getSelection());
                TownCheckEntity townCheckEntity = townCheckDao.selectOne(townWrapper);
                TownCheckEntity town = new TownCheckEntity();
                BeanUtil.copyProperties(re,town);
                if (townCheckEntity != null){
                    townCheckDao.update(town,townWrapper);
                }else {
                    selectionId = UUID.randomUUID().toString().replace("-","");
                    town.setId(selectionId);
                    townCheckDao.insert(town);
                }

                if (townCheckEntity != null){
                    //插入具体的数据
                    QueryWrapper<TownCheckDataEntity> wrapper = new QueryWrapper<>();
                    wrapper.eq("selection_id",townCheckEntity.getId());
                    wrapper.eq("time",townCheckEntity.getId());
                    TownCheckDataEntity townCheckDataEntity = new TownCheckDataEntity();
                    BeanUtil.copyProperties(re,townCheckDataEntity);
                    townCheckDataEntity.setSelectionId(townCheckEntity.getId());
                    List<TownCheckDataEntity> townCheckDataEntities = townCheckDataDao.selectList(wrapper);
                    if (CollUtil.isNotEmpty(townCheckDataEntities)){
                        townCheckDataDao.update(townCheckDataEntity,wrapper);
                    }else {
                        townCheckDataDao.insert(townCheckDataEntity);
                    }

                }else {
                    TownCheckDataEntity townCheckDataEntity = new TownCheckDataEntity();
                    BeanUtil.copyProperties(re,townCheckDataEntity);
                    townCheckDataEntity.setSelectionId(selectionId);
                    townCheckDataDao.insert(townCheckDataEntity);
                }


            }
        }
    }

    @Override
    public TownCheckStatisticDTO getTownsStatistic(TownCheckStatisticDTO townCheckStatisticDTO) {
        TownCheckStatisticDTO res = new TownCheckStatisticDTO();
        String time = townCheckStatisticDTO.getTime();
        String now = time.substring(0,7);
        DateTime parse = DateUtil.parse(time);
        QueryWrapper<TownCheckDataEntity> wrapper = new QueryWrapper();
        wrapper.eq("time",now);
        List<TownCheckEntity> nowList = townCheckDao.selectList(new QueryWrapper<>());
        List<TownCheckDataEntity> townCheckDataEntities = townCheckDataDao.selectList(wrapper);
        TownCheckStatisticDTO nowDto = getStatisticNum(nowList,townCheckDataEntities);
        res.setPass(nowDto.getPass());
        res.setUnPass(nowDto.getUnPass());
        res.setNone(nowDto.getNone());
        //上个月
        DateTime lastMonth = DateUtil.offset(parse, DateField.MONTH, -1);
        String format1 = DateUtil.format(lastMonth, "yyyy-MM-dd");
        String lastMonthStr = format1.substring(0,7);
        QueryWrapper<TownCheckDataEntity> lastMonthWrapper = new QueryWrapper();
        lastMonthWrapper.eq("time",lastMonthStr);
        List<TownCheckDataEntity> lasttownCheckDataEntities = townCheckDataDao.selectList(lastMonthWrapper);
        TownCheckStatisticDTO lastMonthDto = getStatisticNum(nowList,lasttownCheckDataEntities);
        //环比
        res.setRangeMore(nowDto.getUnPass() - lastMonthDto.getUnPass());
        res.setRangePass(nowDto.getPass() - lastMonthDto.getPass());
        res.setRangeNone(nowDto.getNone() - lastMonthDto.getNone());

        //上一年
        DateTime lastYear = DateUtil.offset(parse, DateField.YEAR, -1);
        String format2 = DateUtil.format(lastYear, "yyyy-MM-dd");
        String lastYearStr = format2.substring(0,7);
        QueryWrapper<TownCheckDataEntity> lastYearWrapper = new QueryWrapper();
        lastYearWrapper.eq("time",lastYearStr);

        List<TownCheckDataEntity> lastYearListDataEntities = townCheckDataDao.selectList(lastYearWrapper);
        TownCheckStatisticDTO lastYearDto = getStatisticNum(nowList,lastYearListDataEntities);
        //同比
        res.setCurrentMore(nowDto.getUnPass() - lastYearDto.getUnPass());
        res.setCurrentPass(nowDto.getPass() - lastYearDto.getPass());
        res.setCurrentNone(nowDto.getNone() - lastYearDto.getNone());
        return res;
    }

    @Override
    public void delOrganismExcelData(InputStream inputStream) throws IOException {
        List<OrganismRiverRecordEntity> res = new ArrayList<>();
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        String year = DateUtil.format(new Date(), "yyyy-MM-dd").substring(0, 4);;
        for (int i = 1; i < physicalNumberOfRows; i++) {
            OrganismRiverRecordEntity riverRecord = new OrganismRiverRecordEntity();
            XSSFRow row = sheet.getRow(i);
            // 河流名称
            String rvnm = row.getCell(0).getStringCellValue();
            //河流 长度
            BigDecimal rvLong = row.getCell(1) == null ? BigDecimal.ZERO : new BigDecimal(row.getCell(1).getNumericCellValue()) ;
            //河流 面积
            BigDecimal rvWide = row.getCell(2) == null ? BigDecimal.ZERO : new BigDecimal( row.getCell(2).getNumericCellValue()) ;
            riverRecord.setRvnm(rvnm);
            riverRecord.setRvLong(rvLong);
            riverRecord.setRvWide(rvWide);
            riverRecord.setYear(year);
            res.add(riverRecord);
        }
        if (CollUtil.isNotEmpty(res)){
            for (OrganismRiverRecordEntity re : res) {
                QueryWrapper<OrganismRiverRecordEntity> wrapper = new QueryWrapper();
                wrapper.eq("year",re.getYear());
                wrapper.eq("rvnm",re.getRvnm());
                OrganismRiverRecordEntity riverRecord = organismRiverRecordDao.selectOne(wrapper);
                if (riverRecord != null){
                    organismRiverRecordDao.update(re,wrapper);
                }else {
                    organismRiverRecordDao.insert(re);
                }

            }
        }
    }

    @Override
    public OrganismRiverInfosDto getRiverList(String rvid) {
        OrganismRiverInfosDto organismRiverInfosDto = new OrganismRiverInfosDto();
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        if (StrUtil.isNotEmpty(rvid) ){
            queryWrapper.eq("id", rvid);
        }
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        if (CollUtil.isEmpty( reaBases)){
            return organismRiverInfosDto;
        }
        List<String> reaNames = reaBases.parallelStream().map(ReaBase::getReaName).collect(Collectors.toList());
        String year = DateUtil.format(new Date(), "yyyy-MM-dd").substring(0, 4);
        QueryWrapper<OrganismRiverRecordEntity> wrapper = new QueryWrapper();
        wrapper.eq("year",year);
        wrapper.in("rvnm",reaNames);
        List<OrganismRiverRecordEntity> organismRiverRecordEntities = organismRiverRecordDao.selectList(wrapper);
        if (CollUtil.isEmpty(organismRiverRecordEntities)){
            String lastYear = DateUtil.format(DateUtil.offset(new Date(),DateField.YEAR,-1) , "yyyy-MM-dd").substring(0, 4);
            QueryWrapper<OrganismRiverRecordEntity> lastWrapper = new QueryWrapper();
            lastWrapper.eq("year",lastYear);
            lastWrapper.in("rvnm",reaNames);
            organismRiverRecordEntities = organismRiverRecordDao.selectList(lastWrapper);
        }
        if (CollUtil.isNotEmpty(organismRiverRecordEntities)){
            //排序河长
            List<OrganismRiverRecordEntity> longRiverOrg = organismRiverRecordEntities.parallelStream().sorted(Comparator.comparing(organismRiverRecordEntity -> {
                return organismRiverRecordEntity.getRvLong() == null ? BigDecimal.ZERO : organismRiverRecordEntity.getRvLong();
            })).collect(Collectors.toList());
            List<OrganismRiverRecordDto> longListDto = BeanUtil.copyToList(longRiverOrg, OrganismRiverRecordDto.class);
            setGeom(longListDto,reaBases);
            organismRiverInfosDto.setRiverLong(CollUtil.reverseNew(longListDto) );
            //排序 河面积
            List<OrganismRiverRecordEntity> wideOrg = organismRiverRecordEntities.parallelStream().sorted(Comparator.comparing(organismRiverRecordEntity -> {
                return organismRiverRecordEntity.getRvWide() == null ? BigDecimal.ZERO : organismRiverRecordEntity.getRvWide();
            })).collect(Collectors.toList());
            List<OrganismRiverRecordDto> wideListDto = BeanUtil.copyToList(wideOrg, OrganismRiverRecordDto.class);
            setGeom(wideListDto,reaBases);
            organismRiverInfosDto.setRiverWide(CollUtil.reverseNew(wideListDto) );
        }
        return organismRiverInfosDto;
    }


    public void setGeom(List<OrganismRiverRecordDto> longListDto ,List<ReaBase> reaBases){
        Map<String, ReaBase> collect = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getReaName, Function.identity()));
        for (OrganismRiverRecordDto organismRiverRecordDto : longListDto) {
            organismRiverRecordDto.setGeom(collect.get(organismRiverRecordDto.getRvnm()) == null ? null : collect.get(organismRiverRecordDto.getRvnm()).getGeom());
        }
    }

    @Override
    public List<AreaHealthDataInfoDto> getHealthList(HealthRequestDto healthRequestDto) {
        QueryWrapper<AreaHealthDataInfoEntity> wrapper = new QueryWrapper();
        wrapper.eq("ym",healthRequestDto.getYm());
        wrapper.eq("type",healthRequestDto.getType());
        List<AreaHealthDataInfoEntity> areaHealthDataInfoEntities = areaHealthDataInfoDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(areaHealthDataInfoEntities)){
            List<AreaHealthDataInfoDto> areaHealthDataInfoDtos = BeanUtil.copyToList(areaHealthDataInfoEntities, AreaHealthDataInfoDto.class);
            return areaHealthDataInfoDtos;
        }
        return null;
    }

    @Override
    public List<OrganismRiverRecordDto> getManageRiverList(String year) {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);

        OrganismRiverInfosDto organismRiverInfosDto = new OrganismRiverInfosDto();
        QueryWrapper<OrganismRiverRecordEntity> wrapper = new QueryWrapper();
        wrapper.eq("year",year);

        List<OrganismRiverRecordEntity> organismRiverRecordEntities = organismRiverRecordDao.selectList(wrapper);
        if (CollUtil.isEmpty(organismRiverRecordEntities)){
            String lastYear = DateUtil.format(DateUtil.offset(new Date(),DateField.YEAR,-1) , "yyyy-MM-dd").substring(0, 4);
            QueryWrapper<OrganismRiverRecordEntity> lastWrapper = new QueryWrapper();
            lastWrapper.eq("year",lastYear);
            organismRiverRecordEntities = organismRiverRecordDao.selectList(lastWrapper);
        }
        List<OrganismRiverRecordDto> res = new ArrayList<>();
        if (CollUtil.isNotEmpty(organismRiverRecordEntities)){
            Map<String, OrganismRiverRecordEntity> map = organismRiverRecordEntities.parallelStream().collect(Collectors.toMap(OrganismRiverRecordEntity::getRvnm, Function.identity()));
            for (ReaBase reaBase : reaBases) {
                OrganismRiverRecordDto organismRiverRecordDto = new OrganismRiverRecordDto();
                String reaName = reaBase.getReaName();
                OrganismRiverRecordEntity organismRiverRecordEntity = map.get(reaName);
                if (organismRiverRecordEntity != null) {
                    organismRiverRecordDto.setRvnm(reaName);
                    organismRiverRecordDto.setAdName(reaBase.getAdName());
                    organismRiverRecordDto.setRvLong(organismRiverRecordEntity.getRvLong());
                    organismRiverRecordDto.setRvWide(organismRiverRecordEntity.getRvWide());
                    organismRiverRecordDto.setUnitName(reaBase.getUnitName());
                    organismRiverRecordDto.setYear(organismRiverRecordEntity.getYear());
                    res.add(organismRiverRecordDto);
                }

            }
        }
        return res;
    }

    @Override
    public void addManageRiverList(List<OrganismRiverRecordDto> list) {
        if (CollUtil.isNotEmpty(list)){
            for (OrganismRiverRecordDto organismRiverRecordDto : list) {
                OrganismRiverRecordEntity entity = new OrganismRiverRecordEntity();
                String year = organismRiverRecordDto.getYear();
                String rvnm = organismRiverRecordDto.getRvnm();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("year",year);
                wrapper.eq("rvnm",rvnm);
                BeanUtil.copyProperties(organismRiverRecordDto,entity);
                List<OrganismRiverRecordEntity> organismRiverRecordEntities = organismRiverRecordDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(organismRiverRecordEntities)){
                    organismRiverRecordDao.update(entity,wrapper);
                }else {
                    organismRiverRecordDao.insert(entity);
                }

            }
        }
    }

    @Override
    public List<ClearWeekCheckDTO> getWeekData(String week) {
        List<ClearWeekCheckDTO> res = new ArrayList<>();
        String[] split = week.split("-");
        QueryWrapper<ClearWeekCheckEntity> wrapper = new QueryWrapper();
        wrapper.eq("year",split[0]);
        wrapper.eq("week_of_year",split[1]);
        List<ClearWeekCheckEntity> list = clearWeekCheckDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)){
            for (ClearWeekCheckEntity clearWeekCheckEntity : list) {
                ClearWeekCheckDTO clearWeekCheckDTO = new ClearWeekCheckDTO();
                BeanUtil.copyProperties(clearWeekCheckEntity,clearWeekCheckDTO);
                res.add(clearWeekCheckDTO);
            }
        }
        return res;
    }

    @Override
    public List<TownCheckDto> townList(TownCheckStatisticDTO townCheckStatisticDTO) {
        List<TownCheckDto> res = new ArrayList<>();
        String time = townCheckStatisticDTO.getTime();
        String now = time.substring(0,7);

        QueryWrapper<TownCheckDataEntity> wrapper = new QueryWrapper();
        wrapper.eq("time",now);
        if (StrUtil.isNotEmpty(townCheckStatisticDTO.getSelectionId()) ){
            wrapper.eq("selection_id",townCheckStatisticDTO.getSelectionId());
        }
        List<TownCheckEntity> nowList = townCheckDao.selectList(new QueryWrapper<>());
        List<TownCheckDataEntity> townCheckDataEntities = townCheckDataDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(townCheckDataEntities)){
            Map<String, TownCheckDataEntity> selectionIdMap = townCheckDataEntities.parallelStream().collect(Collectors.toMap(TownCheckDataEntity::getSelectionId, Function.identity(),(o1,o2)->o2));
            for (TownCheckEntity townCheckEntity : nowList) {

                TownCheckDataEntity townCheckDataEntity = selectionIdMap.get(townCheckEntity.getId());
                if (!Objects.isNull(townCheckDataEntity)){
                    TownCheckDto townCheckDto = new TownCheckDto();
                    BeanUtil.copyProperties(townCheckEntity,townCheckDto);
                    BeanUtil.copyProperties(townCheckDataEntity,townCheckDto);
                    res.add(townCheckDto);
                }
            }
        }
        return res;
    }

    @Override
    public Map<String, Integer> getRankStatistic(String year) {
        Map<String, Integer> res = new HashMap<>();
        QueryWrapper  queryWrapper = new QueryWrapper();
        queryWrapper.like("time",year);
        List<TownCheckDataEntity> townCheckDataEntities = townCheckDataDao.selectList(queryWrapper);
        Map<String, List<TownCheckDataEntity>> waterType = new HashMap<>();
        if (CollUtil.isNotEmpty(townCheckDataEntities)){
           waterType = townCheckDataEntities.parallelStream().collect(Collectors.groupingBy(TownCheckDataEntity::getWaterType));
        }
        for(SectionWaterQualityLevelEnum waterQualityLevelEnum: SectionWaterQualityLevelEnum.values()){
            List<TownCheckDataEntity> townCheckDataEntities1 = waterType.get(waterQualityLevelEnum.getText());
            res.put(waterQualityLevelEnum.getText(),CollUtil.isNotEmpty(townCheckDataEntities1) ? townCheckDataEntities1.size() : 0);
        }
        return res;
    }

    public TownCheckStatisticDTO getStatisticNum(List<TownCheckEntity> nowList,List<TownCheckDataEntity> townCheckDataEntities){
        Map<String, TownCheckDataEntity> selectionMap = new HashMap<>();
        if (CollUtil.isNotEmpty(townCheckDataEntities)){
            selectionMap = townCheckDataEntities.parallelStream().collect(Collectors.toMap(TownCheckDataEntity::getSelectionId, Function.identity()));
        }
        List<TownCheckDto> res = new ArrayList<>();
        if (CollUtil.isNotEmpty(nowList)){
            for (TownCheckEntity townCheckEntity : nowList) {
                TownCheckDto townCheckDto = new TownCheckDto();
                TownCheckDataEntity townCheckDataEntity = selectionMap.get(townCheckEntity.getId());
                if (townCheckDataEntity == null){
                    continue;
                }else {
                    BeanUtil.copyProperties(townCheckDataEntity,townCheckDto);
                }
                BeanUtil.copyProperties(townCheckEntity,townCheckDto);
                res.add(townCheckDto);
            }
        }
        TownCheckStatisticDTO townCheckStatisticDTO = new TownCheckStatisticDTO();
        // 0 无水 1 达标 2 不达标
        Map<Integer, List<TownCheckDto>> map = new HashMap<>();
        if (CollUtil.isNotEmpty(res)){
            map = res.parallelStream().collect(Collectors.groupingBy(TownCheckDto::getStatus));
        }
        townCheckStatisticDTO.setPass(CollUtil.isNotEmpty(map.get(1)) ? map.get(1).size() : 0 );
        townCheckStatisticDTO.setNone(CollUtil.isNotEmpty(map.get(0)) ? map.get(0).size() : 0 );
        townCheckStatisticDTO.setUnPass(CollUtil.isNotEmpty(map.get(2)) ? map.get(2).size() : 0 );
        return townCheckStatisticDTO;
    }

    /**
     * 获取 达标  数量
     * @param list
     * @return
     */
    public Integer getOkCheeked(List<ClearWeekCheckEntity> list){
        int checked = 0;
        if (CollUtil.isNotEmpty(list)){
            for (ClearWeekCheckEntity clearWeekCheckEntity : list) {
                BigDecimal p = clearWeekCheckEntity.getP();
                BigDecimal pStander = clearWeekCheckEntity.getPStander();
                boolean flag = p.compareTo(pStander) <= 0;

                BigDecimal nh4 = clearWeekCheckEntity.getNh4();
                BigDecimal nh4Stander = clearWeekCheckEntity.getNh4Stander();
                boolean flag2 = nh4.compareTo(nh4Stander) <= 0;

                BigDecimal cod = clearWeekCheckEntity.getCod();
                BigDecimal codStander = clearWeekCheckEntity.getCodStander();
                boolean flag3 = cod.compareTo(codStander) <= 0;

                BigDecimal transmission = clearWeekCheckEntity.getTransmission();
                BigDecimal transmissionStander = clearWeekCheckEntity.getTransmissionStander();
                boolean fla4 = transmission.compareTo(transmissionStander) >= 0;

                if(flag && flag2 && flag3 && fla4){
                    ++ checked;
                }
            }
        }
        return checked;
    }

    /**
     * 获取最优 水样 河系
     * @param list
     * @return
     */
    public ClearWeekCheckEntity getMvWater(List<ClearWeekCheckEntity> list){
        if (CollUtil.isNotEmpty(list)){
            List<ClearWeekCheckEntity> mv = list.parallelStream().filter(clearWeekCheckEntity -> {
                boolean b = clearWeekCheckEntity.getTransmission().compareTo(clearWeekCheckEntity.getTransmissionStander()) > 0;
                return b;
            }).sorted(Comparator.comparing(clearWeekCheckEntity -> {
                BigDecimal add = clearWeekCheckEntity.getP().add(clearWeekCheckEntity.getCod()).add(clearWeekCheckEntity.getNh4());
                return  add;
            })).collect(Collectors.toList());

            return mv.get(0);
        }
        return null;
    }


}
