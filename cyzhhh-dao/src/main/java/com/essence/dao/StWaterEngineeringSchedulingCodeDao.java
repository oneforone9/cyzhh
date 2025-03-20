package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水系联调-工程调度调度指令记录(StWaterEngineeringSchedulingCode)表数据库访问层
 *
 * @author majunjie
 * @since 2023-07-04 14:57:11
 */
@Mapper
public interface StWaterEngineeringSchedulingCodeDao extends BaseDao<StWaterEngineeringSchedulingCodeDto> {
}
