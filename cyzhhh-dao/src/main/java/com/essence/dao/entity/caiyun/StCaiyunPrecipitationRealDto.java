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
* 彩云预报实时数据
*
* @authorBINX
* @since 2023年5月4日 下午3:47:15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_caiyun_precipitation_real")
public class StCaiyunPrecipitationRealDto extends Model<StCaiyunPrecipitationRealDto> {

    private static final long serialVersionUID = 452785220583272713L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
    * 网格编号
    */
    @TableField("mesh_id")
    private String meshId;

    /**
    * 雨量值（mm）
    */
    @TableField("drp")
    private String drp;

    /**
    * 雨量时间
    */
    @TableField("drp_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drpTime;

    /**
    * 写入时间
    */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private transient String stcd;

    private transient String  stnm;
}
