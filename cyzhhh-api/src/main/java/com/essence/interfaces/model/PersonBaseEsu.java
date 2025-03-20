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
 * 人员基础信息表更新实体
 *
 * @author zhy
 * @since 2022-10-20 15:07:20
 */

@Data
public class PersonBaseEsu extends Esu {

    private static final long serialVersionUID = 179556558888464089L;


    /**
     * 主键
     * @mock 1231231321
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 姓名
     * @mock 张三
     */
    @Size(max = 10, message = "姓名的最大长度:10")
    @NotEmpty(groups = Insert.class, message = "姓名不能为空")
    private String name;

    /**
     * 联系方式
     * @mock 12387623111
     */
    @Size(max = 11, message = "联系方式的最大长度:11")
    @NotEmpty(groups = Insert.class, message = "联系方式不能为空")
    private String telephone;

    /**
     * 单位主键
     * @mock 1
     */
    @Size(max = 32, message = "单位主键的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "单位主键不能为空")
    private String unitId;

    /**
     * 所属单位
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "所属单位的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "所属单位不能为空")
    private String unitName;

    /**
     * 状态(0 启用 1停用）
     * @mock 0
     */
    @Size(max = 1, message = "状态的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "状态不能为空")
    private String status;

    /**
     * 类型(1 内部人员 2第三方人员)
     * @mock 1
     */
    @Size(max = 1, message = "类型的最大长度:1")
    @NotEmpty(message = "类型不能为空")
    private String type;

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
    @Size(max = 1000, message = "备注的最大长度:1000")
    private String remark;


}
