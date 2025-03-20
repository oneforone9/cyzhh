package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.dao.entity.StPlanOperateDto;
import com.essence.dao.entity.StPlanOperateRejectDto;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 工单基础信息表返回实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:24
 */

@Data
public class WorkorderBaseEsrYh extends Esr {

    private static final long serialVersionUID = -50577681575542846L;


    /**
     * 主键
     */
    private String id;

    /**
     * 工单负责人
     */
    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5维修 6养护)
     */
    private String orderType;

    /**
     * 工单编号
     */
    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报)
     */
    private String orderSource;

    /**
     * 工单名称
     */
    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     */
    private String distributeType;

    /**
     * 作业区域
     */
    private String location;
    /**
     * 事件主键
     * @mock 12321
     */
    private String eventId;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     */
    private Integer timeSpan;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）
     */
    private String orderLabel;

    /**
     * 工单描述
     */
    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     */
    private String isAttention;

    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 交办至 1 北京金都园林绿化有限公司 2、北京金河生态科技有限公司 3、北京鸿运通生态科技有限公司 4、北京朝水环境治理有限公司 5、河道管理一所 6、河道管理二所
     */
    private String sendTo;
    /**
     * 所属河道id
     */
    private String realId;
    /**
     * 所属河道id
     */
    private String realName;

    /**
     * 开始巡河时间
     */
    private String startWorkTime;

    /**
     * 工单完成巡查时间
     */
    private String endWorkTime;

    /**
     * 第三方服务公司主键id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 处理时效
     */
    private String dealEffect;

    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    private String sttp;
    /**
     * 设备设施名称
     */
    private String equipmentName;
    /**
     *日常维护内容
     */
    private String serviceContent;

    /**
     *测站名称
     */
    private String stnm;

    /**
     *手机号
     */
    private String phone;

    /**
     * 作业录
     */
    List<StPlanOperateDto> operateList;

    /**
     * 驳回记录
     */
    List<StPlanOperateRejectDto> operateRejectList;

    /**
     * 工单处理时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;
}
