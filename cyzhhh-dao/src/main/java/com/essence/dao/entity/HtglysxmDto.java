package com.essence.dao.entity;

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
 * 合同管理预审项目实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htglysxm")
public class HtglysxmDto extends Model<HtglysxmDto> {

    private static final long serialVersionUID = 343774326754414427L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同主键
     */
    @TableField("htid")
    private String htid;

    /**
     * 审查项目
     */
    @TableField("scxm")
    private String scxm;

    /**
     * 具体内容
     */
    @TableField("jtnr")
    private String jtnr;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("tjsj")
    private Date tjsj;

}
