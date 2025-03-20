package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.OrderRosteringRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 值班表生成工单记录表(避免重复生成工单)(OrderRosteringRecord)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-31 17:53:17
 */


@Mapper
public interface OrderRosteringRecordDao extends BaseDao<OrderRosteringRecord> {

}
