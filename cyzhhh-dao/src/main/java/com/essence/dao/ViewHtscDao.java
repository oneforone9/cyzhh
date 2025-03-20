package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.ViewHtscDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ViewHtsc)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-13 16:41:09
 */
@Mapper
public interface ViewHtscDao extends BaseDao<ViewHtscDto> {
}
