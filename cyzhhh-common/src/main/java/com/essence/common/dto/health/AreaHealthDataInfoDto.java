package com.essence.common.dto.health;


import lombok.Data;

import java.io.Serializable;

@Data

public class AreaHealthDataInfoDto implements Serializable {
    /**
     *id
     */
    private String id;
    /**
     * 红码
     */
    private Integer cntRedCode;
    /**
     * 黄码
     */
    private Integer cntYellowCode;
    /**
     * 标志 1 区政 2 管属
     */
    private Integer type;
    /**
     * 河流名称
     */
    private String adnm;
    /**
     * 绿码
     */
    private Integer cntGreenCode;

    private String ym;
}
