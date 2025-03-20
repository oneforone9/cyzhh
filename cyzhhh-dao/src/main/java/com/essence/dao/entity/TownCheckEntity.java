package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 乡镇考核 断面
 */
@Data
@TableName("t_town_check")
public class TownCheckEntity implements Serializable {
    /**
     * 断面id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 区域
     */
    private String area;
    /**
     * 河流
     */
    private String street;
    /**
     * 河流
     */
    private String river;
    /**
     * 断面名称
     */
    private String selection;

}
