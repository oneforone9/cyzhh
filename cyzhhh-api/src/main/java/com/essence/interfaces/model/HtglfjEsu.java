package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理附件更新实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:25
 */

@Data
public class HtglfjEsu extends Esu {

    private static final long serialVersionUID = 351962900506225420L;

    /**
     * 主键
     */
    private String id;

    /**
     * 合同id
     */
    private String htid;

    /**
     * 来源 0：合同电子版； 1: 合同扫描件 ； 2: 会签单扫描件； 3: 预审单扫描件； 4:合法性审查单扫描件
     */
    private Integer lx;

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
