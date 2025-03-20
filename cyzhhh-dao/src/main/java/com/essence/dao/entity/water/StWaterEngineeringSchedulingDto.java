package com.essence.dao.entity.water;

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

import java.math.BigDecimal;
import java.util.Date;

/**
* 水系联调-工程调度
*
* @authorBINX
* @since 2023年5月14日 下午4:59:32
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_engineering_scheduling")
public class StWaterEngineeringSchedulingDto extends Model<StWaterEngineeringSchedulingDto> {

    private static final long serialVersionUID = 768288460678839641L;

    /**
    * 
    */
    @TableId(value = "id", type = IdType.UUID)
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
    @TableField("scheduling_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date schedulingTime;

    /**
    * 参考指令
    */
    @TableField("scheduling_code")
    private String schedulingCode;

    /**
    * 创建时间
    */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 数据状态0调令未发1河道所调令接收2河道所调令待下发3负责人接收4调令执行5调令完成
     */
    @TableField("state")
    private Integer state;
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
     * 现场负责人需要和用户ID
     */
    @TableField("xcfzr_user_id")
    private String xcfzrUserId;
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
     * 河道所下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time_r")
    private Date orderTimeR;
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
     * 1蓝色 2黄色 3橙色 预警
     */
    @TableField("type")
    private Integer type;
    /**
     * 实时水位
     */
    private transient BigDecimal momentRiverPosition;

    /**
     * 经度
     */
    private transient BigDecimal lttd;

    /**
     * 纬度
     */
    private transient BigDecimal lgtd;
    /**
     * 水闸 id
     */
    @TableField("gate_id")
    private String gateId;
    /**
     * 所属单位 id
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
}
