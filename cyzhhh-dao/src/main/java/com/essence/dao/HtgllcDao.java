package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.HtgllcDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * 合同管理历程(Htgllc)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */
@Mapper
public interface HtgllcDao extends BaseDao<HtgllcDto> {
    @Update("UPDATE htgllc SET blwcrq = #{blwcrq},blzt =#{blzt} WHERE htid = #{htid} and zt =#{zt}")
    void updateDataWc(@Param("htid") String htid, @Param("blzt") Integer blzt, @Param("zt") Integer zt, @Param("blwcrq") Date blwcrq);

    @Update("UPDATE htgllc SET dblrq = #{blwcrq},blwcrq =null,blzt =#{blzt} WHERE htid = #{htid} and zt =#{zt}")
    void updateDataWb(@Param("htid") String htid, @Param("blzt") Integer blzt, @Param("zt") Integer zt, @Param("blwcrq") Date blwcrq);

    @Select("<script>" +
            "INSERT INTO htgllc (id,htid,ms,jbrmc,jbrks,dblrq,blwcrq,blzt,zt,jbr) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.htid},#{item.ms},#{item.jbrmc},#{item.jbrks},#{item.dblrq},#{item.blwcrq},#{item.blzt},#{item.zt},#{item.jbr})" +
            "</foreach> " +
            "</script>")
    void saveData(@Param("param") List<HtgllcDto> list);

    @Update("UPDATE htgllc SET dblrq = #{blwcrq},blzt =#{blzt},jbrmc =#{jbrmc},jbrks =#{jbrks},jbr =#{jbr} WHERE htid = #{htid} and zt =#{zt}")
    void updateDataWbName(@Param("htid") String htid, @Param("blzt") Integer blzt, @Param("zt") Integer zt, @Param("blwcrq") Date blwcrq, @Param("jbrmc") String jbrmc, @Param("jbrks") String jbrks, @Param("jbr") String jbr);

    @Update("UPDATE htgllc SET blwcrq = #{blwcrq},blzt =#{blzt} WHERE htid = #{htid} and zt =#{zt} and jbr =#{jbr}")
    void updateDataWcKs(@Param("htid") String htid, @Param("blzt") Integer blzt, @Param("zt") Integer zt, @Param("blwcrq") Date blwcrq, @Param("jbr") String jbr);
}
