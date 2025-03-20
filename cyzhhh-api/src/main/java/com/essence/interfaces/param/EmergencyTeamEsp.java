package com.essence.interfaces.param;


import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 抢险队伍参数实体
 *
 * @author tyy
 * @since 2024-07-17 19:39:51
 */

@Data
public class EmergencyTeamEsp extends Esp {

    private static final long serialVersionUID = 322122008655876096L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 车组编号
     */
    @ColumnMean("group_num")
    private String groupNum;
    /**
     * 负责人
     */
    @ColumnMean("leader")
    private String leader;
    /**
     * 手机
     */
    @ColumnMean("phone_num")
    private String phoneNum;
    /**
     * 总人数
     */
    @ColumnMean("total_num")
    private Integer totalNum;
    /**
     * 车号顺序
     */
    @ColumnMean("car_order")
    private String carOrder;
    /**
     * 车牌
     */
    @ColumnMean("car_num")
    private String carNum;
    /**
     * 排水量
     */
    @ColumnMean("water_discharge")
    private BigDecimal waterDischarge;
    /**
     * 级别(1市 2区 3驻区 4街乡)
     */
    @ColumnMean("grade")
    private Integer grade;
    /**
     * 详细地址
     */
    @ColumnMean("detailed_address")
    private String detailedAddress;
    /**
     * 抢险区域
     */
    @ColumnMean("emergency_area")
    private String emergencyArea;
    /**
     * 主要设备
     */
    @ColumnMean("major_equipment")
    private String majorEquipment;
    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private BigDecimal lgtd;
    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private BigDecimal lttd;


}
