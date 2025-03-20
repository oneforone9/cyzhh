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
 * 字典数据表更新实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:25
 */

@Data
public class SysDictionaryDataEsu extends Esu {

    private static final long serialVersionUID = 896956885668104197L;


    /**
     * 字典ID
     * @mock 123213asd
     */
    @NotEmpty(groups = Update.class, message = "字典类型ID不能为空")
    private String id;

    /**
     * 字典名称
     * @mock 事件分类一
     */
    @Size(max = 100, message = "字典名称的最大长度:100")
    @NotEmpty(groups = Update.class, message = "字典类型ID不能为空")
    private String dictionaryName;

    /**
     * 字典值
     * @mock 11
     */
    @Size(max = 100, message = "字典值的最大长度:100")
    @NotEmpty(groups = Update.class, message = "字典类型ID不能为空")
    private String dictionaryValue;

    /**
     * 字典类型
     * @mock EVENT_001
     */
    @Size(max = 100, message = "字典类型的最大长度:100")
    @NotEmpty(groups = Insert.class, message = "字典类型不能为空")
    private String dictionaryType;

    /**
     * 显示顺序
     * @mock 1
     */
    @NotNull(groups = Update.class, message = "字典类型ID不能为空")
    private Long sort;

    /**
     * 父ID
     */
    @Size(max = 32, message = "父ID的最大长度:100")
    private String parentId;


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

    /**
     * 处理单位（1-第三方公司 2-水环境组 3-闸坝设施组）
     */
    private Integer dealUnit;


}
