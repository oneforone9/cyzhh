package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 实体
 *
 * @author majunjie
 * @since 2023-04-20 17:35:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "v_gate_data")
public class VGateDataDto extends Model<VGateDataDto> {

    private static final long serialVersionUID = -25953785845748601L;

    @TableField("id")
    private String id;
    @TableField("pid")
    private String pid;
    @TableField("type")
    private String type;
    /**
     * 采集名称
     */
    @TableField("addr")
    private String addr;
    /**
     * 采集数据数值
     */
    @TableField("addrv")
    private String addrv;
    /**
     * 采集时间
     */
    @TableField("ctime")
    private String ctime;
    /**
     * sn码  实际机器标识码与clientid对应
     */
    @TableField("did")
    private String did;
    /**
     * 站点编码
     */
    @TableField("stcd")
    private String stcd;


    @TableField(exist = false)
    private String VD4;

    @TableField(exist = false)
    private String VD8;

    @TableField(exist = false)
    private String VD12;

    @TableField(exist = false)
    private String VD16;

    @TableField(exist = false)
    private String VD20;

    @TableField(exist = false)
    private String VD24;

    @TableField(exist = false)
    private String VD212;

    @TableField(exist = false)
    private String VD200;

    @TableField(exist = false)
    private String VD220;

    @TableField(exist = false)
    private String M00;
    @TableField(exist = false)
    private String M01;
    @TableField(exist = false)
    private String M02;


    /**
     * 站名
     */
    @TableField(exist = false)
    private String stnm;


    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField(exist = false)
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField(exist = false)
    private Double lttd;

    /**
     * 河系id
     */
    @TableField(exist = false)
    private Integer riverId;

    /**
     * 单位id
     */
    @TableField(exist = false)
    private String unitId;

    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    @TableField(exist = false)
    private String sttp;
}
