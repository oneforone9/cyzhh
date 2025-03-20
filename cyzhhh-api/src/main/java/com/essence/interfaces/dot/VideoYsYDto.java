package com.essence.interfaces.dot;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
@Data
public class VideoYsYDto implements Serializable {
    private String id;
    private String code;
    private String name;
    private String url;
    /**
     * 1 在线  0 不在线
     */
    private Integer status;
    private String reaId;

    private String accessToken;


    private String cameraType;
}
