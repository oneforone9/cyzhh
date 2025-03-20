package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("clear_week_cheek")
public class ClearWeekCheckEntity implements Serializable {
    /**
     *id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 河流名称
     */
    private String rvnm;
    /**
     * 名称
     */
    private String name;
    /**
     * 时间
     */
    private String time;
    /**
     * 氨氮
     */
    private BigDecimal nh4 ;

    private BigDecimal cod ;
    /**
     * 磷标准
     */
    private BigDecimal pStander ;
    /**
     * 氨氮 标准
     */
    private BigDecimal nh4Stander;
    /**
     * cod 标准
     */
    private BigDecimal codStander ;
    /**
     * 磷
     */
    private BigDecimal p ;
    /**
     * 透明度
     */
    private BigDecimal transmission;
    /**
     * 透明度标准
     */
    private BigDecimal transmissionStander;
    /**
     * 一年的第几个周
     */
    private Integer weekOfYear;
    /**
     * 年份
     */
    private String year;

    private Date update_time;

}
