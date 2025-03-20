package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjSxtJhTimeDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检摄像头巡检计划日期(XjSxtJhTime)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 17:51:55
 */
@Mapper
public interface XjSxtJhTimeDao extends BaseDao<XjSxtJhTimeDto> {
    @Insert("<script>" +
            "INSERT INTO xj_sxt_jh_time (id, zjh_id, jh_id) VALUES " +
            "<foreach item='item' index='index' collection='params'  separator=','  >" +
            "( #{item.id}, #{item.zjhId}, #{item.jhId})" +
            "</foreach> "+
            "</script>")
    void saveData(@Param("params") List<XjSxtJhTimeDto> list);
}
