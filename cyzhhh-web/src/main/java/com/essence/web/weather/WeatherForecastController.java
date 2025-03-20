package com.essence.web.weather;

import com.alibaba.fastjson.JSONObject;
import com.essence.common.dto.weather.QixiangImageDto;
import com.essence.common.dto.weather.ReturnForecastDto;
import com.essence.common.utils.HttpClientUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.service.properties.RemoteProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 气象
 */
@RestController
@RequestMapping("weather")
@Slf4j
public class WeatherForecastController {
    @Autowired
    private RemoteProperties remoteProperties;

    private static final CloseableHttpClient httpclient;

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(15000).build();
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * 获取细网格预报图片数据
     *
     * @return com.essence.hdfxdp.util.SystemSecurityMessage
     * @Author huangxiaoli
     * @Description
     * @Date 14:10 2020/8/4
     * @Param []
     **/
    @RequestMapping(value = "/obtainFineGridForecastMap", method = RequestMethod.GET)
    public ResponseResult obtainFineGridForecastMap() {
        try {
            List<QixiangImageDto> obtainMap = getFineGridForecast();
            return ResponseResult.success("获取细网格预报图片数据成功！", obtainMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("error", "获取细网格预报图片数据失败!");
        }

    }


    protected List<QixiangImageDto> getFineGridForecast() {
        List<QixiangImageDto> result = new ArrayList<>();
        String s = "";
        try {
            s = HttpClientUtil.sendGet(httpclient,
                    remoteProperties.getWeatherBureauUrl() + "/weatherBureau/findFineGridForecast?type=" + remoteProperties.getFineGridForecastType());
            ReturnForecastDto dto = JSONObject.parseObject(s, ReturnForecastDto.class);
            log.debug(dto.getCode());
            result = dto.getData();
            Collections.reverse(result);
            //替换请求路径信息
            for (QixiangImageDto qixiangImageDto : result) {
                String imageUrl = qixiangImageDto.getImageUrl();
                qixiangImageDto.setImageUrl(imageUrl.replace("/srv/program", remoteProperties.getWeatherBureauUrl()));
                imageUrl = qixiangImageDto.getImageUrl();
                String imageName = imageUrl.substring(imageUrl.lastIndexOf("/"), imageUrl.indexOf(".GIF"));
                String s1 = LocalDate.now().getYear() + File.separator + LocalDate.now().getMonthValue();
                readImgToLocal(imageUrl, remoteProperties.getGridLocalBasePathSelf() + File.separator + s1, imageName + ".PNG");
                String newUrl = remoteProperties.getGridUrlSelf() + File.separator + s1 + File.separator + imageName + ".PNG";
                qixiangImageDto.setImageUrl(newUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void readImgToLocal(String srcPath, String directory, String fileName) {
        URL source;
        try {
            source = new URL(srcPath);
            File file = new File((directory));
            if (!file.exists()) {
                file.mkdirs();
            }
            File destination = FileUtils.getFile(directory, fileName);
            FileUtils.copyURLToFile(source, destination);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
