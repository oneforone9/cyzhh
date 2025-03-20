package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 闸坝计划排班信息表(StPlanInfo)表数据库访问层
 *
 * @author liwy
 * @since 2023-07-13 14:40:31
 */
@Mapper
public interface StPlanInfoDao extends BaseDao<StPlanInfoDto> {
    /**
     * 查询维护计划列表
     * @param sttp
     * @param stnm
     * @param riverName
     * @return
     */
    @Select("<script> SELECT a.side_gate_id,a.stnm,a.river_name,a.company,a.name,a.phone  FROM st_plan_info a " +
            " where a.sttp = #{sttp} " +
            " <when test='stnm !=null'> and a.stnm like concat('%',#{stnm},'%') </when> " +
            " <when test='name !=null'> and a.name like concat('%',#{name},'%') </when> " +
            " <when test='riverName !=null'> and a.river_name like concat('%',#{riverName},'%') </when> " +
            " GROUP BY a.sttp,a.stnm,a.company,a.name </script>")
    List<StPlanInfoDto> getStPlanInfo(@Param("sttp") String sttp, @Param("stnm") String stnm, @Param("riverName") String riverName, @Param("name") String name);

    /**
     * 同步修改闸坝养护计划中三方公司的负责人信息
     * @param companyId
     * @param company
     * @param unitId
     * @param userId
     * @param name
     * @param phone
     * @param userId1
     * @return
     */
    @Update("update st_plan_info set  company_id = #{companyId}, company = #{company},  name = #{name} ,user_id = #{userId},  phone = #{phone} where user_id = #{userIdBefore} " +
            "and unit_id = #{unitId}")
    Integer updateStPlanInfoByConditionb(@Param("companyId") String companyId, @Param("company") String company, @Param("unitId") String unitId, @Param("userId") String userId,
                                         @Param("name") String name, @Param("phone") String phone, @Param("userIdBefore") String userId1);
}
