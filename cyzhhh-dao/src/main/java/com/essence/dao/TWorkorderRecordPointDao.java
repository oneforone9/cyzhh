package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.TWorkorderRecordPointDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息(TWorkorderRecordPoint)表数据库访问层
 *
 * @author liwy
 * @since 2023-05-07 12:11:34
 */
@Mapper
public interface TWorkorderRecordPointDao extends BaseDao<TWorkorderRecordPointDto> {
}
