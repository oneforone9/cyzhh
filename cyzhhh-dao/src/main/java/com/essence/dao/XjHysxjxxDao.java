package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjHysxjxxDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检会议室关联巡检信息(XjHysxjxx)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 14:00:52
 */
@Mapper
public interface XjHysxjxxDao extends BaseDao<XjHysxjxxDto> {
}
