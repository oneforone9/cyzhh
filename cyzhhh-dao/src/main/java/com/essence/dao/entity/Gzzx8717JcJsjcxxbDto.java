package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author BINX
 * @since 2023-05-23 17:22:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "gzzx_8717_jc_jsjcxxb")
public class Gzzx8717JcJsjcxxbDto extends Model<Gzzx8717JcJsjcxxbDto> {

    private static final long serialVersionUID = -21252533719311127L;
    
    /**
     * 测站代码
     */
    @TableId(value = "czdm", type = IdType.INPUT)
    private String czdm;
    
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("sj")
    private Date sj;
        
    /**
     * 积水(m)
     */
    @TableField("js")
    private BigDecimal js;
    
    /**
     * 水务码
     */
    @TableField("swm")
    private String swm;
    
    /**
     * 入库时间-自动生成
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("rksj")
    private Date rksj;

}
