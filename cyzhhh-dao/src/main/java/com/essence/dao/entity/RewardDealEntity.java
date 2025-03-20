package com.essence.dao.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 接诉即办理
 */
@Data
@TableName("t_reward_deal")
public class RewardDealEntity extends Model<RewardDealEntity> {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.UUID)
    private String id;
    /**
     * 来电时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 3)
    private String callTime;
    /**
     * 来源渠道
     */
    @ExcelProperty(index = 12)
    private String source;
    /**
     * 大类
     */
    @ExcelProperty(index = 14)
    private String bigKind;
    /**
     * 小类
     */
    @ExcelProperty(index = 15)
    private String smallKind;
    /**
     * 细类
     */
    @ExcelProperty(index = 16)
    private String detailKind;
    @ExcelIgnore
    private String update_time;
}
