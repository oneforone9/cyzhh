package com.essence.dao.entity.caiyun;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 彩云预报网格编号
*
* @authorBINX
* @since 2023年5月5日 上午11:59:16
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_caiyun_mesh")
public class StCaiyunMeshDto extends Model<StCaiyunMeshDto> {

    private static final long serialVersionUID = 204805172712572347L;

    /**
    * 预报网格id
    */
    @TableId(value = "mesh_id", type = IdType.UUID)
    private String meshId;

    /**
    * 经度（WGS 84坐标系）
    */
    @TableField("lgtd")
    private String lgtd;

    /**
    * 纬度（WGS 84坐标系）
    */
    @TableField("lttd")
    private String lttd;

    /**
    * 经度（GCJ 02坐标系）
    */
    @TableField("lgtd_gcj")
    private String lgtdGcj;

    /**
    * 纬度（GCJ 02坐标系）
    */
    @TableField("lttd_gcj")
    private String lttdGcj;

    /**
    * 关联站点编码
    */
    @TableField("stcd")
    private String stcd;

    /**
    * 关联站点名称
    */
    @TableField("stnm")
    private String stnm;

    /**
    * 关联id类型
    */
    @TableField("type")
    private String type;

}
