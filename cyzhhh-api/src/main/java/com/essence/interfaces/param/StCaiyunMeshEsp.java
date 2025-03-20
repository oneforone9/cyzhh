package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
* 彩云预报网格编号 参数实体
*
* @authorBINX
* @since 2023年5月5日 上午11:59:16
*/
@Data
public class StCaiyunMeshEsp extends Esp {

    private static final long serialVersionUID = 204805172712572347L;
    
    /**
    * 预报网格id
    */
    @ColumnMean("mesh_id")
    private String meshId;
    
    /**
    * 经度（WGS 84坐标系）
    */
    @ColumnMean("lgtd")
    private String lgtd;
    
    /**
    * 纬度（WGS 84坐标系）
    */
    @ColumnMean("lttd")
    private String lttd;
    
    /**
    * 经度（GCJ 02坐标系）
    */
    @ColumnMean("lgtd_gcj")
    private String lgtdGcj;
    
    /**
    * 纬度（GCJ 02坐标系）
    */
    @ColumnMean("lttd_gcj")
    private String lttdGcj;
    
    /**
    * 关联站点编码
    */
    @ColumnMean("stcd")
    private String stcd;
    
    /**
    * 关联站点名称
    */
    @ColumnMean("stnm")
    private String stnm;
    
    /**
    * 关联id类型
    */
    @ColumnMean("type")
    private String type;

}
