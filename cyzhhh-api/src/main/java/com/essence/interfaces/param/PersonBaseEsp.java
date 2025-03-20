package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员基础信息表参数实体
 *
 * @author zhy
 * @since 2022-10-20 15:07:21
 */

@Data
public class PersonBaseEsp extends Esp {

    private static final long serialVersionUID = -35859976685262746L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 姓名
     */
    @ColumnMean("name")
    private String name;

    /**
     * 联系方式
     */
    @ColumnMean("telephone")
    private String telephone;

    /**
     * 单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 所属单位
     */
    @ColumnMean("unit_name")
    private String unitName;

    /**
     * 状态(0 启用 1停用）
     */
    @ColumnMean("status")
    private String status;

    /**
     * 类型(1 内部人员 2第三方人员)
     */
    @ColumnMean("type")
    private String type;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 更新者
     */
    @ColumnMean("updater")
    private String updater;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
