package com.essence.dao;

import com.essence.common.dto.StatisticsDto;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.CyCaseBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 案件基础表(CyCaseBase)表数据库访问层
 *
 * @author zhy
 * @since 2023-01-04 18:13:41
 */


@Mapper
public interface CyCaseBaseDao extends BaseDao<CyCaseBase> {

    @Select(" \n" +
            " SELECT case_type as type, count(1) as value FROM `cy_case_base` where  filing_date >= #{startStr} AND filing_date <= #{endStr}  GROUP BY case_type")
    List<StatisticsDto> mstatisticsByCasetype(@Param("startStr") String startStr,@Param("endStr") String endStr);

    @Select("SELECT substring(warehousing_date,1,4)  as updateDate ,closing_status as type, count(1) as value FROM `cy_case_base` where case_type = 2 AND filing_date >= #{startStr} AND filing_date <= #{endStr}  GROUP BY closing_status")
    List<StatisticsDto> statisticsByClosingstatus(@Param("startStr") String startStr,@Param("endStr") String endStr);
}
