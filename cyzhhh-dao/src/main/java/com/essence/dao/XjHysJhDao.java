package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjHysJhDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检会议室巡检计划(XjHysJh)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */
@Mapper
public interface XjHysJhDao extends BaseDao<XjHysJhDto> {
}
