package com.essence.interfaces.model;

import com.essence.common.constant.ItemConstant;
import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 工单处理过程表更新实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */

@Data
public class WorkorderProcessEsu extends Esu {

    private static final long serialVersionUID = -63480790304478403L;


    /**
     * 主键
     *
     * @ignore
     */
    private String id;

    /**
     * 工单主键
     *
     * @mock 11123232
     */
    @NotEmpty(groups = Insert.class, message = "工单主键不能为空")
    private String orderId;
    /**
     * 事件主键（由事件生成的工单必填）
     *
     * @mock 11123232
     */
    private String eventId;

    /**
     * 当前处理人主键
     *
     * @mock 525b437e2f2644dbaedb24c9e5028386
     */
    @Size(max = 32, message = "当前处理人主键的最大长度:32")
    //@NotEmpty(groups = Insert.class, message = "当前处理人主键不能为空")
    private String personId;

    /**
     * 当前处理人
     *
     * @mock 张三
     */
    @Size(max = 20, message = "当前处理人的最大长度:20")
    //@NotEmpty(groups = Insert.class, message = "当前处理人主键不能为空")
    private String personName;

    /**
     * 工单当前状态（1未派发 2未开始 3进行中 4待审核 5已归档 6已终止 7催办）
     *
     * @mock 3
     */
    @Size(max = 2, message = "工单当前状态的最大长度:2")
    @NotEmpty(groups = Insert.class, message = "工单当前状态不能为空")
    private String orderStatus;

    /**
     * 工单处理时间
     *
     * @ignore
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    /**
     * 操作(记录工作人员处理过程)
     */
    private String operation;

    /**
     * 关联的文件id
     *
     * @mock [1111, 222, 333]
     */
    private List<String> fileIds;

    @AssertTrue(message = "工单当前状态请从以下选择[未派发,未开始,进行中,待审核,已归档,已终止,催办]")
    private boolean isOrderType() {
        if (null == this.orderStatus) {
            return false;
        }
        if (ItemConstant.ORDER_STATUS_NO_HANDOUT.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_NO_START.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_RUNNING.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_EXAMINNING.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_END.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_OVER.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_STOP_FINAL.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_URGE.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_YHWC.equals(this.orderStatus) ||
                ItemConstant.ORDER_STATUS_REFUSE.equals(this.orderStatus)
        ) {
            return true;
        }
        return false;
    }

    /**
     * 备注
     */
    private String remark;

    /**
     * 公司负责人审批意见
     */
    private String opinion;

    /**
     * 整改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fixTime;
}
