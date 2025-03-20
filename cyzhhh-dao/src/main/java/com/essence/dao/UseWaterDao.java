package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.UseWaterDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用水量(UseWater)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-04 17:18:02
 */
@Mapper
public interface UseWaterDao extends BaseDao<UseWaterDto> {
}
