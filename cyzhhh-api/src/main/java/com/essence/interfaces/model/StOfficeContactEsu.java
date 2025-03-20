package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-03-29 18:53:12
 */

@Data
public class StOfficeContactEsu extends Esu {

    private static final long serialVersionUID = -95857892360164912L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    @NotEmpty(groups = Update.class, message = "用户ID不能为空")
    private String userId;

    /**
     * 科室联系人表主键
     */
    @NotEmpty(groups = Insert.class, message = "科室联系人表主键不能为空")
    private Integer officeBaseRelationId;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 备注
     */
    private String remark;


}
