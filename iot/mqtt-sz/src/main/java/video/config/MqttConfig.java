package video.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import video.mqtt.MqttPushClient;

import java.util.Arrays;
import java.util.List;

/**
 * @author cuirx
 * @Classname MqttConfig
 * @Description TODO
 * @Date 2022/10/11 14:55
 * @Created by essence
 */

@Component
@ConfigurationProperties("spring.mqtt")
@Setter
@Getter
public class MqttConfig {



    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接地址
     */
    private String hostUrl;
    /**
     * 客户Id
     */
    private String clientId;
    /**
     * 默认连接话题
     */
    private String defaultTopic;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 保持连接数
     */
    private int keepalive;




}
