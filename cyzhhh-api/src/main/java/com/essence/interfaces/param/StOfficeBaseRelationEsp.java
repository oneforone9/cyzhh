package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 科室联系人表参数实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:40
 */

@Data
public class StOfficeBaseRelationEsp extends Esp {

    private static final long serialVersionUID = -67823332070102147L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 科室基础表ID（st_office_base.id）
     */
    @ColumnMean("office_base_id")
    private Integer officeBaseId;
    /**
     * 使用人姓名
     */
    @ColumnMean("user_name")
    private String userName;
    /**
     * 办公电话
     */
    @ColumnMean("office_telephone")
    private String officeTelephone;
    /**
     * 职务
     */
    @ColumnMean("job")
    private String job;
    /**
     * 使用人id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 移动电话
     */
    @ColumnMean("phone_number")
    private String phoneNumber;
    /**
     * 房间号
     */
    @ColumnMean("room_no")
    private String roomNo;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


    /**
     * 是否合同管理员 0:不是； 1:是
     */
    @ColumnMean("is_ht_gly")
    private Integer isHtGly;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;


}
