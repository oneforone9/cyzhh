package com.essence.dao.entity.alg;

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
 * 防汛调度-方案基础表实体
 *
 * @author BINX
 * @since 2023-04-17 16:29:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_case_base_info")
public class StCaseBaseInfoDto extends Model<StCaseBaseInfoDto> {

    private static final long serialVersionUID = 842426345151593678L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 方案名称
     */
    @TableField("case_name")
    private String caseName;
    
    /**
     * 预报开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("forecast_start_time")
    private Date forecastStartTime;
    
    /**
     * 预热时间（期）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("pre_hot_time")
    private Date preHotTime;
    
    /**
     * 预见期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("pre_see_time")
    private Date preSeeTime;
    
    /**
     * 步长
     */
    @TableField("step")
    private Integer step;
    
    /**
     * 雨型
     */
    @TableField("rain_type")
    private String rainType;
    
    /**
     * 雨型名称
     */
    @TableField("rain_type_name")
    private String rainTypeName;
    
    /**
     * 降雨总量
     */
    @TableField("rain_total")
    private String rainTotal;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */
    private String status;
    /**
     * 雨形 id
     */
    private String rainId;
    /**
     * 文件地址
     */
    private String modelPath;
    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    @TableField("model_type")
    private Integer modelType;

    /**
     * 预报方式 1 气象预测 2 设计雨型
     */
    private String forecastType;
}
