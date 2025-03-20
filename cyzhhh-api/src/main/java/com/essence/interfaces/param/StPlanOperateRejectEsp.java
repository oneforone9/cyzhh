package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容-驳回记录表参数实体
 *
 * @author zhy
 * @since 2023-09-11 17:52:38
 */

@Data
public class StPlanOperateRejectEsp extends Esp {

    private static final long serialVersionUID = -82347510174374648L;

    @ColumnMean("id")
    private String id;
    /**
     * 工单主键(生成事件的工单主键)
     */
    @ColumnMean("order_id")
    private String orderId;
    /**
     * 整改说明
     */
    @ColumnMean("reject_explain")
    private String rejectExplain;
    /**
     * 整改内容
     */
    @ColumnMean("reject_content")
    private String rejectContent;
    /**
     * 整改图片
     */
    @ColumnMean("reject_picture")
    private String rejectPicture;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
