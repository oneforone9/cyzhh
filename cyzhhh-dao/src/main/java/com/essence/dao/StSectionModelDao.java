package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSectionModelDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模型断面基础表(StSectionModel)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-19 18:15:42
 */
@Mapper
public interface StSectionModelDao extends BaseDao<StSectionModelDto> {
}
