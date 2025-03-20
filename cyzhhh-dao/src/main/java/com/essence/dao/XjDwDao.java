package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjDwDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检单位(XjDw)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 08:56:29
 */
@Mapper
public interface XjDwDao extends BaseDao<XjDwDto> {
}
