package com.essence.dao.entity;

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
 * 水系联调-工程调度调度指令记录实体
 *
 * @author majunjie
 * @since 2023-07-04 14:57:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_engineering_scheduling_code")
public class StWaterEngineeringSchedulingCodeDto extends Model<StWaterEngineeringSchedulingCodeDto> {

    private static final long serialVersionUID = -43864841359438786L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 编码
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 水位站名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 闸名称
     */
    @TableField("zbmc")
    private String zbmc;
    
    /**
     * 摄像头编码code
     */
    @TableField("video_code")
    private String videoCode;
    
    /**
     * 摄像头名称
     */
    @TableField("video_name")
    private String videoName;
    
    /**
     * 调度时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("scheduling_time")
    private Date schedulingTime;
    
    /**
     * 参考指令
     */
    @TableField("scheduling_code")
    private String schedulingCode;
    
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 数据状态0调令未发1河道所调令接收2负责人接收3调令执行4调令完成
     */
    @TableField("state")
    private Integer state;
    
    /**
     * 现场负责人需要和用户ID
     */
    @TableField("xcfzr_user_id")
    private String xcfzrUserId;
    
    /**
     * 现场负责人
     */
    @TableField("xcfzr")
    private String xcfzr;
    
    /**
     * 现场负责人联系方式
     */
    @TableField("xcfzr_phone")
    private String xcfzrPhone;
    
    /**
     * 接收时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("reception_time")
    private Date receptionTime;
    
    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("finish_time")
    private Date finishTime;
    
    /**
     * 局指下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;
    
    /**
     * 执行人员
     */
    @TableField("dispose")
    private String dispose;
    
    /**
     * 执行情况
     */
    @TableField("dispose_case")
    private String disposeCase;
    
    /**
     * 河流名称：测站所属河流的中文名称
     */
    @TableField("rvnm")
    private String rvnm;
    
    /**
     * 河道id
     */
    @TableField("river_id")
    private String riverId;
    
    /**
     * 顺序
     */
    @TableField("sn")
    private Integer sn;
    
    /**
     * 预警类型
     */
    @TableField("type")
    private Integer type;
    
    /**
     * 水闸 id
     */
    @TableField("gate_id")
    private String gateId;
    
    /**
     * 所属单位主键
     */
    @TableField("unit_id")
    private String unitId;
    
    /**
     * 调度指令
     */
    @TableField("scheduling_code_new")
    private String schedulingCodeNew;
    
    /**
     * 下发级别0逐级1越级
     */
    @TableField("rank")
    private String rank;
    
    /**
     * 河道所指令下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time_r")
    private Date orderTimeR;
    /**
     * 调度主键
     */
    @TableField("st_id")
    private String stId;
}
