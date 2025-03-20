package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WorkorderProcess;
import com.essence.dao.entity.WorkorderProcessEx;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单处理过程表(WorkorderProcess)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */


@Mapper
public interface WorkorderProcessExDao extends BaseDao<WorkorderProcessEx> {

}
