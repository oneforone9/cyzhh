package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjHysxxDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检会议室信息(XjHysxx)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */
@Mapper
public interface XjHysxxDao extends BaseDao<XjHysxxDto> {
}
