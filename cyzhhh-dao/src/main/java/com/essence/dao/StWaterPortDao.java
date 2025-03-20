package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterPortDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 补水口基础表(StWaterPort)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-22 17:12:43
 */
@Mapper
public interface StWaterPortDao extends BaseDao<StWaterPortDto> {
}
