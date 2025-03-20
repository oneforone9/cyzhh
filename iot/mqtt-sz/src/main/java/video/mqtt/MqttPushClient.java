package video.mqtt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import video.common.StationDataDTO;
import video.dao.StWaterRateDao;
import video.dao.StWaterRateLatestDao;
import video.entity.StWaterRateEntity;
import video.entity.StWaterRateLatestDto;
import video.service.WaterGateService;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author cuirx
 * @Classname MqttPushClient
 * @Description
 * @Date 2022/10/11 15:03
 * @Created by essence
 */
@Component
@Slf4j
public class MqttPushClient {


    private static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }
    @Autowired
    private StWaterRateDao stWaterRateDao;
    @Resource
    private WaterGateService waterGateService;

    @Autowired
    StWaterRateLatestDao stWaterRateLatestDao;

    /**
     * 客户端连接
     *
     * @param host      ip+端口
     * @param clientID  客户端Id
     * @param username  用户名
     * @param password  密码
     * @param timeout   超时时间
     * @param keepalive 保留数
     */
    public void connect(String host, String clientID, String username, String password, int timeout, int keepalive,String defaultTopic) {
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            // 设置自动重连
            options.setAutomaticReconnect(true);
            MqttPushClient.setClient(client);
            try {
                // 此处使用的MqttCallbackExtended类而不是MqttCallback，是因为如果emq服务出现异常导致客户端断开连接后，重连后会自动调用connectComplete方法
                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        System.out.println("连接完成...");
                        try {
                            // 重连后要自己重新订阅topic，这样emq服务发的消息才能重新接收到，不然的话，断开后客户端只是重新连接了服务，并没有自动订阅，导致接收不到消息
                            List<String> topics = Arrays.asList(defaultTopic.split(","));
                            for (String topic : topics) {
                                client.subscribe(topic, 0);
                            }
                            log.info("订阅成功");
                        }catch (Exception e){
                            log.info("订阅出现异常:{}", e);
                        }
                    }
                    @SneakyThrows
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("失去连接....");
                        client.isConnected();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        saveMessage(topic,message);
                        // subscribe后得到的消息会执行到这里面
                        String payload = new String(message.getPayload());
                        System.out.println("接收消息主题 : " + topic);
                        System.out.println("接收消息Qos : " + message.getQos());
                        System.out.println("接收消息内容 : " + payload);
                        String clientId = client.getClientId();
                        System.out.println("接收clientId : " + clientId);

                        // 处理数据
                        System.out.println("---------------------------------------------》场站数据接入");
                        if (StrUtil.isNotEmpty(payload)){
                            if (topic.equals("topic-water-sz-control")){
                                FileUtil.appendUtf8String(payload,new File("sz-control.txt"));

                                log.info("推送的控制数据是"+payload);
                            }
                            if (topic.equals("topic-water-bz-control")){
                                FileUtil.appendUtf8String(payload,new File("bz-control.txt"));

                                log.info("推送的控制数据是"+payload);
                            }
                            if (topic.equals("topic-water-bz")){
//                                FileUtil.writeString(payload,new File("bz.txt"),"UTF-8");
                                waterGateService.dealWaterBz(payload);
                                System.out.println("测站id是============================>"+clientId);
                                System.out.println("接入到的泵站数据点位是===========================================》"+payload);
                                //删除今天之前的数据
                                log.info("接入到的水闸数据点位是===========================================》"+payload);
                            }
                            if (topic.equals("topic-water-sz")){
//                                FileUtil.writeString(payload,new File("sz.txt"),"UTF-8");
                                waterGateService.dealWaterGate(payload);
                                System.out.println("测站id是============================>"+clientId);
                                System.out.println("接入到的水闸数据点位是===========================================》"+payload);
                                //删除今天之前的数据
                                log.info("接入到的水闸数据点位是===========================================》"+payload);
                            }
                            if (topic.equals("topic-water-bj")) {
                                FileUtil.writeString(payload, new File("water_level_bj.txt"), "UTF-8");

                                log.info("接入到的水位报警数据是===========================================》" + payload);
                            } else {
                                List<StationDataDTO> stationDataDTOS = null;
                                try {
                                    if (topic.equals("topic-water-level") || topic.equals("topic-rate-flow") ){
                                        System.out.println("clientId======================>"+clientId);
                                        JSONObject jsonObject = JSONObject.parseObject(payload);
                                        String did = jsonObject.getString("did");
                                        String utime = jsonObject.getString("utime");
                                        JSONArray jsonArray = jsonObject.getJSONArray("content");
                                        stationDataDTOS = jsonArray.toJavaList(StationDataDTO.class);
                                        if (CollUtil.isNotEmpty(stationDataDTOS)){
                                            for (StationDataDTO stationDataDTO : stationDataDTOS) {
                                                StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
                                                BeanUtil.copyProperties(stationDataDTO,stWaterRateEntity);
                                                if ("shuiwei".equals(stWaterRateEntity.getAddr()) ){
                                                    stWaterRateEntity.setDid(did);
                                                    stWaterRateDao.insert(stWaterRateEntity);
                                                    dealLastWaterRateData(stWaterRateEntity);
                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    System.out.println("内容消息转换失败 : " + payload);
                                    return;
                                }
                                System.out.println("测站id是============================>" + clientId);
                                System.out.println("接入到的数据点位是===========================================》"+stationDataDTOS.toString());
                                log.info("接入到的数据点位是===========================================》"+stationDataDTOS.toString());
                            }
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete....");
                    }
                });
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dealLastWaterRateData(StWaterRateEntity stWaterRateEntity){
        //fix 填充最新的
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("did",stWaterRateEntity.getDid());
        List<StWaterRateLatestDto> stWaterRateLatestDtos = stWaterRateLatestDao.selectList(wrapper);
        StWaterRateLatestDto stWaterRateLatestDto = new StWaterRateLatestDto();
        stWaterRateLatestDto.setAddr(stWaterRateEntity.getAddr());
        stWaterRateLatestDto.setAddrv(stWaterRateEntity.getAddrv());
        stWaterRateLatestDto.setCtime(stWaterRateEntity.getCtime());
        stWaterRateLatestDto.setDid(stWaterRateEntity.getDid());
        if (CollUtil.isNotEmpty(stWaterRateLatestDtos)){
            //此处更新流量站的最新数据通过站点did
            stWaterRateLatestDao.update(stWaterRateLatestDto,wrapper);
        }else {
            stWaterRateLatestDao.insert(stWaterRateLatestDto);
        }
    }

    /**
     * 发布
     *
     * @param qos         连接方式
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            log.error("topic not exist");
        }
        MqttDeliveryToken token;

        token = mTopic.publish(message);
        token.waitForCompletion();

    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {
        log.info("==============开始订阅主题=========" + topic);
        try {
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void saveMessage(String topic, MqttMessage message){
        String payload = new String(message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(payload);
        try {
            String did = jsonObject.getString("did");
            String utime = jsonObject.getString("utime");
            stWaterRateDao.saveMessage(IdWorker.get32UUID(),topic,did,payload,utime, DateUtil.now());
        } catch (Exception e) {
            stWaterRateDao.saveMessage(IdWorker.get32UUID(),topic,"",payload,"", DateUtil.now());
        }
    }

}
