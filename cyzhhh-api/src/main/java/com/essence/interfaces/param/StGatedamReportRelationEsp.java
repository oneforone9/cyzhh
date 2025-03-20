package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝运行维保日志上报-关联表参数实体
 *
 * @author liwy
 * @since 2023-03-15 11:57:20
 */

@Data
public class StGatedamReportRelationEsp extends Esp {

    private static final long serialVersionUID = 532438810754227692L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 闸坝运行维保日志上报表st_gatedam_report的id
     */
    @ColumnMean("st_gatedam_id")
    private String stGatedamId;
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
