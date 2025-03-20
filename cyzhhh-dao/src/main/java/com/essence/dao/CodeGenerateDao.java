package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.CodeGenerate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 编码记录表(CodeGenerateUtil)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-27 16:54:03
 */


@Mapper
public interface CodeGenerateDao extends BaseDao<CodeGenerate> {

}
