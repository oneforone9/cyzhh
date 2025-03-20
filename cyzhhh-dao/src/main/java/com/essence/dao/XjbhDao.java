package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjbhDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备巡检任务编码(Xjbh)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-10 13:35:33
 */
@Mapper
public interface XjbhDao extends BaseDao<XjbhDto> {
}
