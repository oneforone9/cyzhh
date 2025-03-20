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
 * 字典表更新实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:28
 */

@Data
public class SysDictionaryTypeEsu extends Esu {

    private static final long serialVersionUID = -64991738297808391L;


    /**
     * 字典类型ID
     * @mock 123qwe
     */
    @NotEmpty(groups = Update.class, message = "字典类型ID不能为空")
    private String id;

    /**
     * 字典类型
     * @mock EVETN_001
     */

    @Size(max = 100, message = "字典类型的最大长度:100")
    @NotEmpty(groups = Insert.class, message = "字典类型不能为空")
    private String dictionaryType;

    /**
     * 字典类型名称
     * @mock 事件类型
     */

    @Size(max = 255, message = "人字典类型名称的最大长度:255")
    @NotEmpty(groups = Insert.class, message = "字典类型名称不能为空")
    private String dictionaryTypeName;

    /**
     * 显示顺序
     * @mock 1
     */
    @NotNull(groups = Insert.class, message = "显示顺序不能为空")
    private Long sort;


    /**
     * 创建时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 创建者
     * @ignore
     */
    private String creator;

    /**
     * 更新者
     * @ignore
     */
    private String updater;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注的最大长度:1000")
    private String remark;

    /**
     * 是否逻辑删除（0正常/未删除  1删除）
     * @ignore
     */
    private String isDeleted;


}
