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
 * 实体
 *
 * @author majunjie
 * @since 2025-01-08 14:13:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "view_video_xj")
public class ViewVideoXjDto extends Model<ViewVideoXjDto> {

    private static final long serialVersionUID = 678449294027493196L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private Integer id;
    /**
     * 编码
     */
    @TableField("code")
    private String code;
    /**
     * 班组名称
     */  @TableField("bzmc")
    private String bzmc;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 地址
     */  @TableField("address")
    private String address;
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
    /**
     * 河系名称
     */
    @TableField("river_name")
    private String riverName;
    /**
     * 巡检单位id
     */
    @TableField("xjdw_id")
    private String xjdwId;
    /**
     * 单位名称
     */
    @TableField("dwmc")
    private String dwmc;
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
     * 巡检内容
     */
    @TableField("xjnr")
    private String xjnr;
    /**
     * 巡检日期
     */
    @TableField("xj_rq")
    private String xjRq;
    /**
     * 计划主键
     */
    @TableField("jhid")
    private String jhid;
    /**
     * 班组id
     */
    @TableField("bz_id")
    private String bzId;
    /**
     * 海康HIV,华为HUAWEI,内网NW,萤石云YSYC,YUSY宇视云,专线ZX
     */
    @TableField("source")
    private String source;
}
