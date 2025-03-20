package com.essence.job.executor.rain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RainDateOrgDao;
import com.essence.dao.RainTokenDao;
import com.essence.dao.entity.RainDateOrgDto;
import com.essence.dao.entity.RainTokenEntity;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 调用接口-原始雨量入库
 */
@Component
public class RainFallNumHandler {

    @Resource
    private RainDateOrgDao rainDateOrgDao;

    @Resource
    private RainTokenDao rainTokenDao;

    private String token = "9BDE88E4DF38D08A7368E979C9C1EA8F";

    /**
     * 获取 token
     */
    String authenticationUrl = "http://10.248.8.4:7778/DataInterface.asmx?op=Login";


    @XxlJob("RainFallNumHandler")
    public void demoJobHandler() throws Exception {
        getToken();
        getRainData();
    }

    /**
     * 获取登陆使用的 token
     */
    @Transactional
    public void getToken(){
        HashMap<String, Object> WorkflowRequestInfomap = new HashMap<>();
        WorkflowRequestInfomap.put("UserName","水务局");
        WorkflowRequestInfomap.put("pwdMD5","Swj12345!@#$%");
        SoapClient soapClient = SoapClient.create(authenticationUrl)
                .setMethod("targetNamespace:Login", "ZZY")
                .setParams(WorkflowRequestInfomap);
        String send = soapClient.send(true);
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(send);

        Map<String, Object> msgInfo = (Map<String, Object>) stringObjectMap.get("soap:Body");
        XxlJobHelper.log("soap:Body"+msgInfo.keySet()+ "==========>"+msgInfo.values());

        Map<String, Object> loginResponse = (Map<String, Object>) msgInfo.get("LoginResponse");
        XxlJobHelper.log("LoginResponse"+loginResponse.keySet()+ "==========>"+loginResponse.values());

        Map<String, Object> loginResult = (Map<String, Object>) loginResponse.get("LoginResult");
        XxlJobHelper.log("LoginResult"+loginResult.keySet()+ "==========>"+loginResult.values());
        Object msg = loginResult.get("msg");
        String msgStr = msg.toString();
        if (!msgStr.equals("already Login!")){
            String tokenKey = (String) loginResult.get("TokenKey");
            this.token = tokenKey;
            FileUtil.writeString(tokenKey,new File("tokenKey.txt"),"UTF-8");
            rainTokenDao.delete(new QueryWrapper<>());
            RainTokenEntity rainTokenEntity = new RainTokenEntity();
            rainTokenEntity.setToken(token);
            rainTokenDao.insert(rainTokenEntity);
        }else {
            RainTokenEntity rainTokenEntity1 = rainTokenDao.selectOne(new QueryWrapper<>());
            if (rainTokenEntity1 != null){
                this.token = rainTokenEntity1.getToken();
            }else {
                System.out.println("数据库无tokenkey");
            }

        }
    }


    /**
     * 获取雨量数据 fix 2023-05-18 14:15注释提供的分钟雨量采用 新增的小时雨量接口去获取每一分钟的雨量信息
     */
    public void getRainData() throws UnirestException {
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetMinute(date, -10);

        //对日期进行格式化
        String end = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String start = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");

        String s = formatTime(start);
        String e = formatTime(end);
        FileUtil.writeString("start"+s+"==============="+"end"+e+"token"+token,new File("param.txt"),"UTF-8");

        HttpResponse<String> response = Unirest.post("http://10.248.8.4:7778/DataInterface.asmx?op=GetRain")
                .header("content-type", "text/xml")
                .header("cache-control", "no-cache")
                .header("postman-token", "7e50d11d-557a-cc67-d1f8-37856569d4cd")
                .body("<soap:Envelope \n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n    xmlns:targetNamespace=\"ZZY\"\n    xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n\n    <soap:Body>\n        <targetNamespace:GetRain  xmlns=\"ZZY\">\n        \t<TokenKey>"+token+"</TokenKey>\n           <dt1>"+s+"</dt1>\n           <dt2>"+e+"</dt2>\n        </targetNamespace:GetRain>\n    </soap:Body>\n</soap:Envelope>")
                .asString();
        XxlJobHelper.log("response================"+response.getBody());
        FileUtil.writeString(response.getBody(), new File("1.txt"), "UTF-8");
        JSONObject resp = JSONUtil.parseObj(response.getBody());

        JSONObject envelope = resp.getJSONObject("soap:Envelope");
        JSONObject body = envelope.getJSONObject("soap:Body");
        JSONObject rainResponse = body.getJSONObject("GetRainResponse");
        JSONObject rainResult = rainResponse.getJSONObject("GetRainResult");
        JSONObject rainsH = rainResult.getJSONObject("HHRains");
        JSONArray rainClass = rainsH.getJSONArray("RainClass");

        List<RainDateOrgDto> rainDateDtos = new ArrayList<>();
        for (Object aClass : rainClass) {
            JSONObject entries1 = JSONUtil.parseObj(aClass);
            String stationID =entries1.getStr("StationID");
            String obsTime =entries1.getStr("ObsTime");
            String s1 = formatRTime(obsTime);
            DateTime parse = DateUtil.parse(s1);
            String hhRain = entries1.getStr("HHRain");
            RainDateOrgDto rainDateDto = new RainDateOrgDto();
            rainDateDto.setStationID(stationID);
            rainDateDto.setHhRain(hhRain);

            rainDateDto.setDate(parse);
            rainDateDtos.add(rainDateDto);
        }
        if (CollUtil.isNotEmpty(rainDateDtos)){
            dealDate(rainDateDtos);
        }
    }

    public void dealDate(List<RainDateOrgDto> rainDateDtos){
        if (CollUtil.isNotEmpty(rainDateDtos)){
            for (RainDateOrgDto rainDateDto : rainDateDtos) {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date",rainDateDto.getDate());
                wrapper.eq("station_id",rainDateDto.getStationID());
                RainDateOrgDto rainDateDtos1 = rainDateOrgDao.selectOne(wrapper);
                if ( rainDateDtos1 == null){
                    rainDateOrgDao.insert(rainDateDto);
                }else {
                    rainDateOrgDao.update(rainDateDto,wrapper);
                }
            }
        }
    }

    public static String formatTime(String str){
        return str.substring(0, 19).replace(" ", "T");
    }

    /**
     * 还原时间
     * @param str
     * @return
     */
    public static String formatRTime(String str){
        return str.substring(0, 19).replace("T", " ");
    }



}
