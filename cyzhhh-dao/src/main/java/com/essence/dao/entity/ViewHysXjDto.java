package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实体
 *
 * @author majunjie
 * @since 2025-01-09 10:22:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "view_hys_xj")
public class ViewHysXjDto extends Model<ViewHysXjDto> {

    private static final long serialVersionUID = 718557920531129283L;

    /**
     * 主键
     */
    @TableField("id")
    private String id;
    /**
     * 会议室名称
     */
    @TableField("mc")
    private String mc;
    /**
     * 班组名称
     */  @TableField("bzmc")
    private String bzmc;
    /**
     * 生成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;
    /**
     * 主键
     */
    @TableField("jhid")
    private String jhid;
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
    /**
     * 单位名称
     */
    @TableField("dwmc")
    private String dwmc;
    /**
     * 经度
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    private Double lttd;
}
