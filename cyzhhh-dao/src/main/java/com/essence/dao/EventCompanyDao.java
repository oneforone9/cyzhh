package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.EventCompanyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 第三方服务公司人员配置(TEventCompany)表数据库访问层
 *
 * @author majunjie
 * @since 2023-06-05 11:24:44
 */
@Mapper
public interface EventCompanyDao extends BaseDao<EventCompanyDto> {
    @Select("SELECT company_id FROM t_event_company WHERE company= #{company} LIMIT 1")
    String selectEventCompanyId(@Param("company") String company);

    /**
     * 统计公司当年绿化工单数
     * @param stCompanyBaseId
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.order_type = #{orderType} and vwn.company_id = #{stCompanyBaseId} " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("orderType") String orderType,
                                    @Param("startTime") Date start, @Param("endTime") Date end);
    /**
     * 获取公司下绿化保洁已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.company_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') and  vwn.order_type in ('2','3')" +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId5(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);

    /**
     * 获取公司下运行维保已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.company_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') and  vwn.order_type in ('4','5')" +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId6(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);

    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.company_id = #{stCompanyBaseId} " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId2(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime") Date start, @Param("endTime") Date end);

    /**
     * 获取公司下已完成工单总数（待审核和已归档的）
     * @param stCompanyBaseId
     * @param start
     * @param end
     * @return
     */
    @Select("SELECT count(1) from v_workorder_newest vwn  where   vwn.company_id = #{stCompanyBaseId} and vwn.order_status in ('4','5') " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId3(@Param("stCompanyBaseId") String stCompanyBaseId, @Param("startTime")Date start, @Param("endTime")Date end);

    @Select("SELECT count(1) from v_workorder_newest vwn  where vwn.order_type in (#{orderType1},#{orderType2}) " +
            "and DATE_FORMAT(vwn.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} ")
    Integer searchByStCompanyBaseId4(@Param("orderType1") String orderType1, @Param("orderType2") String orderType2, @Param("startTime")Date start, @Param("endTime")Date end);

    /**
     * 获取第三方公司下拉列表
     * @param type
     * @param unitId
     */
    @Select("<script> select a.company_id,a.company,a.unit_id,a.unit_name,a.type from t_event_company a where 1=1 " +
            " <when test='type !=null'> and a.type = #{type} </when> " +
            " <when test='unitId !=null'> and a.unit_id = #{unitId} </when> " +
            " GROUP BY a.company_id,a.company,a.unit_id </script>")
    List<EventCompanyDto> selectEventCompanyList(@Param("type")Integer type, @Param("unitId")String unitId);

    /**
     * 根据第三方公司获取三方公司的负责人
     * @param companyId
     * @return
     */
    @Select("<script> select a.name,a.user_id, a.phone from t_event_company a " +
            " where company_id = #{companyId} " +
            " <when test='type !=null'> and a.type = #{type} </when> " +
            " <when test='unitId !=null'> and a.unit_id = #{unitId} </when> " +
            " GROUP BY a.name,a.user_id, a.phone </script>")
    List<EventCompanyDto> selectUserBycompanyId(@Param("companyId") String companyId, @Param("type") Integer type, @Param("unitId") String unitId);



}
