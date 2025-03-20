package com.essence.common.utils;

import com.essence.common.dto.QixiangImageDto;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取气象实况图片数据
 * @Author: liwy
 * @CreateTime: 2022-11-07  16:41
 */
public class ObtainQixiangImage {

    /**
     * 获取卫星云图，刷新频率30分钟和1小时
     * @return
     */
    public List<QixiangImageDto> ObtainSatellitCloudImage(String path){

        CloseableHttpClient httpclient = HttpClients.createDefault();
        //HttpGet httpget = new HttpGet("http://www.nmc.cn/publish/satellite/fy2.htm");
        HttpGet httpget = new HttpGet(path);
        String html = "";
        try {
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
            html = EntityUtils.toString(responseEntity,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("col-xs-12 time ");
        List<QixiangImageDto> qixiangImageDtoList = new ArrayList<QixiangImageDto>();
        QixiangImageDto meteorologicalImage = null;
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            meteorologicalImage = new QixiangImageDto();
            String imageUrl = elements.get(size-(i+1)).attr("data-img");
            String imageDate = elements.get(size-(i+1)).text();
            meteorologicalImage.setImageUrl(imageUrl);
            meteorologicalImage.setImageDate(imageDate);
            qixiangImageDtoList.add(meteorologicalImage);
        }
        elements = document.getElementsByClass("col-xs-12 time actived");
        size = elements.size();
        for (int i = 0; i < size; i++) {
            meteorologicalImage = new QixiangImageDto();
            String imageUrl = elements.get(size-(i+1)).attr("data-img");
            String imageDate = elements.get(size-(i+1)).text();
            meteorologicalImage.setImageUrl(imageUrl);
            meteorologicalImage.setImageDate(imageDate);
            qixiangImageDtoList.add(meteorologicalImage);
        }
        return qixiangImageDtoList;
    }


    /**
     * 获取雷达回波图，刷新频率6分钟
     * @return
     */
    public List<QixiangImageDto> ObtainRadarMap(String path){

        CloseableHttpClient httpclient = HttpClients.createDefault();
        //HttpGet httpget = new HttpGet("http://www.nmc.cn/publish/radar/huabei.html");
        HttpGet httpget = new HttpGet(path);
        String html = "";
        try {
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
            html = EntityUtils.toString(responseEntity,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("col-xs-12 time ");
        List<QixiangImageDto> qixiangImageDtoList = new ArrayList<QixiangImageDto>();
        QixiangImageDto meteorologicalImage = null;
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            meteorologicalImage = new QixiangImageDto();
            String imageUrl = elements.get(size-(i+1)).attr("data-img");
            String imageDate = elements.get(size-(i+1)).text();
            meteorologicalImage.setImageUrl(imageUrl);
            meteorologicalImage.setImageDate(imageDate);
            qixiangImageDtoList.add(meteorologicalImage);
        }
        elements = document.getElementsByClass("col-xs-12 time actived");
        size = elements.size();
        for (int i = 0; i < size; i++) {
            meteorologicalImage = new QixiangImageDto();
            String imageUrl = elements.get(size-(i+1)).attr("data-img");
            String imageDate = elements.get(size-(i+1)).text();
            meteorologicalImage.setImageUrl(imageUrl);
            meteorologicalImage.setImageDate(imageDate);
            qixiangImageDtoList.add(meteorologicalImage);
        }
        return qixiangImageDtoList;
    }
}
