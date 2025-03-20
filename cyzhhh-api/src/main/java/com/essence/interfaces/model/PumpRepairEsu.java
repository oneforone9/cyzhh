package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author cuirx
 * @since 2023-02-20 14:33:10
 */

@Data
public class PumpRepairEsu extends Esu {



    /**
     * 主键
     */
    private String id;

    /**
     * 作业对象
     */
    private String projectTarget;
    /**
     * 闸坝名称
     */
    private String pumpName;
    /**
     * 问题类型
     */
    private String questionType;
    /**
     * 解决方式
     */
    private String dealMethod;
    /**
     * 更新时间
     */
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
