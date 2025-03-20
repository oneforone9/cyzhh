package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjBzxxDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检班组信息(XjBzxx)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 08:13:25
 */
@Mapper
public interface XjBzxxDao extends BaseDao<XjBzxxDto> {
}
