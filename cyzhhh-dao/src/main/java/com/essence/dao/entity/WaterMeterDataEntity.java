package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 远传水表 数据
 */
@Data
@TableName("st_water_meter_data")
public class WaterMeterDataEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 取水项目编号
     */
    private String wiuCd;
    /**
     * 取水日期yyyy-MM-dd
     */
    private Date dt;
    /**
     * 取水口编号
     */
    private String mpCd;
    /**
     * 当日水表止数
     */
    private Integer dayW;
    /**
     * 更新时间
     */
    private Date updateDate;
}
