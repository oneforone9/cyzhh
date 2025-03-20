package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 历史生态 河流 信息
 */
@Data
@TableName("t_organism_river_record")
public class OrganismRiverRecordEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 河流 名称
     */
    private String rvnm;
    /**
     * 河流长度
     */
    private BigDecimal rvLong;
    /**
     * 河流面积
     */
    private BigDecimal rvWide;
    /**
     * 年份
     */
    private String year;
}
