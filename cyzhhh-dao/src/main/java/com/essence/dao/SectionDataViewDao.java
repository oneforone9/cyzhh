package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.SectionDataViewDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (SectionDataView)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-05 14:59:48
 */
@Mapper
public interface SectionDataViewDao extends BaseDao<SectionDataViewDto> {
}
