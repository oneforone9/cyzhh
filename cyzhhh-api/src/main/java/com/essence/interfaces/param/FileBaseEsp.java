package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文件管理表参数实体
 *
 * @author zhy
 * @since 2022-10-28 16:20:13
 */

@Data
public class FileBaseEsp extends Esp {

    private static final long serialVersionUID = -98443173004852226L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 类型外键
     */
    @ColumnMean("type_id")
    private String typeId;

    /**
     * 分类外键
     */
    @ColumnMean("class_id")
    private String classId;

    /**
     * 业务外键
     */
    @ColumnMean("business_id")
    private String businessId;

    /**
     * 文件名
     */
    @ColumnMean("file_name")
    private String fileName;

    /**
     * 文件格式
     */
    @ColumnMean("file_format")
    private String fileFormat;

    /**
     * 文件大小
     */
    @ColumnMean("file_size")
    private String fileSize;

    /**
     * 文件地址
     */
    @ColumnMean("file_url")
    private String fileUrl;

    /**
     * 是否删除(1是 0否)
     */
    @ColumnMean("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * pdf文件url
     */
    @ColumnMean("pdf_url")
    private String pdfUrl;

}
