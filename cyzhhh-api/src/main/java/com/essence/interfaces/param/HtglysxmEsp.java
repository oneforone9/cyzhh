package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理预审项目参数实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */

@Data
public class HtglysxmEsp extends Esp {

    private static final long serialVersionUID = 440866995986884431L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 合同主键
     */        @ColumnMean("htid")
    private String htid;
    /**
     * 审查项目
     */        @ColumnMean("scxm")
    private String scxm;
    /**
     * 具体内容
     */        @ColumnMean("jtnr")
    private String jtnr;
    /**
     * 提交时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tjsj")
    private Date tjsj;


}
