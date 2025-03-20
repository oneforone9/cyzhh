package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
* 水系联调-工程调度 更新实体
*
* @authorBINX
* @since 2023年5月14日 下午4:59:32
*/
@Data
public class StWaterEngineeringSchedulingEsu extends Esu {

    private static final long serialVersionUID = 768288460678839641L;

    /**
    * 
    */
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
     * 数据状态0调令未发1河道所调令接收2河道所调令待下发3负责人接收4调令执行5调令完成
     */
    private Integer state;
    /**
     * 现场负责人
     */
    private String xcfzr;
    /**
     * 现场负责人联系方式
     */
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    private String xcfzrUserId;
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
     * 河道所下发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTimeR;
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
     * 调度处理-文件上传id
     */
    private List<String> file;
    /**
     * 所属单位 id
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
}
