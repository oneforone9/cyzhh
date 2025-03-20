package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StLibraryFileDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 知识库文件表(StLibraryFile)表数据库访问层
 *
 * @author liwy
 * @since 2023-08-17 10:27:23
 */
@Mapper
public interface StLibraryFileDao extends BaseDao<StLibraryFileDto> {
}
