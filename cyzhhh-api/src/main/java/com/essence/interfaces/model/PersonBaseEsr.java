package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员基础信息表返回实体
 *
 * @author zhy
 * @since 2022-10-20 15:07:21
 */

@Data
public class PersonBaseEsr extends Esr {

    private static final long serialVersionUID = 264780627274707358L;


    /**
     * 主键
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String telephone;

    /**
     * 单位主键
     */
    private String unitId;

    /**
     * 所属单位
     */
    private String unitName;

    /**
     * 状态(0 启用 1停用）
     */
    private String status;

    /**
     * 类型(1 内部人员 2第三方人员)
     */
    private String type;



    /**
     * 备注
     */
    private String remark;


}
