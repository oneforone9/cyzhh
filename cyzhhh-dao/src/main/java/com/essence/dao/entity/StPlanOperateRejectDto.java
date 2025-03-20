package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容-驳回记录表实体
 *
 * @author BINX
 * @since 2023-09-11 17:52:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_operate_reject")
public class StPlanOperateRejectDto extends Model<StPlanOperateRejectDto> {

    private static final long serialVersionUID = -17594489306552417L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 工单主键(生成事件的工单主键)
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 整改说明
     */
    @TableField("reject_explain")
    private String rejectExplain;

    /**
     * 整改内容
     */
    @TableField("reject_content")
    private String rejectContent;

    /**
     * 整改图片
     */
    @TableField("reject_picture")
    private String rejectPicture;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

}
