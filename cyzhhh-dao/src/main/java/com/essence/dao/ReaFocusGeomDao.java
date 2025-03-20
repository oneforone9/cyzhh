package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.ReaFocusGeom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 河道重点位置地理信息表(ReaFocusGeom)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-26 14:06:34
 */


@Mapper
public interface ReaFocusGeomDao extends BaseDao<ReaFocusGeom> {
    /**
     * 根据河道主键查询（不含空间数据）
     * @param reaId
     * @return
     */
    @Select("select id, focus_position from t_rea_focus_geom where rea_id =#{reaId}")
    List<ReaFocusGeom> mfindByReaId(@Param("reaId") String reaId);

}
