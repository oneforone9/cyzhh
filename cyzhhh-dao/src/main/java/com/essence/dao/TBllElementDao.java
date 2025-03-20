package com.essence.dao;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.henrunan.TBllElementDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 恒润安：定时从第三方数据库取数 定时数据表(TBllElement)表数据库访问层
 *
 * @author BINX
 * @since 2023-08-02 14:03:48
 */
@Mapper
@DS("henrunan")
public interface TBllElementDao extends BaseDao<TBllElementDto> {
}
