package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 防汛物资基础表返回实体
 *
 * @author liwy
 * @since 2023-04-13 14:59:56
 */

@Data
public class StMaterialBaseEsr extends Esr {

    private static final long serialVersionUID = 647611586191346008L;


    /**
     * 主键
     */
    private Integer id;


    /**
     * 物资名称
     */
    private String materialName;


    /**
     * 库存
     */
    private Integer amount;


    /**
     * 单位
     */
    private String danwei;


    /**
     * 存放点位
     */
    private String location;


    /**
     * 单位id
     */
    private String unitId;


    /**
     * 管理单位
     */
    private String unitName;


    /**
     * 备注
     */
    private String remark;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 创建者
     */
    private String creator;


}
