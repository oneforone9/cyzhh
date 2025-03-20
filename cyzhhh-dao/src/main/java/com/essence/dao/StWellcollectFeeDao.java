package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWellcollectFeeDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StWellcollectFee)表数据库访问层
 *
 * @author bird
 * @since 2023-01-04 18:01:06
 */
@Mapper
public interface StWellcollectFeeDao extends BaseDao<StWellcollectFeeDto> {
}
