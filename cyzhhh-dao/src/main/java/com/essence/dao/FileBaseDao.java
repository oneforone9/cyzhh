package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.FileBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 文件管理表(FileBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-28 16:20:13
 */


@Mapper
public interface FileBaseDao extends BaseDao<FileBase> {
    @Update("update t_file_base set pdf_url=#{pdfUrl} where id =#{id}")
    void updateData(@Param("id") String id, @Param("pdfUrl") String localUrlPath);
}
