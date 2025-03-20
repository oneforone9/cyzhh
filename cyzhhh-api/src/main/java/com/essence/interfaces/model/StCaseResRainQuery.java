package com.essence.interfaces.model;

import com.essence.interfaces.vaild.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 实测降雨
 *
 * @author majunjie
 * @since 2023-03-10 16:57:04
 */

@Data
public class StCaseResRainQuery {

    /**
     * 开始时间(yyyy-MM-dd HH:mm:ss)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class,message = "开始时间不能为空")
    private Date startTime;

    /**
     * 结束时间(yyyy-MM-dd HH:mm:ss)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class,message = "结束时间不能为空")
    private Date endTime;

    /**
     * 步长(分钟)
     */
    @NotNull(groups = Insert.class,message = "步长(分钟)不能为空")
    private Integer step;
}
