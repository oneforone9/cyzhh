package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 文件管理表返回实体
 *
 * @author zhy
 * @since 2022-10-28 16:20:13
 */

@Data
public class FileBaseEsr extends Esr {

    private static final long serialVersionUID = -22749843139321522L;


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
     * 备注
     */
    private String remark;
    /**
     * pdf文件url
     */
    private String pdfUrl;

}
