package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 抢险队伍更新实体
 *
 * @author zhy
 * @since 2024-07-17 19:32:42
 */
@Data
public class EmergencyTeamEsu extends Esu {

    private static final long serialVersionUID = 363939487685263369L;

    private Integer id;

    /**
     * 车组编号
     */
    private String groupNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 手机
     */
    private String phoneNum;

    /**
     * 总人数
     */
    private Integer totalNum;

    /**
     * 车号顺序
     */
    private String carOrder;

    /**
     * 车牌
     */
    private String carNum;

    /**
     * 排水量
     */
    private BigDecimal waterDischarge;

    /**
     * 级别(1市 2区 3驻区 4街乡)
     */
    private Integer grade;

    /**
     * 详细地址
     */
    private String detailedAddress;

    /**
     * 抢险区域
     */
    private String emergencyArea;

    /**
     * 主要设备
     */
    private String majorEquipment;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;


    /**
     * 组织机构
     */
    private String corpId;

}
