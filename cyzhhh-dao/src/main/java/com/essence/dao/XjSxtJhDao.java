package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjSxtJhDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检摄像头巡检计划(XjSxtJh)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 22:46:37
 */
@Mapper
public interface XjSxtJhDao extends BaseDao<XjSxtJhDto> {
    @Insert("<script>" +
            "INSERT INTO xj_sxt_jh (id,sxt_id,xjnr,fzr,fzr_id,xj_rq,xjdw_id,bz_id,bzmc) VALUES " +
            "<foreach item='item' index='index' collection='params'  separator=','  >" +
            "( #{item.id}, #{item.sxtId}, #{item.xjnr}, #{item.fzr}, #{item.fzrId}, #{item.xjRq}, #{item.xjdwId}, #{item.bzId}, #{item.bzmc})" +
            "</foreach> "+
            "</script>")
    void saveData(@Param("params") List<XjSxtJhDto> list);
}
