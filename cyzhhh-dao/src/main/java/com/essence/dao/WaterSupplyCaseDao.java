package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WaterSupplyCaseDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 补水口案件计算入参表(WaterSupplyCase)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-04 19:15:23
 */
@Mapper
public interface WaterSupplyCaseDao extends BaseDao<WaterSupplyCaseDto> {

    @Insert("<script> " +
            "INSERT INTO water_supply_case " +
            "(case_id, river_name, water_port_name, supply, lgtd, lttd, section_name, seria_name, supply_way, time_supply) " +
            "VALUES" +
            "<foreach item='item' index='index' collection='list' separator=','>" +
            "(#{item.caseId}, #{item.riverName}, #{item.waterPortName}, #{item.supply}, #{item.lgtd}, #{item.lttd}, #{item.sectionName}, #{item.seriaName}, #{item.supplyWay}, #{item.timeSupply})" +
            "</foreach>" +
            "</script>")
    int insertAll(@Param("list") List<WaterSupplyCaseDto> list);

    @Update("<script> " +
            "REPLACE INTO water_supply_case " +
            "(id, case_id, river_name, water_port_name, supply, lgtd, lttd, section_name, seria_name, supply_way, time_supply) " +
            "VALUES" +
            "<foreach item='item' index='index' collection='list' separator=','>" +
            "(#{item.id}, #{item.caseId}, #{item.riverName}, #{item.waterPortName}, #{item.supply}, #{item.lgtd}, #{item.lttd}, #{item.sectionName}, #{item.seriaName}, #{item.supplyWay}, #{item.timeSupply})" +
            "</foreach>" +
            "</script>")
    int updateAll(@Param("list") List<WaterSupplyCaseDto> list);
}
