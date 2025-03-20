package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSideGateRelationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * 闸坝负责人关联表(StSideGateRelation)表数据库访问层
 *
 * @author liwy
 * @since 2023-04-13 17:50:14
 */
@Mapper
public interface StSideGateRelationDao extends BaseDao<StSideGateRelationDto> {

    /**
     * 编辑闸坝负责人信息
     * @param id
     * @param jsfzr
     * @param jsfzrPhone
     * @param xcfzr
     * @param xcfzrPhone
     * @param xcfzrUserId
     * @param xzfzr
     * @param xzfzrPhone
     * @return
     */
    @Update("UPDATE st_side_gate_relation SET jsfzr=#{jsfzr},jsfzr_phone=#{jsfzrPhone}, " +
            " xcfzr=#{xcfzr},xcfzr_phone=#{xcfzrPhone},xcfzr_user_id=#{xcfzrUserId}, " +
            " xzfzr=#{xzfzr},xzfzr_phone=#{xzfzrPhone} WHERE id=#{id}")
    int updateStSideGateRelation(@Param("id")Integer id,
                                 @Param("jsfzr")String jsfzr, @Param("jsfzrPhone")String jsfzrPhone,
                                 @Param("xcfzr")String xcfzr, @Param("xcfzrPhone")String xcfzrPhone, @Param("xcfzrUserId")String xcfzrUserId,
                                 @Param("xzfzr")String xzfzr, @Param("xzfzrPhone")String xzfzrPhone);
}
