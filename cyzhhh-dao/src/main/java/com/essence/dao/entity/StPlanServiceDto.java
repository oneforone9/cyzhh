package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 闸坝维护计划基础表实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_service")
public class StPlanServiceDto extends Model<StPlanServiceDto> {

    private static final long serialVersionUID = 713723268712672896L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 设备设施名称
     */
    @TableField("equipment_name")
    private String equipmentName;
    
    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 父id
     */
    @TableField("pid")
    private Integer pid;
    
    /**
     * 日常维护内容
     */
    @TableField("service_content")
    private String serviceContent;
    
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

}
