package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容-驳回记录表更新实体
 *
 * @author zhy
 * @since 2023-09-11 17:52:37
 */

@Data
public class StPlanOperateRejectEsu extends Esu {

    private static final long serialVersionUID = -86666748344669819L;

    private String id;

    /**
     * 工单主键(生成事件的工单主键)
     */
    private String orderId;

    /**
     * 整改说明
     */
    private String rejectExplain;

    /**
     * 整改内容
     */
    private String rejectContent;

    /**
     * 整改图片
     */
    private String rejectPicture;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
