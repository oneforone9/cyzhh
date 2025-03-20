package com.essence.interfaces.dot;

import lombok.Data;

import java.util.List;

/**
 * 超水位闸坝返回实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/10 17:28
 */
@Data
public class WaterOverLevelStatisticsDto {

    /**
     * 时期 (非汛期（9月16日～5月31日）、非主汛期（6月1日～7月19日、8月16日～9月15日）、主汛期(7月20日～8月15日))
     */
    private String period;

    /**
     * 水位取值(高水位/中水位)
     */
    private String levelName;

    /**
     * 高于正常水位数量
     */
    private Integer overLevelCount;

    /**
     * 低于正常水位数量
     */
    private Integer underLevelCount;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 超水位闸坝列表
     */
    private List<WaterOverLevelDto> list;
}
