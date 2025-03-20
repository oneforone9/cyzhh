package com.essence.interfaces.model;


import com.essence.interfaces.vaild.Insert;
import lombok.Data;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 预警报表返回实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:30
 */

@Data
public class StForecastReception  {

    /**
     * 预警主键
     */

    @NotEmpty(groups = Insert.class,message = "预警id不能为空")
    private String forecastId;

    /**
     * 操作类别1发送2接收
     */
    @NotNull(groups = Update.class, message = "操作类别不能为空")
    private Integer state;

}
