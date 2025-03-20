package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjrwDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检任务(Xjrw)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 15:09:05
 */
@Mapper
public interface XjrwDao extends BaseDao<XjrwDto> {
    @Insert("<script>" +
            "INSERT INTO xjrw (id, gdmc, bh, ly, mc, type, lx, river_name, address, lgtd, lttd, xjnr, fzr, fzr_id, lxfs, jhsj, sjsj, jssj, zt, wcqk, fxwt, yj, cjsj, dkjd, dkwd, dkdz, dkjltp, wtms, dktp, cltp, clcs, fxsj, pfsj, jjsj, bzmc, pfry, bz_id, sbr,parent_id,zzly,jjsx) VALUES " +
            "<foreach item='item' index='index' collection='params'  separator=','  >" +
            "( #{item.id}, #{item.gdmc}, #{item.bh}, #{item.ly}, #{item.mc}, #{item.type}, #{item.lx}, #{item.riverName}, #{item.address}, #{item.lgtd}, #{item.lttd}, #{item.xjnr}, #{item.fzr}, #{item.fzrId}, #{item.lxfs}" +
            ", #{item.jhsj}, #{item.sjsj}, #{item.jssj}, #{item.zt}, #{item.wcqk}, #{item.fxwt}, #{item.yj}, #{item.cjsj}, #{item.dkjd}, #{item.dkwd}, #{item.dkdz}, #{item.dkjltp}, #{item.wtms}, #{item.dktp}, #{item.cltp}, #{item.clcs}, #{item.fxsj}, #{item.pfsj}, #{item.jjsj}, #{item.bzmc}, #{item.pfry}, #{item.bzId}, #{item.sbr}, #{item.parentId}, #{item.zzly}, #{item.jjsx})" +
            "</foreach> "+
            "</script>")
    void saveData(@Param("params") List<XjrwDto> list);
}
