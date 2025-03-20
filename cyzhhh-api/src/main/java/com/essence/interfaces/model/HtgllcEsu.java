package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理历程更新实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */

@Data
public class HtgllcEsu extends Esu {

    private static final long serialVersionUID = -31738582011376052L;

    /**
     * 主键
     */        private String id;    

    /**
     * 合同主键
     */        private String htid;    

    /**
     * 描述
     */        private String ms;

    /**
     * 经办人名称
     */        private String jbrmc;    

    /**
     * 经办人科室
     */        private String jbrks;    

    /**
     * 待办理日期
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dblrq;    

    /**
     * 办理完成日期
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date blwcrq;    

    /**
     * 办理状态0未办1已办
     */        private Integer blzt;    

    /**
     *
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成,
     */
    private Integer zt;
    /**
     * 经办人
     */
    private String jbr;
}
