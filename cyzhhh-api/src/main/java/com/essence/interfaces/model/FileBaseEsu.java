package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文件管理表更新实体
 *
 * @author zhy
 * @since 2022-10-28 16:20:13
 */

@Data
public class FileBaseEsu extends Esu {

    private static final long serialVersionUID = 776901977044130603L;


    /**
     * 主键
     */
    private String id;

    /**
     * 类型外键
     */
    private String typeId;

    /**
     * 分类外键
     */
    private String classId;

    /**
     * 业务外键
     */
    private String businessId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 是否删除(1是 0否)
     */
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 备注
     */
    private String remark;
    /**
     * pdf文件url
     */
    private String pdfUrl;

}
