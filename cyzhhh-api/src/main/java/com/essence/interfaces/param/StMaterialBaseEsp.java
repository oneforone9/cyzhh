package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 防汛物资基础表参数实体
 *
 * @author liwy
 * @since 2023-04-13 14:59:53
 */

@Data
public class StMaterialBaseEsp extends Esp {

    private static final long serialVersionUID = 927608577393976921L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 物资名称
     */
    @ColumnMean("material_name")
    private String materialName;
    /**
     * 库存
     */
    @ColumnMean("amount")
    private Integer amount;
    /**
     * 单位
     */
    @ColumnMean("danwei")
    private String danwei;
    /**
     * 存放点位
     */
    @ColumnMean("location")
    private String location;
    /**
     * 单位id
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 管理单位
     */
    @ColumnMean("unit_name")
    private String unitName;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;


}
