package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 知识库文件表更新实体
 *
 * @author liwy
 * @since 2023-08-17 10:30:12
 */

@Data
public class StLibraryFileEsu extends Esu {

    private static final long serialVersionUID = 964725825361663742L;

    private Integer id;

    /**
     * 文库类型
     */
    private Integer libraryId;

    /**
     * 文库标题
     */
    private String fileTitle;

    /**
     * 文档地址
     */
    private String fileUrl;

    /**
     * 文档类型
     */
    private String fileType;

    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    /**
     * 上传人id
     */
    private String userId;

    /**
     * 上传人id
     */
    private String userName;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 备注
     */
    private String remark;


}
