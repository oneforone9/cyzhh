package com.essence.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/14 17:49
 */
@Data
public class WaterSituationVo {

    /**
     * 站点名称
     */
    private String stnm;

    /**
     * 所属河道
     */
    private String riverName;

    /**
     * 最高水位 (m)
     */
    private String highestLevel;

    /**
     * 最高水位时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date highestLevelTime;

    /**
     * 距警戒（m）
     */
    private String fromWaring;

    /**
     * 距漫堤（m）
     */
    private String fromMandi;

}
