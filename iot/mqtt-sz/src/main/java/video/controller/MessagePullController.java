package video.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import video.common.StationDataDTO;
import video.config.MqttConfig;
import video.dto.WaterGatePushDTO;
import video.mqtt.MqttPushClient;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Classname MessagePullController
 * @Description
 * @Date 2022/10/11 15:14
 * @Created by essence
 */
@RestController
@RequestMapping("mqtt")
public class MessagePullController {
    @Resource
    MqttPushClient mqttPushClient ;
    @Resource
    MqttConfig mqttConfig;

    /**
     * @author cuiruix
     * @description    测试发布主题
     * @date 2021/8/16 15:04
     * @return RUtils
     */
    @PostMapping(value = "/publishTopic")
    public String publishTopic(@RequestParam(value = "topic" ,required = false)  String topic) throws MqttException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("did","2323232");
//        jsonObject.put("utime","tttt11");
//        StationDataDTO state = new StationDataDTO();
//        state.setAddr("addddddsss");
//        state.setAddrv("23");
//        state.setCtime("ctime");
//        state.setPid("pid");
//        state.setType("12");
//        List<StationDataDTO> list = new ArrayList<>();
//        list.add(state);
//        String string = JSON.toJSONString(list);
//        jsonObject.put("content",string);
//
//        String sendMessage = JSON.toJSONString(jsonObject);
//        if (StrUtil.isEmpty(topic)){
//            topic = "topic-water-sz";
//        }
//        MqttPushClient mqttPushClient = getMqttPushClient();
//        mqttPushClient.publish(0,false,topic,sendMessage);
//
//
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("did","11111111111111111111111111");
//        jsonObject1.put("utime","tttt11");
//        StationDataDTO state1 = new StationDataDTO();
//        state1.setAddr("addddddsss");
//        state1.setAddrv("23");
//        state1.setCtime("ctime");
//        state1.setPid("pid");
//        state1.setType("12");
//        List<StationDataDTO> list1 = new ArrayList<>();
//        list1.add(state1);
//        String string1 = JSON.toJSONString(list1);
//        jsonObject1.put("content",string1);
//
//        String sendMessage1 = JSON.toJSONString(jsonObject1);
        MqttPushClient mqttPushClient = getMqttPushClient();
        mqttPushClient.publish(0,false,"topic-rate-flow","12");
        return "OK";
    }


    /**
     * @author cuiruix
     * @description    测试发布主题
     * @date 2021/8/16 15:04
     * @return RUtils
     */
    @PostMapping(value = "/control/sz")
    public String publishSzControlTopic(@RequestBody WaterGatePushDTO waterGateDTO) throws MqttException {
        String string1 = JSON.toJSONString(waterGateDTO);
        FileUtil.appendUtf8String(string1,new File("mqtt-pre-punlish.txt"));
        MqttPushClient mqttPushClient = getMqttPushClient();

            mqttPushClient.publish(1,false,"topic-water-sz-control",string1);

        FileUtil.appendUtf8String(string1,new File("mqtt-punlish.txt"));
        return "OK";
    }

    /**
     * @author cuiruix
     * @description    测试发布主题
     * @date 2021/8/16 15:04
     * @return RUtils
     */
    @PostMapping(value = "/control/bz")
    public String publishBzControlTopic(@RequestBody WaterGatePushDTO waterGateDTO) throws MqttException {
        String string1 = JSON.toJSONString(waterGateDTO);
        FileUtil.appendUtf8String(string1,new File("bz-control-pre-punlish.txt"));
        MqttPushClient mqttPushClient = getMqttPushClient();
        mqttPushClient.publish(1,false,"topic-water-bz-control",string1);
        FileUtil.appendUtf8String(string1,new File("bz-control-punlish.txt"));
        return "OK";
    }

    @Bean
    public  MqttPushClient getMqttPushClient() {
        mqttPushClient.connect( mqttConfig.getHostUrl(), UUID.randomUUID().toString().replace("-",""), mqttConfig.getUsername(), mqttConfig.getPassword(), mqttConfig.getTimeout(), mqttConfig.getKeepalive(),mqttConfig.getDefaultTopic());

        // 以/#结尾表示订阅所有以test开头的主题
        List<String> topics = Arrays.asList( mqttConfig.getDefaultTopic().split(","));
        for (String topic : topics) {
            mqttPushClient.subscribe(topic, 0);
        }
        return mqttPushClient;
    }


}
