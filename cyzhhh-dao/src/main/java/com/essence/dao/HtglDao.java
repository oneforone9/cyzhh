package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.HtglDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同管理(Htgl)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-09 17:45:34
 */
@Mapper
public interface HtglDao extends BaseDao<HtglDto> {
    @Select("<script>" +
            "INSERT INTO htgl (id,htbh,sqlx,htmc,hytlqk,shrq,shnr,cgfs,zjly,htnr,htjf,htyf,qtf,htqdrq,htje,sfk,zqk,wkqk,qszxrq,zzzxrq,qdks,jbr,jbrmc,jbrbm,htsmjsczt,htsxzt,tdsj,ly,htsxztxgyy,qt,zt,flgwsc,tdrqrscd,cbjgfzryj,qfldlx,htfs,lsscbh,ysqk,zdsxjttlqk,fzyj,cwyj,zgldyj,zyldyj,htshlc,cwsid,bgsid,zgldid,kzid,kzids,kzidss,cwshqqr,bgshqqr,keshqr,keshqrs,keshqrss,hsz,th,sfys,lsid,tdslys,tdsjhq,gq,bz) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.htbh},#{item.sqlx},#{item.htmc}, #{item.hytlqk}," +
            "#{item.shrq},#{item.shnr},#{item.cgfs},#{item.zjly},#{item.htnr}," +
            "#{item.htjf},#{item.htyf},#{item.qtf},#{item.htqdrq},#{item.htje}," +
            "#{item.sfk},#{item.zqk},#{item.wkqk},#{item.qszxrq},#{item.zzzxrq}," +
            " #{item.qdks},#{item.jbr},#{item.jbrmc},#{item.jbrbm}, #{item.htsmjsczt}," +
            " #{item.htsxzt}, #{item.tdsj},#{item.ly}, #{item.htsxztxgyy}," +
            "#{item.qt}, #{item.zt},#{item.flgwsc},#{item.tdrqrscd},#{item.cbjgfzryj}," +
            "#{item.qfldlx},#{item.htfs},#{item.lsscbh},#{item.ysqk},#{item.zdsxjttlqk}," +
            "#{item.fzyj},#{item.cwyj},#{item.zgldyj},#{item.zyldyj},#{item.htshlc}," +
            "#{item.cwsid},#{item.bgsid},#{item.zgldid},#{item.kzid},#{item.kzids},#{item.kzidss}," +
            "#{item.cwshqqr},#{item.bgshqqr},#{item.keshqr},#{item.keshqrs},#{item.keshqrss},#{item.hsz},#{item.th},#{item.sfys},#{item.lsid},#{item.tdslys},#{item.tdsjhq},#{item.gq},#{item.bz})" +
            "</foreach> " +
            "</script>")
    void saveData(@Param("param") List<HtglDto> htglDtoList);

}
