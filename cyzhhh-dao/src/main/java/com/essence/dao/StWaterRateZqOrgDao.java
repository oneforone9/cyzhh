package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.henrunan.StWaterRateZqOrgDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流量站从恒润安软件同步原始数据(StWaterRateZqOrg)表数据库访问层
 *
 * @author BINX
 * @since 2023-08-02 15:39:23
 */
@Mapper
public interface StWaterRateZqOrgDao extends BaseDao<StWaterRateZqOrgDto> {
}
