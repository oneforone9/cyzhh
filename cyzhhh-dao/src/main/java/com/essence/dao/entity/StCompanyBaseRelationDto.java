package com.essence.dao.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author BINX
 * @since 2023-02-16 12:01:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_company_base_relation")
public class StCompanyBaseRelationDto extends Model<StCompanyBaseRelationDto> {

    private static final long serialVersionUID = -77331389427924290L;
        
    @TableId(value = "id", type = IdType.INPUT)
    @ExcelIgnore
    private Integer id;
        
    /**
     * 第三方服务公司主键id
     */
    @TableField("st_company_base_id")
    @ExcelIgnore
    private String stCompanyBaseId;
    
    /**
     * 数据类型（1-服务类型  2-管护河道）(新改 数据类型（1-河道绿化保洁  2-闸坝运行养护）)
     */
    @TableField("type")
    @ExcelIgnore
    private String type;
    
    /**
     * 服务类型或管护河段id
     */
    @TableField("data_id")
    @ExcelIgnore
    private String dataId;
    
    /**
     * 备注
     */
    @TableField("remark")
    @ExcelIgnore
    private String remark;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 创建者
     */
    @TableField("creator")
    @ExcelIgnore
    private String creator;

    /**
     * 服务类型或管护河段名称
     */
    @TableField("data_name")
    @ExcelProperty(value = "工单类型",index = 2)
    private String dataName;
    /**
     * 公司名称
     */
    @TableField(exist = false)
    @ExcelProperty(value = "公司名称" , index = 1)
    private String company;

    /**
     * 绿化工单数
     */
    @TableField(exist = false)
    @ExcelIgnore
    private Integer count3;

    /**
     * 保洁工单数
     */
    @TableField(exist = false)
    @ExcelIgnore
    private Integer count2;

    /**
     * 总数
     */
    @TableField(exist = false)
    @ExcelProperty(value = "工单数量(个)",index = 3)
    private Integer total;
    /**
     * 所有的工单总数
     */
    @TableField(exist = false)
    @ExcelIgnore
    private Integer allCount;

    /**
     * 已处理数
     */
    @TableField(exist = false)
    @ExcelProperty(value = "工单处理量(个)",index = 4)
    private Integer dealCount;

    /**
     * 站比
     */
    @TableField(exist = false)
    @ExcelIgnore
    private String percent;

    /**
     * 统计年月
     */
    @TableField(exist = false)
    @ExcelIgnore
    private String preDayDate;

    @TableField(exist = false)
    @ExcelProperty(value = "时间",index = 0)
    private String start;

    @TableField(exist = false)
    @ExcelIgnore
    private String end;

    /**
     * 管护河段
     */
    @TableField(exist = false)
    @ExcelIgnore
    private List manageRiver;

}
