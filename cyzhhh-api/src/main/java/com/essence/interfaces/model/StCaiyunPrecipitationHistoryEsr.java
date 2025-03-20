package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 彩云预报历史数据 返回实体
*
* @authorBINX
* @since 2023年5月4日 下午3:47:15
*/
@Data
public class StCaiyunPrecipitationHistoryEsr extends Esr {

    private static final long serialVersionUID = 332526044213626908L;

    /**
    * id
    */
    private String id;

    /**
    * 网格编号
    */
    private String meshId;

    /**
    * 雨量值（mm）
    */
    private String drp;

    /**
    * 雨量时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drpTime;

    /**
    * 写入时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
