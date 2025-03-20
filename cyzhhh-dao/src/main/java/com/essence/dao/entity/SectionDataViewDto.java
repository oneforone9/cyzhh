package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;



@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "section_data_view")
public class SectionDataViewDto extends Model<SectionDataViewDto> {

    private static final long serialVersionUID = 747093370698510368L;

                            @TableField("id")
    private Integer id;
    
            /**
     * 断面名称
     */
                    @TableField("section_name")
    private String sectionName;
    
            /**
     * 断面类型(0 国考 1 市考 2区考)
     */
                    @TableField("section_type")
    private String sectionType;
    
            /**
     * 河系id
     */
                    @TableField("river_id")
    private Integer riverId;
    
            /**
     * 经度
     */
                    @TableField("lgtd")
    private Double lgtd;
    
            /**
     * 纬度
     */
                    @TableField("lttd")
    private Double lttd;
    
            /**
     * 断面类型
     */
                    @TableField("type")
    private String type;
    
                        @TableField("quality_id")
    private String qualityId;
    
            /**
     * 断面主键
     */
                    @TableField("section_id")
    private Integer sectionId;
    
            /**
     * 水质期间(年月)
     */
                    @TableField("quality_period")
    private String qualityPeriod;
    
            /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */
                    @TableField("quality_level")
    private Integer qualityLevel;
    
            /**
     * 创建者
     */
                    @TableField("creator")
    private String creator;
    
            /**
     * 更新者
     */
                    @TableField("updater")
    private String updater;
    
            /**
     * 备注(水质等级，无水还是结冰)
     */
                    @TableField("level_remark")
    private String levelRemark;
    
            /**
     * 平均水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;)
     */
                    @TableField("average_level")
    private Integer averageLevel;
    
            /**
     * 河道名称
     */
                    @TableField("rea_name")
    private String reaName;
    

}
