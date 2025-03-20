package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理附件参数实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:25
 */

@Data
public class HtglfjEsp extends Esp {

    private static final long serialVersionUID = 621724333088174049L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 合同id
     */
    @ColumnMean("htid")
    private String htid;
    /**
     * 来源 0：合同电子版； 1: 合同扫描件 ； 2: 会签单扫描件； 3: 预审单扫描件； 4:合法性审查单扫描件
     */
    @ColumnMean("lx")
    private Integer lx;
    /**
     * 文件id集合
     */
    @ColumnMean("fileid")
    private String fileid;
    /**
     * 上传人
     */
    @ColumnMean("scr")
    private String scr;
    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tjsj")
    private Date tjsj;


}
