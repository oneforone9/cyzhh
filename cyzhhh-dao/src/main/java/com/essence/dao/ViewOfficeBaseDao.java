package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.ViewOfficeBaseDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ViewOfficeBase)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-11 14:21:33
 */
@Mapper
public interface ViewOfficeBaseDao extends BaseDao<ViewOfficeBaseDto> {
}
