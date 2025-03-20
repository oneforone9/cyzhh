package com.essence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用水户取水量实体
 *
 * @author BINX
 * @since 2023-01-04 17:50:30
 */
@Data
@TableName(value = "user_water")
public class UserWaterDto  implements Serializable {

        
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;
    
    /**
     * 取水口
     */
    @TableField("out_num")
    private String outNum;
    
    /**
     * 许可水量
     */
    @TableField("water")
    private String water;
    
    /**
     * 检测水量
     */
    @TableField("mn_water")
    private String mnWater;
    
    /**
     * 年月yyyy-MM-dd
     */
    @TableField("date")
    private String date;

    private String updateTime;

}
