package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjrwlcDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检流程(Xjrwlc)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */
@Mapper
public interface XjrwlcDao extends BaseDao<XjrwlcDto> {
    @Insert("<script>" +
            "INSERT INTO xjrwlc (id, rw_id, ms, czr, cjsj) VALUES " +
            "<foreach item='item' index='index' collection='params'  separator=','  >" +
            "( #{item.id}, #{item.rwId}, #{item.ms}, #{item.czr}, #{item.cjsj})" +
            "</foreach> "+
            "</script>")
    void saveData(@Param("params") List<XjrwlcDto> list);
}
