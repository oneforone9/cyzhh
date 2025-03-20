package com.essence.job.backjob.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhy
 * @since 2022/8/16 14:57
 */
@Data
public class HikApiParam implements Serializable {
    private static final long serialVersionUID = 1682899790109643508L;
    /**
     * 请求路径
     */
    private String apiPath;
    /**
     * 请求参数 必须json格式
     */
    private String paramBody;
}
