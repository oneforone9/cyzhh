package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 知识库文件表参数实体
 *
 * @author liwy
 * @since 2023-08-17 10:30:59
 */

@Data
public class StLibraryFileEsp extends Esp {

    private static final long serialVersionUID = -75024301411029446L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 文库类型
     */
    @ColumnMean("library_id")
    private Integer libraryId;
    /**
     * 文库标题
     */
    @ColumnMean("file_title")
    private String fileTitle;
    /**
     * 文档地址
     */
    @ColumnMean("file_url")
    private String fileUrl;
    /**
     * 文档类型
     */
    @ColumnMean("file_type")
    private String fileType;
    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("upload_time")
    private Date uploadTime;
    /**
     * 上传人id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 上传人id
     */
    @ColumnMean("user_name")
    private String userName;
    /**
     * 所属单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 所属单位名称
     */
    @ColumnMean("unit_name")
    private String unitName;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
