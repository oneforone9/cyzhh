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
 * @author bird
 * @since 2023-01-04 18:01:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_wellcollect_fee")
public class StWellcollectFeeDto extends Model<StWellcollectFeeDto> {

    private static final long serialVersionUID = 683680468186055725L;
        
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
        
    /**
     * 乡政府名称
     */
    @TableField("town_name")
    private String townName;
    
    /**
     * 类型 1-乡 2-街道
     */
    @TableField("type")
    private String type;
    
    /**
     * 乡管机井_实有
     */
    @TableField("xzjj_sy")
    private String xzjjSy;
    
    /**
     * 乡管机井_装表计量
     */
    @TableField("xzjj_zbjl")
    private String xzjjZbjl;
    
    /**
     * 乡管机井_实际发生
     */
    @TableField("xzjj_sjfs")
    private Double xzjjSjfs;
    
    /**
     * 乡管机井_实缴
     */
    @TableField("xzjj_sj")
    private Double xzjjSj;
    
    /**
     * 社会机井_实有
     */
    @TableField("shdwjj_sy")
    private String shdwjjSy;
    
    /**
     * 社会机井_装表计量
     */
    @TableField("shdwjj_zbjl")
    private String shdwjjZbjl;
    
    /**
     * 社会机井_实际发生
     */
    @TableField("shdwjj_sjfs")
    private Double shdwjjSjfs;
    
    /**
     * 社会机井_实缴
     */
    @TableField("shdwjj_sj")
    private Double shdwjjSj;
    
    /**
     * 统计年份
     */
    @TableField("tj_time")
    private String tjTime;
    /**
     * 季度
     */
    @TableField("tj_jd")
    private String tjJd;

}
