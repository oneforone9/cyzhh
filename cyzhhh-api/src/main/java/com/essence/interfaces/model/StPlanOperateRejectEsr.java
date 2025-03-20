package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容-驳回记录表返回实体
 *
 * @author zhy
 * @since 2023-09-11 17:52:38
 */

@Data
public class StPlanOperateRejectEsr extends Esr {

    private static final long serialVersionUID = -26673988264248418L;

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
