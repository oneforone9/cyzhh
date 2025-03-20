package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StDesigRainPatternDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设计雨型(StDesigRainPattern)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-24 09:57:28
 */
@Mapper
public interface StDesigRainPatternDao extends BaseDao<StDesigRainPatternDto> {
}
