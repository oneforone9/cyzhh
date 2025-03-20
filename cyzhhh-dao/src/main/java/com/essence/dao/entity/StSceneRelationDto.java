package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_scene_relation")
public class StSceneRelationDto extends Model<StSceneRelationDto> {

    private static final long serialVersionUID = -96090773527884339L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    
    /**
     * 场景基本表id
     */
    @TableField("scene_id")
    private Integer sceneId;

    
    /**
     * 摄像头主键id
     */
    @TableField("video_id")
    private Integer videoId;
        
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;


    /**
     * 编码
     */
    @TableField(exist = false)
    private String code;

    /**
     * 摄像头名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 摄像机类型
     */
    @TableField(exist = false)
    private String cameraType;


    /**
     * 视频状态(0 离线 1 在线)
     */
    @TableField(exist = false)
    private String status;

    /**
     * 经度
     */
    @TableField(exist = false)
    private Double lgtd;

    /**
     * 纬度
     */
    @TableField(exist = false)
    private Double lttd;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    @TableField(exist = false)
    private String source;

    /**
     * 单位id
     */
    @TableField(exist = false)
    private String unitId;


}
