package com.essence.job.executor.rain;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.WeatherForecastDao;
import com.essence.dao.entity.WeatherForecastEntity;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class WarningMsgHandler {
    @Resource
    private WeatherForecastDao weatherForecastDao;

    @XxlJob("WarningMsgHandler")
    public void execute() throws UnirestException {
        getWarning();
    }


    public  void getWarning() throws UnirestException {
        HttpResponse<String> response = Unirest.post("http://10.248.8.4:7778/DataInterface.asmx/GetWarning")
                .header("content-type", "text/xml")
                .header("cache-control", "no-cache")
                .header("postman-token", "4c1dc444-5920-d500-77e5-aa489384b20a")
                .body("<soap:Envelope \n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n    xmlns:targetNamespace=\"ZZY\"\n    xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n\n    <soap:Body>\n        <targetNamespace:GetWarning  xmlns=\"ZZY\">\n       \n        </targetNamespace:GetWarning>\n    </soap:Body>\n</soap:Envelope>")
                .asString();
//
        FileUtil.writeString(response.getBody(),new File("warning.txt"),"UTF-8");

//        String s = FileUtil.readString(new File("C:\\Users\\essence\\Desktop\\cy\\warn.txt"), "UTF-8");
        JSONObject resp = JSONUtil.parseObj(response.getBody());
        JSONObject warningClass = resp.getJSONObject("WarningClass");
        String nowWarning = warningClass.getStr("NowWarning");
        JSONObject warnings = warningClass.getJSONObject("Warnings");
        JSONArray warningInfos = null;
        try {
            warningInfos = warnings.getJSONArray("WarningInfo");
            //发布平台
            for (Object obj : warningInfos) {
                JSONObject warningInfo = (JSONObject)obj;
                String publishDepartment = warningInfo.getStr("PublishPartment");
                String publishTime = warningInfo.getStr("PublishTime");
                //气象类型
                String type = warningInfo.getStr("Class");
                //等级
                String level = warningInfo.getStr("Level");
                //状态
                String status = warningInfo.getStr("Status");
                //内容
                String context = warningInfo.getStr("Context");
                //防护
                String defence = warningInfo.getStr("Defence");
                //信息
                String msg = warningInfo.getStr("Msg");
                //信息
                String icon = warningInfo.getStr("ICON");
                WeatherForecastEntity weatherForecastEntity = new WeatherForecastEntity();
                weatherForecastEntity.setMsg(msg);
                weatherForecastEntity.setIcon(icon);
                weatherForecastEntity.setContext(context);
                weatherForecastEntity.setDefence(defence);
                weatherForecastEntity.setStatus(status);
                weatherForecastEntity.setWeatherType(type);
                weatherForecastEntity.setWeatherLevel(level);
                weatherForecastEntity.setNowWaring(nowWarning);
                String dateStr = formatRTime(publishTime);
                weatherForecastEntity.setPublishTime(dateStr);
                weatherForecastEntity.setPublishDepartment(publishDepartment);

                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("publish_time",weatherForecastEntity.getPublishTime());
                wrapper.eq("publish_department",weatherForecastEntity.getPublishDepartment());
                WeatherForecastEntity weatherForecast = weatherForecastDao.selectOne(wrapper);
                if (weatherForecast!= null){
                    weatherForecastDao.update(weatherForecastEntity,wrapper);
                }else {
                    weatherForecastDao.insert(weatherForecastEntity);
                }
            }
        } catch (Exception e) {
            System.out.println("是单个对象");
            JSONObject warningInfo = warnings.getJSONObject("WarningInfo");
            String publishDepartment = warningInfo.getStr("PublishPartment");
            String publishTime = warningInfo.getStr("PublishTime");
            //气象类型
            String type = warningInfo.getStr("Class");
            //等级
            String level = warningInfo.getStr("Level");
            //状态
            String status = warningInfo.getStr("Status");
            //内容
            String context = warningInfo.getStr("Context");
            //防护
            String defence = warningInfo.getStr("Defence");
            //信息
            String msg = warningInfo.getStr("Msg");
            //信息
            String icon = warningInfo.getStr("ICON");
            WeatherForecastEntity weatherForecastEntity = new WeatherForecastEntity();
            weatherForecastEntity.setMsg(msg);
            weatherForecastEntity.setIcon(icon);
            weatherForecastEntity.setContext(context);
            weatherForecastEntity.setDefence(defence);
            weatherForecastEntity.setStatus(status);
            weatherForecastEntity.setWeatherType(type);
            weatherForecastEntity.setWeatherLevel(level);
            weatherForecastEntity.setNowWaring(nowWarning);
            String dateStr = formatRTime(publishTime);
            weatherForecastEntity.setPublishTime(dateStr);
            weatherForecastEntity.setPublishDepartment(publishDepartment);

            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("publish_time",weatherForecastEntity.getPublishTime());
            wrapper.eq("publish_department",weatherForecastEntity.getPublishDepartment());
            WeatherForecastEntity weatherForecast = weatherForecastDao.selectOne(wrapper);

            if (weatherForecast!= null){
                weatherForecastDao.update(weatherForecastEntity,wrapper);
            }else {
                weatherForecastDao.insert(weatherForecastEntity);
            }
        }

    }


    /**
     * 还原时间
     * @param str
     * @return
     */
    public static String formatRTime(String str){
        //对日期进行格式化    String end = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String substring = str.substring(0, 19);
        String replace = substring.replace("T", " ");

        return replace;
    }

}
