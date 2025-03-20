package com.essence.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName RemoteProperties
 * @Description 配置类
 * @Version 1.0
 **/

@Configuration
@ConfigurationProperties(prefix = "remote", ignoreUnknownFields = false)
public class RemoteProperties {
    /**
     * @Description 海淀雨晴等值面
     **/
    private String hdyqdzm;
    private String hdyqdzmShp;
    private String hdyqdzmPic;
    private String hdyqdzmPicNgPath;
    private String hdyqdzmPicShpPath;
    private String amapShpPath;
    private String amapShpPicPath;
    /**
     * @Description 气象局url:port
     **/
    private String weatherBureauUrl;
    /**
     * 转发气象局内部ip
     */
    private String weatherBureauUrlInside;
    /**
     * 转发气象局外部ip
     */
    private String weatherBureauUrlExternal;
    /**
     * @Description 气象局type 与 url联合使用
     **/
    private String weatherBureauType;

    /**
     * @Description 气象局  获取细网格预报图片数据type 与 url联合使用
     */
    private String fineGridForecastType;

    /**
     * @Description 气象局  是否启用  type 与 url联合使用
     **/
    private Boolean weatherBureauActive;

    /**
     * @Description gif 转 png 后自己的资源访问路径
     * @Author xzc
     * @Date 16:10 2021/3/11
     * @return
     **/
    private String gridUrlSelf;
    private String gridLocalBasePathSelf;//E:/pictures/

    /**
     * @Description 雨量数据【水务局，气象局，智慧灯杆】
     **/
    private String qxjrainfallall;
    /**
     * @Description 智慧灯杆账号
     * @Date 10:18 2021/7/27
     **/
    private String username;
    /**
     * @Description 智慧灯杆密码
     * @Date 10:18 2021/7/27
     **/
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQxjrainfallall() {
        return qxjrainfallall;
    }

    public void setQxjrainfallall(String qxjrainfallall) {
        this.qxjrainfallall = qxjrainfallall;
    }

    public String getHdyqdzmPicShpPath() {
        return hdyqdzmPicShpPath;
    }

    public void setHdyqdzmPicShpPath(String hdyqdzmPicShpPath) {
        this.hdyqdzmPicShpPath = hdyqdzmPicShpPath;
    }

    public String getHdyqdzmPicNgPath() {
        return hdyqdzmPicNgPath;
    }

    public void setHdyqdzmPicNgPath(String hdyqdzmPicNgPath) {
        this.hdyqdzmPicNgPath = hdyqdzmPicNgPath;
    }

    public String getHdyqdzmPic() {
        return hdyqdzmPic;
    }

    public void setHdyqdzmPic(String hdyqdzmPic) {
        this.hdyqdzmPic = hdyqdzmPic;
    }

    public String getHdyqdzmShp() {
        return hdyqdzmShp;
    }

    public void setHdyqdzmShp(String hdyqdzmShp) {
        this.hdyqdzmShp = hdyqdzmShp;
    }

    public String getGridLocalBasePathSelf() {
        return gridLocalBasePathSelf;
    }

    public void setGridLocalBasePathSelf(String gridLocalBasePathSelf) {
        this.gridLocalBasePathSelf = gridLocalBasePathSelf;
    }

    public String getGridUrlSelf() {
        return gridUrlSelf;
    }

    public void setGridUrlSelf(String gridUrlSelf) {
        this.gridUrlSelf = gridUrlSelf;
    }

    public String getFineGridForecastType() {
        return fineGridForecastType;
    }

    public void setFineGridForecastType(String fineGridForecastType) {
        this.fineGridForecastType = fineGridForecastType;
    }

    public String getHdyqdzm() {
        return hdyqdzm;
    }

    public void setHdyqdzm(String hdyqdzm) {
        this.hdyqdzm = hdyqdzm;
    }

    public String getWeatherBureauUrl() {
        return weatherBureauUrl;
    }

    public String getWeatherBureauUrlInside() {
        return weatherBureauUrlInside;
    }

    public void setWeatherBureauUrlInside(String weatherBureauUrlInside) {
        this.weatherBureauUrlInside = weatherBureauUrlInside;
    }

    public String getWeatherBureauUrlExternal() {
        return weatherBureauUrlExternal;
    }

    public void setWeatherBureauUrlExternal(String weatherBureauUrlExternal) {
        this.weatherBureauUrlExternal = weatherBureauUrlExternal;
    }

    public void setWeatherBureauUrl(String weatherBureauUrl) {
        this.weatherBureauUrl = weatherBureauUrl;
    }

    public String getWeatherBureauType() {
        return weatherBureauType;
    }

    public void setWeatherBureauType(String weatherBureauType) {
        this.weatherBureauType = weatherBureauType;
    }

    public Boolean getWeatherBureauActive() {
        return weatherBureauActive;
    }

    public void setWeatherBureauActive(Boolean weatherBureauActive) {
        this.weatherBureauActive = weatherBureauActive;
    }

    public String getAmapShpPath() {
        return amapShpPath;
    }

    public void setAmapShpPath(String amapShpPath) {
        this.amapShpPath = amapShpPath;
    }

    public String getAmapShpPicPath() {
        return amapShpPicPath;
    }

    public void setAmapShpPicPath(String amapShpPicPath) {
        this.amapShpPicPath = amapShpPicPath;
    }

    @Override
    public String toString() {
        return "RemoteProperties{" +
                "hdyqdzm='" + hdyqdzm + '\'' +
                ", weatherBureauUrl='" + weatherBureauUrl + '\'' +
                ", weatherBureauType='" + weatherBureauType + '\'' +
                ", fineGridForecastType='" + fineGridForecastType + '\'' +
                ", weatherBureauActive=" + weatherBureauActive +
                '}';
    }
}
