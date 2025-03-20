package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备巡检摄像头巡检计划实体
 *
 * @author majunjie
 * @since 2025-01-08 22:46:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_sxt_jh")
public class XjSxtJhDto extends Model<XjSxtJhDto> {

    private static final long serialVersionUID = -29740740288949406L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 摄像头id
     */
    @TableField("sxt_id")
    private Integer sxtId;
    /**
     * 班组名称
     */  @TableField("bzmc")
    private String bzmc;
    /**
     * 巡检内容
     */
    @TableField("xjnr")
    private String xjnr;

    /**
     * 负责人
     */
    @TableField("fzr")
    private String fzr;

    /**
     * 负责人id
     */
    @TableField("fzr_id")
    private String fzrId;

    /**
     * 巡检日期
     */

    @TableField(value = "xj_rq", updateStrategy = FieldStrategy.IGNORED)
    private String xjRq;

    /**
     * 巡检单位id
     */
    @TableField("xjdw_id")
    private String xjdwId;

    /**
     * 班组id
     */
    @TableField("bz_id")
    private String bzId;

}
