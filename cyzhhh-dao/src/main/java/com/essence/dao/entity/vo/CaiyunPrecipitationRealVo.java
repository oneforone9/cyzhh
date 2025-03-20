package com.essence.dao.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回彩云天气对应的雨量和测站相关信息
 *
 * @author huangyong
 * Create at 2024/7/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaiyunPrecipitationRealVo {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 网格编号
     */
    private String meshId;

    /**
     * 雨量值（mm）
     */
    private Double drp;

    /**
     * 雨量时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drpTime;
}
