package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.XjRyxxDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 设备巡检人员信息(XjRyxx)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */
@Mapper
public interface XjRyxxDao extends BaseDao<XjRyxxDto> {
    @Update("update xj_ryxx SET lx = #{lx} where bz_id =#{bzId} ;")
    void updateLx(@Param("lx") Integer lx,@Param("bzId") String bzId);
}
