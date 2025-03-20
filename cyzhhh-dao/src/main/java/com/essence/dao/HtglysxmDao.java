package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.HtglysxmDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同管理预审项目(Htglysxm)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
@Mapper
public interface HtglysxmDao extends BaseDao<HtglysxmDto> {
    @Select("<script>" +
            "INSERT INTO htglysxm (id,htid,scxm,jtnr,tjsj) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.htid},#{item.scxm},#{item.jtnr},#{item.tjsj})" +
            "</foreach> "+
            "</script>")
    void saveYsxnData(@Param("param")List<HtglysxmDto> list);
}
