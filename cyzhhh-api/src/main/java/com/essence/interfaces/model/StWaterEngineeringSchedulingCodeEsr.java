package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 水系联调-工程调度调度指令记录返回实体
 *
 * @author majunjie
 * @since 2023-07-04 14:58:42
 */

@Data
public class StWaterEngineeringSchedulingCodeEsr extends Esr {

    private static final long serialVersionUID = -81099377070330113L;

    private String id;


    /**
     * 编码
     */
    private String stcd;


    /**
     * 水位站名称
     */
    private String stnm;


    /**
     * 闸名称
     */
    private String zbmc;


    /**
     * 摄像头编码code
     */
    private String videoCode;


    /**
     * 摄像头名称
     */
    private String videoName;


    /**
     * 调度时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date schedulingTime;


    /**
     * 参考指令
     */
    private String schedulingCode;


    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 数据状态0调令未发1河道所调令接收2负责人接收3调令执行4调令完成
     */
    private Integer state;


    /**
     * 现场负责人需要和用户ID
     */
    private String xcfzrUserId;


    /**
     * 现场负责人
     */
    private String xcfzr;


    /**
     * 现场负责人联系方式
     */
    private String xcfzrPhone;


    /**
     * 接收时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receptionTime;


    /**
     * 完成时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;


    /**
     * 局指下发时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;


    /**
     * 执行人员
     */
    private String dispose;


    /**
     * 执行情况
     */
    private String disposeCase;


    /**
     * 河流名称：测站所属河流的中文名称
     */
    private String rvnm;


    /**
     * 河道id
     */
    private String riverId;


    /**
     * 顺序
     */
    private Integer sn;


    /**
     * 预警类型
     */
    private Integer type;


    /**
     * 水闸 id
     */
    private String gateId;


    /**
     * 所属单位主键
     */
    private String unitId;


    /**
     * 调度指令
     */
    private String schedulingCodeNew;


    /**
     * 下发级别0逐级1越级
     */
    private String rank;


    /**
     * 河道所指令下发时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTimeR;
    /**
     * 调度主键
     */
    private String stId;
    /**
     * 调度处理-文件上传id
     */
    private List<FileBaseEsr> file;

}
