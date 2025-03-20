package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.EventBaseStatusEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件基础信息表(EventBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-30 18:06:22
 */


@Mapper
public interface EventBaseStatusDao extends BaseDao<EventBaseStatusEntity> {

}
