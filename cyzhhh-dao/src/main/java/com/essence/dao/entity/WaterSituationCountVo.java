package com.essence.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/14 17:49
 */
@Data
public class WaterSituationCountVo {

    /**
     * 漫堤
     */
    Integer overFlowCount;

    /**
     * 超警戒
     */
    Integer overWaringCount;

    /**
     * 水位站列表
     */
    List<WaterSituationVo> list;

}
