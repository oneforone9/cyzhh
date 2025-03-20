package com.essence.job.satellite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.QixiangImageDto;
import com.essence.common.utils.ObtainQixiangImage;
import com.essence.dao.RadarImagesLdDao;
import com.essence.dao.RadarImagesLddzDao;
import com.essence.dao.RadarImagesWxDao;
import com.essence.dao.entity.RadarImagesLdDto;
import com.essence.dao.entity.RadarImagesLddzDto;
import com.essence.dao.entity.RadarImagesWxDto;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 定时任务同步雷达图
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/23 9:39
 */
@Component
@Slf4j
public class ObtainRadarJobHandler {

    //获取卫星云图
    @Value("${nmc.wx.path}")
    private String nmcWxPath;
    //获取雷达回波图华北地区的
    @Value("${nmc.ld.path}")
    private String nmcLdPath;
    //获取雷达回波图单站大兴的
    @Value("${nmc.lddz.path}")
    private String nmcLddzPath;

    @Value("${nmc.save.path}")
    private String savePath;

    @Value("${nmc.upload.url}")
    private String uploadUrl;

    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    RadarImagesLdDao radarImagesLdDao;
    @Autowired
    RadarImagesLddzDao radarImagesLddzDao;
    @Autowired
    RadarImagesWxDao radarImagesWxDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 定时任务同步雷达图/卫星云图
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void demoJobHandler() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行定时任务同步雷达图/卫星云图任务,结束.", BACK_JOB);
            return;
        }
        mkdirs();
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            log.info("同步雷达图开始...");
            doExecuteLdImage();
        });
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(()->{
            log.info("获取华北地区雷达回波图完成");
            doExecuteLddzImage();
        });
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(()->{
            log.info("获取单站大兴雷达回波图完成");
            doExecuteWxImage();
        });
        CompletableFuture<Void> completable = CompletableFuture.allOf(task1, task2, task3);
        try {
            completable.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("获取卫星云图完成");
        log.info("同步雷达图结束.");
    }

    public void mkdirs() {
        File dir = new File(savePath + fileDateFormat.format(new Date()) + File.separator);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 华北地区雷达回波图
     */
    @SneakyThrows
    public void doExecuteLdImage() {
        ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
        List<QixiangImageDto> ldImageDtos = obtainQixiangImage.ObtainRadarMap(nmcLdPath);
        QueryWrapper<RadarImagesLdDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("image_date");
        List<RadarImagesLdDto> radarImagesLdDtos = radarImagesLdDao.selectList(queryWrapper);
        RadarImagesLdDto radarImagesLdDto = null;
        if (0 < radarImagesLdDtos.size()) {
            radarImagesLdDto = radarImagesLdDtos.get(0);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (QixiangImageDto dto : ldImageDtos) {
            String dateFormat = Calendar.getInstance().get(Calendar.YEAR) + "/" + dto.getImageDate();
            if (null != radarImagesLdDto && format.parse(dateFormat).after(radarImagesLdDto.getImageDate())) {
                RadarImagesLdDto entity = new RadarImagesLdDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".PNG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesLdDao.insert(entity);
            } else if (null == radarImagesLdDto) {
                RadarImagesLdDto entity = new RadarImagesLdDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".PNG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesLdDao.insert(entity);
            }
        }
    }

    /**
     * 单站大兴雷达回波图
     */
    @SneakyThrows
    public void doExecuteLddzImage() {
        ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
        List<QixiangImageDto> lddzImageDtos = obtainQixiangImage.ObtainRadarMap(nmcLddzPath);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        QueryWrapper<RadarImagesLddzDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("image_date");
        List<RadarImagesLddzDto> radarImagesLddzDtos = radarImagesLddzDao.selectList(queryWrapper);
        RadarImagesLddzDto radarImagesLddzDto = null;
        if (0 < radarImagesLddzDtos.size()) {
            radarImagesLddzDto = radarImagesLddzDtos.get(0);
        }
        for (QixiangImageDto dto : lddzImageDtos) {
            String dateFormat = Calendar.getInstance().get(Calendar.YEAR) + "/" + dto.getImageDate();
            if (null != radarImagesLddzDto && format.parse(dateFormat).after(radarImagesLddzDto.getImageDate())) {
                RadarImagesLddzDto entity = new RadarImagesLddzDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".PNG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesLddzDao.insert(entity);
            } else if (null == radarImagesLddzDto) {
                RadarImagesLddzDto entity = new RadarImagesLddzDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".PNG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesLddzDao.insert(entity);
            }
        }
    }

    /**
     * 卫星云图
     */
    @SneakyThrows
    public void doExecuteWxImage() {
        ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
        List<QixiangImageDto> wxImageDtos = obtainQixiangImage.ObtainRadarMap(nmcWxPath);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        QueryWrapper<RadarImagesWxDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("image_date");
        List<RadarImagesWxDto> radarImagesWxDtos = radarImagesWxDao.selectList(queryWrapper);
        RadarImagesWxDto radarImagesWxDto = null;
        if (0 < radarImagesWxDtos.size()) {
            radarImagesWxDto = radarImagesWxDtos.get(0);
        }
        for (QixiangImageDto dto : wxImageDtos) {
            String dateFormat = Calendar.getInstance().get(Calendar.YEAR) + "/" + dto.getImageDate();
            if (null != radarImagesWxDto && format.parse(dateFormat).after(radarImagesWxDto.getImageDate())) {
                RadarImagesWxDto entity = new RadarImagesWxDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".JPG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesWxDao.insert(entity);
            } else if (null == radarImagesWxDto) {
                RadarImagesWxDto entity = new RadarImagesWxDto();
                String image = savePath + fileDateFormat.format(new Date()) + File.separator + dto.getImageUrl().replace("http://image.nmc.cn/product/", "").replace("/", "").replace("_", "").replace(".PNG?v=", "").replace(".JPG?v=", "") + ".JPG";
                saveFiles(dto.getImageUrl(), image);
                String imageUrl = uploadPost(image);
                entity.setImageUrl(imageUrl);
                entity.setImageDate(format.parse(dateFormat));
                radarImagesWxDao.insert(entity);
            }
        }
    }

    @SneakyThrows
    public void saveFiles(String origen, String target) {
        try (
                InputStream inputStream = new URL(origen).openConnection().getInputStream();
                FileOutputStream outputStream = new FileOutputStream(target)
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error copying resource", e);
        }
    }

    @SneakyThrows
    public String uploadPost(String filePath) {

        // 设置分隔符
        String boundary = UUID.randomUUID().toString();

        // 创建URL对象
        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // 构建请求体
        StringBuilder requestBodyBuilder = new StringBuilder();
        requestBodyBuilder.append("--").append(boundary).append("\r\n");
        requestBodyBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + new File(filePath).getName() + "\"\r\n"); // 文件名
        requestBodyBuilder.append("Content-Type: application/octet-stream\r\n\r\n");

        // 将请求体写入输出流
        connection.getOutputStream().write(requestBodyBuilder.toString().getBytes());

        // 读取文件内容并将其写入输出流
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            connection.getOutputStream().write(buffer, 0, bytesRead);
        }
        connection.getOutputStream().flush();
        inputStream.close();

        // 结束分隔符
        connection.getOutputStream().write(("\r\n--" + boundary + "--\r\n").getBytes());

        // 获取响应结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder responseBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        System.out.println(responseBuilder.toString());
        JSONObject jsonObject = JSON.parseObject(String.valueOf(responseBuilder));
        JSONObject result = JSON.parseObject(String.valueOf(jsonObject.get("result")));
        String fileUrl = String.valueOf(result.get("fileUrl"));
        return fileUrl;
    }
}
