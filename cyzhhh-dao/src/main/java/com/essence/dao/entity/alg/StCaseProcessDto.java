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
 * 方案执行过程表-存放入参等信息实体
 *
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_case_process")
public class StCaseProcessDto extends Model<StCaseProcessDto> {

    private static final long serialVersionUID = -17746563291316386L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 方案id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 入参站点类型
     */
    @TableField("type")
    private String type;
    
    /**
     * 入参站点id 或者
     */
    @TableField("stcd")
    private String stcd;
    /**
     * 序列名称
     */
    private String serializeName;
    
    /**
     * 入参站点名称
     */
    @TableField("st_name")
    private String stName;
    
    /**
     * 入参数的时间点位数据 
     */
    @TableField("input_data")
    private String inputData;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

}
