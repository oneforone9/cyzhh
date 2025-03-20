package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.VGateDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (VGateData)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-20 17:35:43
 */
@Mapper
public interface VGateDataDao extends BaseDao<VGateDataDto> {

    /**
     * 闸坝运行工况
     * @param stnm
     * @param ctime
     * @return
     */
    @Select(" SELECT tba2.*,st.stnm,st.lgtd,st.lttd,st.river_id,st.management_unit,st.unit_id,st.sttp from " +
            " (select tba.*,sg.stcd from " +
            " (select a.ctime,a.did, " +
            " (CASE  WHEN addr = 'VD4' THEN addrv ELSE null END ) VD4,b.VD8,c.VD12,d.VD16,e.VD20,f.VD24,g.VD212,h.VD200,i.M00,j.M01,k.M02 " +
            "  from  st_water_gate a, " +
            " (select did,ctime,(CASE  WHEN addr = 'VD8' THEN addrv ELSE null END ) as VD8 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD8' " +
            " )b, " +
            " (select did,ctime,(CASE  WHEN addr = 'VD12' THEN addrv ELSE null END ) VD12 " +
            "        from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD12' " +
            " ) c, " +
            " (select did,ctime,(CASE  WHEN addr = 'VD16' THEN addrv ELSE null END ) VD16 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD16' " +
            " ) d, " +
            " (select did,ctime,(CASE  WHEN addr = 'VD20' THEN addrv ELSE null END ) VD20" +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD20' " +
            " ) e, " +
            " ( select did,ctime,(CASE  WHEN addr = 'VD24' THEN addrv ELSE null END ) VD24 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD24' " +
            " ) f, " +
            " ( select did,ctime,(CASE  WHEN addr = 'VD212' THEN addrv ELSE null END ) VD212 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD212' " +
            " ) g, " +
            " (select did,ctime,(CASE  WHEN addr = 'VD200' THEN addrv ELSE null END ) VD200" +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'VD200' " +
            " ) h," +
            " ( select did,ctime,(CASE  WHEN addr = 'M0.0' THEN addrv ELSE null END ) M00 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'M0.0' " +
            " ) i, " +
            " ( select did,ctime,(CASE  WHEN addr = 'M0.1' THEN addrv ELSE null END ) M01 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'M0.1' " +
            " ) j, " +
            " ( select did,ctime,(CASE  WHEN addr = 'M0.2' THEN addrv ELSE null END ) M02 " +
            " from  st_water_gate WHERE DATE_FORMAT(ctime,'%Y-%m-%d') = #{ctime} and  addr = 'M0.2' " +
            " ) k " +
            " WHERE DATE_FORMAT(a.ctime,'%Y-%m-%d') = #{ctime} and  a.addr = 'VD4' " +
            "   and  a.ctime = b.ctime and a.did = b.did " +
            "   and  a.ctime = c.ctime and a.did = c.did " +
            "   and  a.ctime = d.ctime and a.did = d.did " +
            "   and  a.ctime = e.ctime and a.did = e.did " +
            "   and  a.ctime = f.ctime and a.did = f.did " +
            "   and  a.ctime = g.ctime and a.did = g.did " +
            "   and  a.ctime = h.ctime and a.did = h.did " +
            "   and  a.ctime = i.ctime and a.did = i.did " +
            "   and  a.ctime = j.ctime and a.did = j.did " +
            "   and  a.ctime = k.ctime and a.did = k.did " +
            " )tba " +
            " left join st_ga_convert sg on sg.sn = tba.did " +
            " )tba2 " +
            " left join st_side_gate st on st.stcd = tba2.stcd")
    List<VGateDataDto> selectvGateDataList2(@Param("stnm")String stnm, @Param("ctime")String ctime);

    /**
     * 根据 stcd 获取到对应的sn
     * @param stcd
     * @return
     */
    @Select("SELECT sn FROM st_ga_convert where stcd = #{stcd} ")
    String selectSn(@Param("stcd") String stcd);

    /**
     * 获取最新数据时间
     * @param sn
     * @return
     */
    @Select("select MAX(ctime) from st_water_gate where did =  #{sn} ")
    String selectCtime(@Param("sn")String sn);
}
