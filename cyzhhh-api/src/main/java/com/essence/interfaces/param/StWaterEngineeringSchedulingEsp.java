package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
* 水系联调-工程调度 参数实体
*
* @authorBINX
* @since 2023年5月14日 下午4:59:32
*/
@Data
public class StWaterEngineeringSchedulingEsp extends Esp {

    private static final long serialVersionUID = 768288460678839641L;
    
    /**
    * 
    */
    @ColumnMean("id")
    private String id;
    
    /**
    * 编码
    */
    @ColumnMean("stcd")
    private String stcd;
    
    /**
    * 水位站名称
    */
    @ColumnMean("stnm")
    private String stnm;
    
    /**
    * 闸名称
    */
    @ColumnMean("zbmc")
    private String zbmc;
    
    /**
    * 摄像头编码code
    */
    @ColumnMean("video_code")
    private String videoCode;
    
    /**
    * 摄像头名称
    */
    @ColumnMean("video_name")
    private String videoName;
    
    /**
    * 调度时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("scheduling_time")
    private Date schedulingTime;
    
    /**
    * 参考指令
    */
    @ColumnMean("scheduling_code")
    private String schedulingCode;
    
    /**
    * 创建时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("create_time")
    private Date createTime;
    /**
     * 数据状态0调令未发1河道所调令接收2河道所调令待下发3负责人接收4调令执行5调令完成
     */
    @ColumnMean("state")
    private Integer state;

    /**
     * 现场负责人
     */
    @ColumnMean("xcfzr")
    private String xcfzr;
    /**
     * 现场负责人联系方式
     */
    @ColumnMean("xcfzr_phone")
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    @ColumnMean("xcfzr_user_id")
    private String xcfzrUserId;
    /**
     * 接收时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("reception_time")
    private Date receptionTime;
    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("finish_time")
    private Date finishTime;
    /**
     * 局指下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("order_time")
    private Date orderTime;
    /**
     * 河道所下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("order_time_r")
    private Date orderTimeR;
    /**
     * 执行人员
     */
    @ColumnMean("dispose")
    private String dispose;
    /**
     * 执行情况
     */
    @ColumnMean("dispose_case")
    private String disposeCase;
    /**
     * 河流名称：测站所属河流的中文名称
     */
    @ColumnMean("rvnm")
    private String rvnm;
    /**
     * 河道id
     */
    @ColumnMean("river_id")
    private String riverId;
    /**
     * 顺序
     */
    @ColumnMean("sn")
    private Integer sn;
    /**
     * 水闸 id
     */
    @ColumnMean("gate_id")
    private String gateId;
    /**
     * 所属单位 id
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 调度指令
     */
    @ColumnMean("scheduling_code_new")
    private String schedulingCodeNew;
    /**
     * 下发级别0逐级1越级
     */
    @ColumnMean("rank")
    private String rank;
}
