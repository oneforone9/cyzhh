package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.HtglfjDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同管理附件(Htglfj)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:24
 */
@Mapper
public interface HtglfjDao extends BaseDao<HtglfjDto> {
    @Select("<script>" +
            "INSERT INTO htglfj (id,htid,lx,fileid,scr,tjsj) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.htid},#{item.lx},#{item.fileid}, #{item.scr},#{item.tjsj})" +
            "</foreach> "+
            "</script>")
    void saveFjData(@Param("param")List<HtglfjDto> list);
}
