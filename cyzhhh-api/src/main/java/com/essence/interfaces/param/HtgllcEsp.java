package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理历程参数实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */

@Data
public class HtgllcEsp extends Esp {

    private static final long serialVersionUID = -96279435732542053L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 合同主键
     */
    @ColumnMean("htid")
    private String htid;
    /**
     * 描述
     */
    @ColumnMean("ms")
    private String ms;
    /**
     * 经办人名称
     */
    @ColumnMean("jbrmc")
    private String jbrmc;
    /**
     * 经办人科室
     */
    @ColumnMean("jbrks")
    private String jbrks;
    /**
     * 待办理日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("dblrq")
    private Date dblrq;
    /**
     * 办理完成日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("blwcrq")
    private Date blwcrq;
    /**
     * 办理状态0未办1已办
     */
    @ColumnMean("blzt")
    private Integer blzt;
    /**
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成,
     */
    @ColumnMean("zt")
    private Integer zt;
    /**
     * 经办人
     */
    @ColumnMean("jbr")
    private String jbr;
}
