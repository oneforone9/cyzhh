package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 河流生态表
 */
@Data
@TableName("t_river_ecology")
public class RiverEcologyEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 河流id
     */
    private String riverId;
    /**
     * 基础信息
     */
    private String baseInfo;
    /**
     * 概化图地址
     */
    private String ghtUrl;
}
