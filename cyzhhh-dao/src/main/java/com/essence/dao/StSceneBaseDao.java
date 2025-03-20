package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSceneBaseDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场景基本表(StSceneBase)表数据库访问层
 *
 * @author liwy
 * @since 2023-06-01 14:47:53
 */
@Mapper
public interface StSceneBaseDao extends BaseDao<StSceneBaseDto> {
}
