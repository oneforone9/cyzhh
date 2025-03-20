package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 科室联系人表返回实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:43
 */

@Data
public class StOfficeBaseRelationEsr extends Esr {

    private static final long serialVersionUID = -39924583410232680L;


    /**
     * 主键
     */
    private Integer id;


    /**
     * 科室基础表ID（st_office_base.id）
     */
    private Integer officeBaseId;


    /**
     * 使用人姓名
     */
    private String userName;
    /**
     * 使用人id
     */
    private String userId;

    /**
     * 办公电话
     */
    private String officeTelephone;


    /**
     * 职务
     */
    private String job;


    /**
     * 移动电话
     */
    private String phoneNumber;


    /**
     * 房间号
     */
    private String roomNo;


    /**
     * 备注
     */
    private String remark;


    /**
     * 是否合同管理员 0:不是； 1:是
     */
    private Integer isHtGly;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 创建者
     */
    private String creator;


}
