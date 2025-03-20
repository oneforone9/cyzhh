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
 * 水系联调-工程调度实体
 *
 * @author majunjie
 * @since 2023-07-03 17:24:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_engineering_scheduling_lead")
public class StWaterEngineeringSchedulingLeadDto extends Model<StWaterEngineeringSchedulingLeadDto> {

    private static final long serialVersionUID = 110610270247273244L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 所长id
     */
    @TableField("s_user_id")
    private String sUserId;
    
    /**
     * 所长名称人
     */
    @TableField("sz")
    private String sz;
    
    /**
     * 联系方式
     */
    @TableField("sz_phone")
    private String szPhone;

}
