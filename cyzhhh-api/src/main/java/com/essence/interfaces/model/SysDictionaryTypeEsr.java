package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典表返回实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:29
 */

@Data
public class SysDictionaryTypeEsr extends Esr {

    private static final long serialVersionUID = -26274181046324572L;


    /**
     * 字典类型ID
     */
    private String id;

    /**
     * 字典类型
     */
    private String dictionaryType;

    /**
     * 字典类型名称
     */
    private String dictionaryTypeName;

    /**
     * 显示顺序
     */
    private Long sort;




    /**
     * 备注
     */
    private String remark;




}
