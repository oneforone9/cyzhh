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
 * @since 2025-01-09 10:22:28
 */

@Data
public class ViewHysXjEsp extends Esp {

    private static final long serialVersionUID = 324897039951311380L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 会议室名称
     */
    @ColumnMean("mc")
    private String mc;
    /**
     * 生成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;
    /**
     * 主键
     */
    @ColumnMean("jhid")
    private String jhid;
    /**
     * 巡检内容
     */
    @ColumnMean("xjnr")
    private String xjnr;
    /**
     * 负责人
     */
    @ColumnMean("fzr")
    private String fzr;
    /**
     * 负责人id
     */
    @ColumnMean("fzr_id")
    private String fzrId;
    /**
     * 巡检日期巡检日期(1,2,3,4,5,6,7)
     */
    @ColumnMean("xj_rq")
    private String xjRq;
    /**
     * 巡检单位id
     */
    @ColumnMean("xjdw_id")
    private String xjdwId;
    /**
     * 班组id
     */
    @ColumnMean("bz_id")
    private String bzId;
    /**
     * 单位名称
     */
    @ColumnMean("dwmc")
    private String dwmc;

    /**
     * 班组名称
     */
    @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private Double lttd;
}
