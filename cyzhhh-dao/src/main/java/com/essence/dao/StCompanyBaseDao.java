package com.essence.dao;


import cn.hutool.core.date.DateTime;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StCompanyBaseDto;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 第三方服务公司基础表(StCompanyBase)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-16 11:57:42
 */
@Mapper
public interface StCompanyBaseDao extends BaseDao<StCompanyBaseDto> {
    /**
     * 河湖管理-河道绿化保洁（绿化、保洁工单统计）
     * @param unitId
     * @return
     */
    @Select("<script>SELECT b.*,a.company from st_company_base_relation b,st_company_base a " +
            " where b.type = 1 and b.st_company_base_id = a.id and b.data_id = 1 " +
            " <when test='unitId !=null'> and a.unit_id = #{unitId} </when> " +
            " <when test='company !=null'> and a.company like concat('%',#{company},'%') </when> </script>")
    List<StCompanyBaseRelationDto> searchCount(@Param("unitId")String unitId, @Param("company")String company );

    /**
     * 河湖管理-河道绿化保洁（运行、维保工单统计）
     * @param unitId
     * @return
     */
    @Select("<script>SELECT b.*,a.company from st_company_base_relation b,st_company_base a " +
            " where b.type = 1 and b.st_company_base_id = a.id and b.data_id = 2 " +
            " <when test='unitId !=null'> and a.unit_id = #{unitId} </when>  " +
            " <when test='company !=null'> and a.company like concat('%',#{company},'%') </when> </script>")
    List<StCompanyBaseRelationDto> searchCount2(String unitId, @Param("company")String company);

    /**
     * 获取所有的公司
     * @param unitId
     * @return
     */
    @Select("<script>SELECT b.*,a.company from st_company_base_relation b,st_company_base a " +
            " where b.type = 1 and b.st_company_base_id = a.id " +
            " <when test='unitId !=null'> and a.unit_id = #{unitId} </when> </script>")
    List<StCompanyBaseRelationDto> searchCount3(String unitId);

    /**
     * 统计公司当年绿化工单数
     * @param stCompanyBaseId
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.order_type = #{orderType} and vwn.person_id = #{stCompanyBaseId} " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("orderType") String orderType,
                                    @Param("startTime") Date start, @Param("endTime") Date end);

    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.person_id = #{stCompanyBaseId} " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId2(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime") Date start, @Param("endTime") Date end);

    /**
     * 获取公司下已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.person_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId3(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);

    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.order_type in (#{orderType1},#{orderType2}) " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId4(@Param("orderType1") String orderType1, @Param("orderType2") String orderType2, @Param("startTime")Date start, @Param("endTime")Date end);

    /**
     * 获取公司下绿化保洁已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.person_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') and  vwn.order_type in ('2','3')" +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId5(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);

    /**
     * 获取公司下运行维保已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.person_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') and  vwn.order_type in ('4','5')" +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId6(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);
}
