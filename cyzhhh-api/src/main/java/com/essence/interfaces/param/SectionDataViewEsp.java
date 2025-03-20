package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-01-05 14:59:49
 */

@Data
public class SectionDataViewEsp extends Esp {

    private static final long serialVersionUID = 304413454424030103L;

                    @ColumnMean("id")
    private Integer id;
        /**
     * 断面名称
     */            @ColumnMean("section_name")
    private String sectionName;
        /**
     * 断面类型(0 国考 1 市考 2区考)
     */            @ColumnMean("section_type")
    private String sectionType;
        /**
     * 河系id
     */            @ColumnMean("river_id")
    private Integer riverId;
        /**
     * 经度
     */            @ColumnMean("lgtd")
    private Double lgtd;
        /**
     * 纬度
     */            @ColumnMean("lttd")
    private Double lttd;
        /**
     * 断面类型
     */            @ColumnMean("type")
    private String type;
                @ColumnMean("quality_id")
    private String qualityId;
        /**
     * 断面主键
     */            @ColumnMean("section_id")
    private Integer sectionId;
        /**
     * 水质期间(年月)
     */            @ColumnMean("quality_period")
    private String qualityPeriod;
        /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */            @ColumnMean("quality_level")
    private Integer qualityLevel;
        /**
     * 创建者
     */            @ColumnMean("creator")
    private String creator;
        /**
     * 更新者
     */            @ColumnMean("updater")
    private String updater;
        /**
     * 备注(水质等级，无水还是结冰)
     */            @ColumnMean("level_remark")
    private String levelRemark;
        /**
     * 平均水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;)
     */            @ColumnMean("average_level")
    private Integer averageLevel;
        /**
     * 河道名称
     */            @ColumnMean("rea_name")
    private String reaName;


}
