package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanOperateRejectDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 养护内容-驳回记录表(StPlanOperateReject)表数据库访问层
 *
 * @author BINX
 * @since 2023-09-11 17:52:36
 */
@Mapper
public interface StPlanOperateRejectDao extends BaseDao<StPlanOperateRejectDto> {
}
