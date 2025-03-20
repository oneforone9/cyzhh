package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备巡检会议室巡检计划实体
 *
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_hys_jh")
public class XjHysJhDto extends Model<XjHysJhDto> {

    private static final long serialVersionUID = 169595897642622790L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 巡检内容
     */
    @TableField("xjnr")
    private String xjnr;
    /**
     * 班组名称
     */  @TableField("bzmc")
    private String bzmc;
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
     * 巡检日期巡检日期(1,2,3,4,5,6,7)
     */
    @TableField("xj_rq")
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
