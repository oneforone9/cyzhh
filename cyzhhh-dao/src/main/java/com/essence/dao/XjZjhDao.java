package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjZjhDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备周计划(XjZjh)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 15:52:40
 */
@Mapper
public interface XjZjhDao extends BaseDao<XjZjhDto> {
    @Insert("<script>" +
            "INSERT INTO xj_zjh (id, ms, start_time, end_time, year,time) VALUES " +
            "<foreach item='item' index='index' collection='params'  separator=','  >" +
            "( #{item.id}, #{item.ms}, #{item.startTime}, #{item.endTime}, #{item.year}, #{item.time})" +
            "</foreach> "+
            "</script>")
    void saveData(@Param("params") List<XjZjhDto> xjZjhDtos);
}
