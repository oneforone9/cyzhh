package com.essence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_area_health_data")
public class AreaHealthDataInfoEntity implements Serializable {
    /**
     *id
     */
    @TableId(value = "id", type = IdType.UUID)
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
