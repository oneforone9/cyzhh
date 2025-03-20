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
 * 文件管理表(FileBase)实体类
 *
 * @author zhy
 * @since 2022-10-28 16:20:13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_file_base")
public class FileBase extends Model<FileBase> {

    private static final long serialVersionUID = -25902548934329383L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 类型外键
     */
    @TableField("type_id")
    private String typeId;

    /**
     * 分类外键
     */
    @TableField("class_id")
    private String classId;

    /**
     * 业务外键
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件格式
     */
    @TableField("file_format")
    private String fileFormat;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private String fileSize;

    /**
     * 文件地址
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 是否删除(1是 0否)
     */
    @TableField("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * pdf文件url
     */
    @TableField("pdf_url")
    private String pdfUrl;

}
