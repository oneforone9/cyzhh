package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author BINX
 * @since 2024-02-26 11:30:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_holiday_country")
public class StHolidayCountryDto extends Model<StHolidayCountryDto> {

    private static final long serialVersionUID = -10838921486843613L;
    
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 节假日名称
     */
    @TableField("holiday_name")
    private String holidayName;
    
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start")
    private Date start;
    
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end")
    private Date end;
    
    /**
     * 年份
     */
    @TableField("year")
    private String year;

}
