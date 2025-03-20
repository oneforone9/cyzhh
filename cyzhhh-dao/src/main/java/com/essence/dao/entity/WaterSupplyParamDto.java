package com.essence.dao.entity;

import lombok.Data;

import java.util.List;

/**
 * 补水口案件计算保存入参
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/5 14:38
 */
@Data
public class WaterSupplyParamDto {

    /**
     * 案件ID
     */
    String caseId;

    /**
     * 补水口实体列表
     */
    List<WaterSupplyCaseDto> list;
}
