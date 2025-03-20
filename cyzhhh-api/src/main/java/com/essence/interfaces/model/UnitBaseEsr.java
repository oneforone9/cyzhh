package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 单位信息表返回实体
 *
 * @author zhy
 * @since 2022-10-20 14:16:36
 */

@Data
public class UnitBaseEsr extends Esr {

    private static final long serialVersionUID = -53826123986930114L;


    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String unitName;


}
