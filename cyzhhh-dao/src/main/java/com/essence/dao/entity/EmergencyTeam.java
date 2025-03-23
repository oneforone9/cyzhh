package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * EmergencyTeam实体类
 *
 * @author zhy
 * @since 2024-07-17 19:32:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "emergency_team")
public class EmergencyTeam extends Model<EmergencyTeam> {
    private static final long serialVersionUID = 691768228240698476L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车组编号
     */
    @TableField("group_num")
    private String groupNum;

    /**
     * 负责人
     */
    @TableField("leader")
    private String leader;

    /**
     * 手机
     */
    @TableField("phone_num")
    private String phoneNum;

    /**
     * 总人数
     */
    @TableField("total_num")
    private Integer totalNum;

    /**
     * 车号顺序
     */
    @TableField("car_order")
    private String carOrder;

    /**
     * 车牌
     */
    @TableField("car_num")
    private String carNum;

    /**
     * 排水量
     */
    @TableField("water_discharge")
    private BigDecimal waterDischarge;

    /**
     * 级别(1市 2区 3驻区 4街乡)
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 详细地址
     */
    @TableField("detailed_address")
    private String detailedAddress;

    /**
     * 抢险区域
     */
    @TableField("emergency_area")
    private String emergencyArea;

    /**
     * 主要设备
     */
    @TableField("major_equipment")
    private String majorEquipment;

    /**
     * 经度
     */
    @TableField("lgtd")
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    private BigDecimal lttd;


    /**
     * 组织机构
     */
    @TableField("corp_id")
    private String corpId;
}
