package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.alg.StCaseResParamDto;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 方案执行结果表入参表 (st_case_res_param)表数据库访问层
*
* @author BINX
* @since 2023年4月28日 下午5:45:25
*/
@Mapper
public interface StCaseResParamDao extends BaseDao<StCaseResParamDto> {

    @Insert("REPLACE INTO st_case_res_param(id,stnm,holes_number,design_flow,section_name,section_name_down," +
            "seria_name,case_id,liquid_level,stop_level,create_time,control_type,control_value) " +
            "VALUES (#{stCaseResParamDto.id},#{stCaseResParamDto.stnm}," +
            "#{stCaseResParamDto.holesNumber},#{stCaseResParamDto.designFlow},#{stCaseResParamDto.sectionName}," +
            "#{stCaseResParamDto.sectionNameDown},#{stCaseResParamDto.seriaName},#{stCaseResParamDto.caseId}," +
            "#{stCaseResParamDto.liquidLevel},#{stCaseResParamDto.stopLevel},#{stCaseResParamDto.createTime},#{stCaseResParamDto.controlType},#{stCaseResParamDto.controlValue})")
    public void saveOrUpdate(@Param("stCaseResParamDto") StCaseResParamDto stCaseResParamDto);
}
