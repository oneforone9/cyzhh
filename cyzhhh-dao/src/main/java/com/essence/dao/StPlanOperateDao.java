package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanOperateDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 养护内容记录表(StPlanOperate)表数据库访问层
 *
 * @author liwy
 * @since 2023-07-24 14:16:44
 */
@Mapper
public interface StPlanOperateDao extends BaseDao<StPlanOperateDto> {
}
