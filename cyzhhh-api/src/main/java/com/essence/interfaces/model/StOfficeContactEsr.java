package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author liwy
 * @since 2023-03-29 18:53:30
 */

@Data
public class StOfficeContactEsr extends Esr {

    private static final long serialVersionUID = 978871183780053294L;


    /**
     * 主键
     */
    private Integer id;


    /**
     * 用户ID
     */
    private String userId;


    /**
     * 科室联系人表主键
     */
    private Integer officeBaseRelationId;


    /**
     * 创建者
     */
    private String creator;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 备注
     */
    private String remark;


    /**
     * 科室基础表ID（st_office_base.id）
     */
    private Integer officeBaseId;


    /**
     * 使用人姓名
     */
    private String userName;


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
     * 科室名
     */
    private String deptName;



}
