package com.essence.dao.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * 用水户 基础信息,重复的取水权人 代表几个取水口实体
 *
 * @author BINX
 * @since 2024-01-03 14:04:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "user_water_base_info")
public class UserWaterBaseInfoDto extends Model<UserWaterBaseInfoDto> {

    private static final long serialVersionUID = -62406583121194354L;
    
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField("user_name")
    private String userName;
    
    /**
     * 电子证号
     */
    @TableField("ele_num")
    private String eleNum;
    
    /**
     * 许可水量
     */
    @TableField("promise_quantity")
    private String promiseQuantity;

    /**
     * 1非居民 2居民生活 3农业生产者 4园林绿地 5地热井 6水源热泵
     */
    @TableField("type")
    private String type;

    /**
     * 机井编码
     */
    @TableField("code")
    private String code;

    /**
     * 街乡
     */
    @TableField("jiedao")
    private String jiedao;

    /**
     * 村
     */
    @TableField("cun")
    private String cun;

    /**
     * 机井编号
     */
    @TableField("num")
    private String num;

    /**
     * 机井位置
     */
    @TableField("location")
    private String location;

    /**
     * 井深
     */
    @TableField("deep")
    private String deep;

    /**
     * 经度
     */
    @TableField("jd")
    private String jd;

    /**
     * 纬度
     */
    @TableField("wd")
    private String wd;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

}
