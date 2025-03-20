package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理附件返回实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:26
 */

@Data
public class HtglfjEsr extends Esr {

    private static final long serialVersionUID = 862625340204994795L;


    /**
     * 主键
     */
    private String id;


    /**
     * 合同id
     */
    private String htid;


    /**
     * 来源 0：合同电子版； 1: 合同扫描件 ； 2: 会签单扫描件； 3: 预审单扫描件； 4:授权扫描件 5:合同最终版 6:授权委托书最终版
     */
    private String lx;


    /**
     * 文件id集合
     */
    private String fileid;


    /**
     * 上传人
     */
    private String scr;


    /**
     * 提交时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tjsj;


}
