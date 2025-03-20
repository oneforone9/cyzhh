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
 * @since 2023-05-10 17:31:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_side_relation")
public class StSideRelationDto extends Model<StSideRelationDto> {

    private static final long serialVersionUID = 473377173427914517L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 编码
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 闸坝名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 闸坝DD-闸  SB-水坝
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 站点编码流量站
     */
    @TableField("stcdz")
    private String stcdz;
    
    /**
     * 站点名称
     */
    @TableField("stnmz")
    private String stnmz;
    
    /**
     * 流量站水位站ZQ水位站ZZ
     */
    @TableField("sttpz")
    private String sttpz;

}
