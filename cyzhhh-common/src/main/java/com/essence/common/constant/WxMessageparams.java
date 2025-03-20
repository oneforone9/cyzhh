package com.essence.common.constant;

import java.util.Map;

/**
 *
 * @Author: liwy
 * @CreateTime: 2023-08-22  18:07
 */
public class WxMessageparams {

    /**
     * 接收者（用户）的 openid
     */
    private String touser;
    /**
     * 所需下发的订阅模板id
     */
    private String template_id;

    /**
     * 点击消息卡片跳转地址
     */
    private String page;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    private String miniprogram_state;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object
     */
    private Map<String, Map> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    public void setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
    }

    public Map<String, Map> getData() {
        return data;
    }

    public void setData(Map<String, Map> data) {
        this.data = data;
    }

}
