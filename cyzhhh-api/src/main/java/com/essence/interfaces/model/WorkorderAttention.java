package com.essence.interfaces.model;

import com.essence.common.constant.ItemConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import java.util.Date;


/**
 * 工单基础信息表更新是否关注
 *
 * @author zhy
 * @since 2022-10-27 15:26:22
 */

@Data
public class WorkorderAttention extends WorkorderNewestEsu {

    private static final long serialVersionUID = 135820168685960855L;


    /**
     * 主键
     * @mock 1
     */
//    @NotEmpty(message = "主键不能为空")
    private String id;


    /**
     * 是否关注（1是 0否）
     * @mock 1
     */
//    @NotEmpty(message = "是否关注不能为空")
//    @Size(max = 1, message = "是否关注的最大长度:1")
    private String isAttention;



    @AssertTrue(message = "是否关注请从以下选择: [是,否]")
    private boolean  isAttention(){
        if (null == this.isAttention){
            return true;
        }
        if (ItemConstant.ORDER_NO_ATTENTION.equals(this.isAttention) || ItemConstant.ORDER_IS_ATTENTION.equals(this.isAttention)){
            return true;
        }
        return false;
    }

    /**
     * 开始巡河时间
     */
    private String startWorkTime;


    /**
     *工单完成巡查时间
     */
    private String endWorkTime;

    /**
     * 派发方式(1人工派发 2自动派发)
     * @mock 1
     */
    private String distributeType;

    /**
     * 工单负责人
     * @mock 李四
     */
    private String orderManager;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rejectTime;
    /**
     * 是否 更新 驳回完成时间 1 是 2 否
     */
    private String isRejectTime;

    /**
     * 驳回工单开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date rejectStartTime;
}
