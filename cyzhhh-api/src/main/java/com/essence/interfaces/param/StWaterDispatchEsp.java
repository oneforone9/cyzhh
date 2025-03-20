package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:27
 */

@Data
public class StWaterDispatchEsp extends Esp {

    private static final long serialVersionUID = -37661421742772055L;

    @ColumnMean("id")
    private String id;
    /**
     * 0正常运行工况1特殊运行工况
     */
    @ColumnMean("type")
    private Integer type;
    /**
     * 时间区间0（8月16-次年7月19）1（7月20-8月15）
     */
    @ColumnMean("data_type")
    private String dataType;
    /**
     * 河道名称
     */
    @ColumnMean("river_name")
    private String riverName;
    /**
     * 河道id
     */
    @ColumnMean("river_id")
    private String riverId;
    /**
     * 方案id
     */
    @ColumnMean("case_id")
    private String caseId;
    /**
     * 文件路径”,“号拆分
     */
    @ColumnMean("file")
    private String file;
    /**
     * 示意图
     */
    @ColumnMean("files")
    private String files;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_update")
    private Date gmtUpdate;


}
