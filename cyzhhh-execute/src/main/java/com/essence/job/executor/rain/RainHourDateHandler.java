package com.essence.job.executor.rain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.exception.BusinessException;
import com.essence.dao.RainHourDataDao;
import com.essence.dao.RainTokenDao;
import com.essence.dao.entity.RainHourDataEntity;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调用接口-小时雨量入库
 */
@Component
public class RainHourDateHandler {

    @Resource
    private RainHourDataDao rainHourDataDao;

    @Resource
    private RainTokenDao rainTokenDao;

    @XxlJob("RainHourDateHandler")
    public void execute() throws UnirestException {
        String jobParam = XxlJobHelper.getJobParam();
        Date date = null;
        if (StringUtils.isNotBlank(jobParam)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = format.parse(jobParam);
            } catch (ParseException e) {
                throw new BusinessException("时间参数格式错误: yyyy-MM-dd HH:mm:ss");
            }
        }
        getRainHourData(date);
    }

    /**
     * 获取小时雨量
     *
     * @throws UnirestException
     */
    public void getRainHourData(Date date) throws UnirestException {
        if (date == null) {
            date = new Date();
        }
        //对日期进行格式化
        String s = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss").substring(0, 19).replace(" ", "T");
        String token = rainTokenDao.selectOne(null).getToken();
        HttpResponse<String> response = Unirest.post("http://10.248.8.4:7778/DataInterface.asmx?op=SatTotalRain")
                .header("content-type", "text/xml")
                .header("cache-control", "no-cache")
                .header("postman-token", "33bfe86d-7bcb-1ab2-52aa-d25ae0465981")
                .body("<soap:Envelope \n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n    xmlns:targetNamespace=\"ZZY\"\n    xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n\n    <soap:Body>\n        <targetNamespace:SatTotalRain  xmlns=\"ZZY\">\n        \t<TokenKey>"+token+"</TokenKey>\n           <dt>"+s+"</dt>\n        </targetNamespace:SatTotalRain>\n    </soap:Body>\n</soap:Envelope>")
                .asString();

        System.out.println("response================"+response.getBody());

        JSONObject resp = JSONUtil.parseObj(response.getBody());
        JSONObject envelope = resp.getJSONObject("soap:Envelope");
        JSONObject soapBody = envelope.getJSONObject("soap:Body");
        JSONObject responseObj = soapBody.getJSONObject("SatTotalRainResponse");
        JSONObject result = responseObj.getJSONObject("SatTotalRainResult");
        JSONObject rains = result.getJSONObject("rains");
        JSONArray rainClass = rains.getJSONArray("Rains");
        List<RainHourDataEntity> rainDateDtos = new ArrayList<>();
        String today = DateUtil.format(date, "yyyy-MM-dd");

        for (Object aClass : rainClass) {
            JSONObject entries1 = JSONUtil.parseObj(aClass);
            String stationID =entries1.getStr("StationID");
            String obsTime =entries1.getStr("ObsTime");
            DateTime parse = DateUtil.parse(obsTime);
            String rain1H = entries1.getStr("Rain1H");
            String rain3H = entries1.getStr("Rain3H");
            String rain6H = entries1.getStr("Rain6H");
            String rain12H = entries1.getStr("Rain12H");
            String rain24H = entries1.getStr("Rain24H");

            RainHourDataEntity RainHourDataEntity = new RainHourDataEntity();
            RainHourDataEntity.setDate(today);
            RainHourDataEntity.setStationId(stationID);
            RainHourDataEntity.setHourOne(rain1H);
            RainHourDataEntity.setHourThree(rain3H);
            RainHourDataEntity.setHourSix(rain6H);
            RainHourDataEntity.setHourTwelve(rain12H);
            RainHourDataEntity.setHourTwenty(rain24H);
            rainDateDtos.add(RainHourDataEntity);
        }
        dealDate(rainDateDtos);
    }

    public void dealDate(List<RainHourDataEntity> rainDateDtos){
        if (CollUtil.isNotEmpty(rainDateDtos)){
            for (RainHourDataEntity rainDateDto : rainDateDtos) {
                String stationId = rainDateDto.getStationId();
                String date = rainDateDto.getDate();
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("station_id",stationId);
                queryWrapper.eq("date",date);
                List<RainHourDataEntity> rainHourDataEntities = rainHourDataDao.selectList(queryWrapper);
                if (CollUtil.isNotEmpty(rainHourDataEntities)){
                    rainHourDataDao.update(rainDateDto,queryWrapper);
                }else {
                    rainHourDataDao.insert(rainDateDto);
                }
            }
        }
    }

}
