package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author liwy
 * @since 2023-03-29 18:53:22
 */

@Data
public class StOfficeContactEsp extends Esp {

    private static final long serialVersionUID = -32911140073277038L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 用户ID
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 科室联系人表主键
     */
    @ColumnMean("office_base_relation_id")
    private Integer officeBaseRelationId;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
