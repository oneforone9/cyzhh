package com.essence.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoYsYDto implements Serializable {
    private String id;
    private String code;
    private String name;
    private String url;
    /**
     * 1 在线  2 不在线
     */
    private Integer onlineStatus;
    private String reaId;

    private String accessToken;
}
