package com.essence.web.caiyun;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StCaiyunPrecipitationHistoryDao;
import com.essence.dao.StStbprpBDao;
import com.essence.dao.entity.StRainDateDto;
import com.essence.dao.entity.StStbprpBEntity;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import com.essence.interfaces.api.StCaiyunPrecipitationHistoryService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StCaiyunPrecipitationHistoryEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.service.utils.DataUtils;
import com.essence.web.basecontroller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 彩云预报历史数据
 *
 * @author BINX
 * @since 2023年5月4日 下午3:47:15
 */
@RestController
@RequestMapping("/stCaiyunPrecipitationHistory")
@Slf4j
public class StCaiyunPrecipitationHistoryController extends BaseController<String, StCaiyunPrecipitationHistoryEsu, StCaiyunPrecipitationHistoryEsp, StCaiyunPrecipitationHistoryEsr> {

    @Autowired
    private StCaiyunPrecipitationHistoryService stCaiyunPrecipitationHistoryService;

    @Autowired
    private StCaiyunPrecipitationHistoryDao stCaiyunPrecipitationHistoryDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;

    public StCaiyunPrecipitationHistoryController(StCaiyunPrecipitationHistoryService stCaiyunPrecipitationHistoryService) {
        super(stCaiyunPrecipitationHistoryService);
    }


    @PostMapping("/selectRain")
    public ResponseResult selectRain(HttpServletRequest request, @RequestBody StCaseResRainQuery stCaseResRainQuery) {
        List<StCaiyunPrecipitationHistoryDto> stCaiyunPrecipitationHistoryDtos = stCaiyunPrecipitationHistoryService.selectRain(stCaseResRainQuery);
        StCaseResRainLists stCaseResRainLists = new StCaseResRainLists();
        List<StCaseResRainList> stCaseResRainListList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> stnmList = new ArrayList<>();
        if (CollUtil.isEmpty(stCaiyunPrecipitationHistoryDtos)){
            List<Date> timeSplit = DataUtils.getTimeSplit(stCaseResRainQuery.getStartTime(), stCaseResRainQuery.getEndTime(), stCaseResRainQuery.getStep(), DateField.MINUTE);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").isNotNull(StStbprpBEntity::getSectionName).orderByAsc(StStbprpBEntity::getStcd))).orElse(com.google.common.collect.Lists.newArrayList());
            List<String> collect = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStnm()).collect(Collectors.toList())).orElse(com.google.common.collect.Lists.newArrayList());
            stCaseResRainLists.setStnmList(collect);
            //如果实测降雨 查询不到数据就补充0
            for (int i = 0; i < timeSplit.size() - 1; i++) {
                StCaseResRainList stCaseResRainList = new StCaseResRainList();
                stCaseResRainList.setTime(df.format(timeSplit.get(i)));
                Date begin = timeSplit.get(i);
                Date over = timeSplit.get(i + 1);
                List<StCaseResRain> dataList = new ArrayList<>();
                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                    StCaseResRain stCaseResRain = new StCaseResRain();
                    stCaseResRain.setStnm(stStbprpBEntity.getStnm());
                    Double rain = 0.0;
                    stCaseResRain.setRain(rain);
                    stCaseResRain.setJd(stStbprpBEntity.getLgtd().doubleValue());
                    stCaseResRain.setWd(stStbprpBEntity.getLttd().doubleValue());
                    dataList.add(stCaseResRain);
                }
                stCaseResRainList.setDataList(dataList);
                stCaseResRainListList.add(stCaseResRainList);
            }
            stCaseResRainLists.setStCaseResRainList(stCaseResRainListList);
            return ResponseResult.success("查询成功", stCaseResRainLists);
        }else {
            stCaiyunPrecipitationHistoryDtos.forEach(stCaiyunPrecipitationHistoryDto -> {
                stnmList.add(stCaiyunPrecipitationHistoryDto.getStnm());
            });
            Map<Date, List<StCaiyunPrecipitationHistoryDto>> collect = stCaiyunPrecipitationHistoryDtos.stream().collect(Collectors.groupingBy(StCaiyunPrecipitationHistoryDto::getDrpTime));
            List<String>  stnmLists = stnmList.stream().distinct().collect(Collectors.toList());
            List<Map<String,Object>> stCaseResRainList = Lists.newArrayList();
            collect.forEach((k,v)->{
                stCaseResRainList.add(new HashMap<String,Object>(){{
                    put("time",simpleDateFormat.format(k));
                    put("dataList",v);
                }});
            });
            return ResponseResult.success("查询成功", new HashMap<String,Object>(){{
                put("stnmList",stnmLists);
                put("stCaseResRainList",stCaseResRainList);
            }});
        }

    }

}
