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
 * 知识库文件表实体
 *
 * @author liwy
 * @since 2023-08-17 10:29:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_library_file")
public class StLibraryFileDto extends Model<StLibraryFileDto> {

    private static final long serialVersionUID = 479885247262229065L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 文库类型
     */
    @TableField("library_id")
    private Integer libraryId;
    
    /**
     * 文库标题
     */
    @TableField("file_title")
    private String fileTitle;
    
    /**
     * 文档地址
     */
    @TableField("file_url")
    private String fileUrl;
    
    /**
     * 文档类型
     */
    @TableField("file_type")
    private String fileType;
    
    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("upload_time")
    private Date uploadTime;
    
    /**
     * 上传人id
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 上传人id
     */
    @TableField("user_name")
    private String userName;
    
    /**
     * 所属单位主键
     */
    @TableField("unit_id")
    private String unitId;
    
    /**
     * 所属单位名称
     */
    @TableField("unit_name")
    private String unitName;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
