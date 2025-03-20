package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanPersonDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * (三方养护人员信息表)表数据库访问层
 *
 * @author liwy
 * @since 2023-07-17 14:52:53
 */
@Mapper
public interface StPlanPersonDao extends BaseDao<StPlanPersonDto> {


    /**
     * 同步修改养护人员表中该三方公司和三方负责人信息
     * @param companyId
     * @param company
     * @param unitId
     * @param userId
     * @param name
     * @param phone
     * @param userIdBefore
     * @return
     */
    @Update("update st_plan_person set company_id = #{companyId}, company= #{company},  name =#{name} ,user_id = #{userId}, phone = #{phone} " +
            " WHERE user_id = #{userIdBefore} and unit_id = #{unitId}")
    Integer updateStPlanPersonByCondition(@Param("companyId") String companyId, @Param("company") String company,
                                          @Param("unitId") String unitId, @Param("userId") String userId,
                                          @Param("name") String name, @Param("phone") String phone,
                                          @Param("userIdBefore") String userIdBefore);
}
