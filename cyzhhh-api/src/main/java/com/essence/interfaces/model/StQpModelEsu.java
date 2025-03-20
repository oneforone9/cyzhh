package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 水系联通-预报水位-河段断面关联表 更新实体
*
* @authorBINX
* @since 2023年5月11日 下午4:34:54
*/
@Data
public class StQpModelEsu extends Esu {

    private static final long serialVersionUID = 375826903949526193L;

    /**
    * 切片ID
    */
    private Integer qpId;

    /**
    * 所在的断面名称
    */
    private String name;

    /**
    * 所在的断面id
    */
    private String sectionId;

    /**
    * 所在的河流id
    */
    private Integer riverId;

}
