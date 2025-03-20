package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.TReaFocusPointDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * (TReaFocusPoint)表数据库访问层
 *
 * @author liwy
 * @since 2023-05-06 10:02:19
 */
@Mapper
public interface TReaFocusPointDao extends BaseDao<TReaFocusPointDto> {
}
