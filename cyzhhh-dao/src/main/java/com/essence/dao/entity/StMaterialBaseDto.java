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
 * 防汛物资基础表实体
 *
 * @author liwy
 * @since 2023-04-13 14:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_material_base")
public class StMaterialBaseDto extends Model<StMaterialBaseDto> {

    private static final long serialVersionUID = -43000835642635071L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 物资名称
     */
    @TableField("material_name")
    private String materialName;
    
    /**
     * 库存
     */
    @TableField("amount")
    private Integer amount;
    
    /**
     * 单位
     */
    @TableField("danwei")
    private String danwei;
    
    /**
     * 存放点位
     */
    @TableField("location")
    private String location;
    
    /**
     * 单位id
     */
    @TableField("unit_id")
    private String unitId;
    
    /**
     * 管理单位
     */
    @TableField("unit_name")
    private String unitName;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

}
