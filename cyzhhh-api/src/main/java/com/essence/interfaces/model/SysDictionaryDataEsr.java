package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典数据表返回实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:26
 */

@Data
public class SysDictionaryDataEsr extends Esr {

    private static final long serialVersionUID = 216100011731894662L;


    /**
     * 字典ID
     */
    private String id;

    /**
     * 字典名称
     */
    private String dictionaryName;

    /**
     * 字典值
     */
    private String dictionaryValue;

    /**
     * 字典类型
     */
    private String dictionaryType;

    /**
     * 显示顺序
     */
    private Long sort;

    /**
     * 父ID
     */
    private String parentId;



    /**
     * 备注
     */
    private String remark;


    /**
     * 处理单位（1-第三方公司 2-水环境组 3-闸坝设施组）
     */
    private Integer dealUnit;




}
