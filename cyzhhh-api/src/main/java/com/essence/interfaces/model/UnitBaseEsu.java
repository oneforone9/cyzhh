package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 单位信息表更新实体
 *
 * @author zhy
 * @since 2022-10-20 14:16:35
 */

@Data
public class UnitBaseEsu extends Esu {

    private static final long serialVersionUID = 812114838212040159L;


    /**
     * 主键
     * @mock 234324324aefe
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 名称
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "单位名称的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "单位名称不能为空")
    private String unitName;

    /**
     * 新增时间(不传)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间(不传)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 创建者(不传)
     */
    private String creator;

    /**
     * 更新者(不传)
     */
    private String updater;

    /**
     * 备注
     */
    private String remark;


}
