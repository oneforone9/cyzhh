package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-03-29 18:53:12
 */

@Data
public class StOfficeContactEsuParam extends Esu {

    private static final long serialVersionUID = -95857892360164912L;

    /**
     * 参数
     */
    private PaginatorParam paginatorParam;

    /**
     * 科室名
     */
    private String deptName;

    /**
     * 使用人姓名
     */
    private String userName;


    /**
     * 办公电话
     */
    private String officeTelephone;

    /**
     * 移动电话
     */
    private String phoneNumber;


    /**
     * 职务
     */
    private String job;


    /**
     * 房间号
     */
    private String roomNo;



}
