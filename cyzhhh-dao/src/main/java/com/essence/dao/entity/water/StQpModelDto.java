package com.essence.dao.entity.water;

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
* 水系联通-预报水位-河段断面关联表
*
* @authorBINX
* @since 2023年5月11日 下午4:34:54
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_qp_model")
public class StQpModelDto extends Model<StQpModelDto> {

    private static final long serialVersionUID = 375826903949526193L;

    /**
    * 切片ID
    */
    @TableId(value = "qp_id", type = IdType.UUID)
    private Integer qpId;

    /**
    * 所在的断面名称
    */
    @TableField("name")
    private String name;

    /**
    * 所在的断面id
    */
    @TableField("section_id")
    private String sectionId;

    /**
    * 所在的河流id
    */
    @TableField("river_id")
    private Integer riverId;

}
