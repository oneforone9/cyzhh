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
 * 用水量实体
 *
 * @author BINX
 * @since 2023-01-04 17:18:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "usewell_water")
public class UseWaterDto extends Model<UseWaterDto> {

    private static final long serialVersionUID = -92081062549878164L;
        
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 日期
     */
    @TableField("date")
    private String date;
    
    /**
     * 用水量
     */
    @TableField("use_info")
    private String useInfo;

    private String updateTime;

}
