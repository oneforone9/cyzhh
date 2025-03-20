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
 * 编码记录表(CodeGenerateUtil)实体类
 *
 * @author zhy
 * @since 2022-10-27 16:54:03
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "code_generate")
public class CodeGenerate extends Model<CodeGenerate> {

    private static final long serialVersionUID = -24935646623432194L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * key
     */
    @TableField("code_key")
    private String codeKey;

    /**
     * 值
     */
    @TableField("code_value")
    private Integer codeValue;

    /**
     * 日期(格式yyyy-MM-dd)
     */

    @TableField("tm")
    private String tm;

}
