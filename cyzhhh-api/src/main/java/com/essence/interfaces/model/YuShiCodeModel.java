package com.essence.interfaces.model;

import com.essence.interfaces.vaild.Update;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class YuShiCodeModel {
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式 get/post
     */
    private String method;
    /**
     * 请求体
     */
    private Map<String, String> body;
}
