package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 河流影像表
 */
@Data
@TableName("t_river_image")
public class RiverImageEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    private Integer tRiverEcologyId;

    private String imageYear;

    private String imageUrl;
}
