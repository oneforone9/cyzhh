package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
* 彩云预报历史数据 参数实体
*
* @authorBINX
* @since 2023年5月4日 下午3:47:15
*/
@Data
public class StCaiyunPrecipitationHistoryEsp extends Esp {

    private static final long serialVersionUID = 332526044213626908L;
    
    /**
    * id
    */
    @ColumnMean("id")
    private String id;
    
    /**
    * 网格编号
    */
    @ColumnMean("mesh_id")
    private String meshId;
    
    /**
    * 雨量值（mm）
    */
    @ColumnMean("drp")
    private String drp;
    
    /**
    * 雨量时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("drp_time")
    private Date drpTime;
    
    /**
    * 写入时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("create_time")
    private Date createTime;

}
