package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WorkorderNewest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单最新状态服务
 *
 * @author zhy
 * @since 2022/10/28 14:41
 */
@Mapper
public interface WorkorderNewestDao extends BaseDao<WorkorderNewest> {
}
