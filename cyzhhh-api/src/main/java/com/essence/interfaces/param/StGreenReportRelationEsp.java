package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 绿化保洁工作日志上报表-关联表参数实体
 *
 * @author liwy
 * @since 2023-03-17 17:20:17
 */

@Data
public class StGreenReportRelationEsp extends Esp {

    private static final long serialVersionUID = -71294701642239149L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 绿化保洁工作日志上报表st_green_report的id
     */
    @ColumnMean("st_green_id")
    private String stGreenId;
    /**
     * 作业内容标记
     */
    @ColumnMean("work_flag")
    private String workFlag;
    /**
     * 作业内容描述
     */
    @ColumnMean("work_content")
    private String workContent;
    /**
     * 解决措施
     */
    @ColumnMean("handle_way")
    private String handleWay;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
