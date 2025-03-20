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
 * 实体
 *
 * @author majunjie
 * @since 2023-04-20 15:36:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_gate")
public class StWaterGateDto extends Model<StWaterGateDto> {

    private static final long serialVersionUID = 872469363249411948L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    @TableField("pid")
    private String pid;
    
    @TableField("type")
    private String type;
    
    /**
     * 采集名称
     */
    @TableField("addr")
    private String addr;
    
    /**
     * 采集数据数值
     */
    @TableField("addrv")
    private String addrv;
    
    /**
     * 采集时间
     */
    @TableField("ctime")
    private String ctime;
    
    /**
     * sn码  实际机器标识码与clientid对应
     */
    @TableField("did")
    private String did;

}
