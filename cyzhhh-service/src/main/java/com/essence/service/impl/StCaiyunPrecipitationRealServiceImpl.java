package com.essence.service.impl;

import com.essence.dao.StCaiyunPrecipitationRealDao;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealDto;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealTableDto;
import com.essence.interfaces.api.StCaiyunPrecipitationRealService;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsr;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsu;
import com.essence.interfaces.param.StCaiyunPrecipitationRealEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaiyunPrecipitationRealEtoT;
import com.essence.service.converter.ConverterStCaiyunPrecipitationRealTtoR;
import com.essence.service.utils.BatchUtils;
import com.essence.service.utils.RainFallLevelHd;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 彩云预报实时数据 (st_caiyun_precipitation_real)表数据库业务层
 *
 * @author BINX
 * @since 2023年5月4日 下午3:47:15
 */
@Service
public class StCaiyunPrecipitationRealServiceImpl extends BaseApiImpl<StCaiyunPrecipitationRealEsu, StCaiyunPrecipitationRealEsp, StCaiyunPrecipitationRealEsr, StCaiyunPrecipitationRealDto> implements StCaiyunPrecipitationRealService {


    @Autowired
    private StCaiyunPrecipitationRealDao stCaiyunPrecipitationRealDao;
    @Autowired
    private ConverterStCaiyunPrecipitationRealEtoT converterStCaiyunPrecipitationRealEtoT;
    @Autowired
    private ConverterStCaiyunPrecipitationRealTtoR converterStCaiyunPrecipitationRealTtoR;


    @Autowired
    private BatchUtils batchUtils;

    public StCaiyunPrecipitationRealServiceImpl(StCaiyunPrecipitationRealDao stCaiyunPrecipitationRealDao, ConverterStCaiyunPrecipitationRealEtoT converterStCaiyunPrecipitationRealEtoT, ConverterStCaiyunPrecipitationRealTtoR converterStCaiyunPrecipitationRealTtoR) {
        super(stCaiyunPrecipitationRealDao, converterStCaiyunPrecipitationRealEtoT, converterStCaiyunPrecipitationRealTtoR);
    }

    @Override
    public void saveOrUpdate(List<StCaiyunPrecipitationRealEsu> stCaiyunPrecipitationRealEsuList) {
        List<StCaiyunPrecipitationRealDto> list = converterStCaiyunPrecipitationRealEtoT.toList(stCaiyunPrecipitationRealEsuList);

        batchUtils.batchInsert(list, StCaiyunPrecipitationRealDao.class, (m, e) -> {
            m.saveOrUpdate(e);
        }, 500);
    }

    @Override
    public List<Shiduan> getGridRainData() {
        List<StCaiyunPrecipitationRealDto> caiYunList = stCaiyunPrecipitationRealDao.selectOfGrid();
        List<Shiduan> shiduanList = getShiduans(caiYunList);
        return shiduanList;
    }

    private List<Shiduan> getShiduans(List<StCaiyunPrecipitationRealDto> caiYunList) {
        List<Shiduan> shiduanList = Lists.newArrayList();
        caiYunList.stream().collect(Collectors.groupingBy(StCaiyunPrecipitationRealDto::getDrpTime))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(entry -> {
                            Map<String, BigDecimal> gridMap = new HashMap<>();
                            entry.getValue().forEach(caiYunDto -> {
                                gridMap.put(caiYunDto.getMeshId(), new BigDecimal(caiYunDto.getDrp()));
                            });
                            Shiduan dealData = getDealData(gridMap, entry.getKey().getHours());
                            shiduanList.add(dealData);
                        }
                );
        return shiduanList;
    }

    @Override
    public List<Shiduan> getGridRainData59() {
        List<StCaiyunPrecipitationRealDto> stCaiyunPrecipitationRealDtos = stCaiyunPrecipitationRealDao.selectOfGrid59();
        List<Shiduan> shiduanList = getShiduans(stCaiyunPrecipitationRealDtos);
        return shiduanList;
    }

    @Override
    public List<StCaiyunPrecipitationRealTableDto> getTableRainData59() {
        Date now = new Date();
        List<StCaiyunPrecipitationRealDto> stCaiyunPrecipitationRealDtos = stCaiyunPrecipitationRealDao.selectOfGrid59();
        List<StCaiyunPrecipitationRealTableDto> stCaiyunPrecipitationRealTableDtos = mergeList(stCaiyunPrecipitationRealDtos, stCaiyunPrecipitationRealDto -> stCaiyunPrecipitationRealDto.getStnm(), stCaiyunPrecipitationRealDto -> StCaiyunPrecipitationRealTableDto.build(stCaiyunPrecipitationRealDto), ((o1, o2) -> {
            Date drpTime = o2.getDrpTime();
            int hours = (int) ((drpTime.getTime() - now.getTime()) / (1000 * 3600));
            switch (hours) {
                case 1:
                    o1.setDrp1(o2.getDrp());
                    break;
                case 3:
                    o1.setDrp3(o2.getDrp());
                    break;
                case 6:
                    o1.setDrp6(o2.getDrp());
                    break;
                case 12:
                    o1.setDrp12(o2.getDrp());
                    break;
                case 24:
                    o1.setDrp24(o2.getDrp());
                    break;
                default:
                    break;
            }
            Date drpTime1 = o1.getDrpTime();
            int hours1 = (int) ((drpTime1.getTime() - now.getTime()) / (1000 * 3600));
            switch (hours1) {
                case 1:
                    o1.setDrp1(o1.getDrp());
                    break;
                case 3:
                    o1.setDrp3(o1.getDrp());
                    break;
                case 6:
                    o1.setDrp6(o1.getDrp());
                    break;
                case 12:
                    o1.setDrp12(o1.getDrp());
                    break;
                case 24:
                    o1.setDrp24(o1.getDrp());
                    break;
                default:
                    break;
            }
            return o1;
        }));
        return stCaiyunPrecipitationRealTableDtos;
    }

    public static <T, R> List<R> mergeList(List<T> list, Function<? super T, ? extends String> classifier, Function<T, R> valueMap, BinaryOperator<R> mergeFunction) {
        if (null == list || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().collect(Collectors.toMap(classifier, valueMap, mergeFunction)).values()
                .stream()
                .collect(Collectors.toList());
    }

    public Shiduan getDealData(Map<String, BigDecimal> gridMap, int step) {
        Shiduan shiduan = new Shiduan();
        Map<String, String> map = new HashMap<>();
        for (String meshId : gridMap.keySet()) {
            BigDecimal drp = gridMap.get(meshId);
            BigDecimal divide = drp.divide(new BigDecimal(1), 3, BigDecimal.ROUND_HALF_UP);
            String value = "l:" + divide + ",v:" + RainFallLevelHd.getRainFallLevelNew(drp);
            map.put(meshId, value);
        }
        shiduan.setList(map);
        shiduan.setT(step);
        return shiduan;
    }
}
