package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StHolidayCountryDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StHolidayCountry)表数据库访问层
 *
 * @author BINX
 * @since 2024-02-26 11:30:18
 */
@Mapper
public interface StHolidayCountryDao extends BaseDao<StHolidayCountryDto> {
}
