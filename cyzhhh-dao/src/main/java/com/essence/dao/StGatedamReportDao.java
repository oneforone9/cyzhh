package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StGatedamReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 闸坝运行维保日志上报表(StGatedamReport)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-15 11:56:05
 */
@Mapper
public interface StGatedamReportDao extends BaseDao<StGatedamReportDto> {

    /**
     * PC端-闸坝运行养护工作报告汇总列表
     * @param startTime
     * @param endTime
     * @param workUnit
     * @param stBRiverId
     * @param unitId
     * @param workType
     * @return
     */
    @Select("<script>SELECT tba.*," +
            " (SELECT GROUP_CONCAT(sgrr.work_content) work_content  FROM st_gatedam_report_relation sgrr where sgrr.st_gatedam_id= tba.id ORDER BY sgrr.work_flag ASC) as work_content " +
            " from ( select gr.work_unit,gr.st_b_river_id, gr.rea_name,gr.work_type,gr.sttp,gr.stcd,gr.stnm,base.unit_id,base.unit_name, " +
            " (select stgr.id from st_gatedam_report stgr where  stgr.work_unit = gr.work_unit and stgr.st_b_river_id = gr.st_b_river_id and stgr.work_type = gr.work_type and stgr.sttp = gr.sttp " +
            " and stgr.stnm = gr.stnm ORDER BY stgr.gmt_create desc LIMIT 1) as id, " +
            " (select stgr.gmt_create from st_gatedam_report stgr where  stgr.work_unit = gr.work_unit and stgr.st_b_river_id = gr.st_b_river_id and stgr.work_type = gr.work_type and stgr.sttp = gr.sttp " +
            " and stgr.stnm = gr.stnm ORDER BY stgr.gmt_create desc LIMIT 1) as gmt_create, " +
            " (select count(*) from st_gatedam_report st where  st.work_unit =  gr.work_unit and st.st_b_river_id = gr.st_b_river_id and st.work_type = gr.work_type " +
            " and st.sttp = gr.sttp and st.stnm = gr.stnm) as sumCount " +
            " from st_gatedam_report gr,st_company_base base where base.id = gr.creator and gr.is_delete = 0 " +
            " and DATE_FORMAT(gr.gmt_create,'%Y-%m-%d') BETWEEN  #{startTime} and  #{endTime} " +
            " <when test='workUnit !=null'> and gr.work_unit like concat('%',#{workUnit},'%') </when> " +
            " <when test='stBRiverId !=null'> and gr.st_b_river_id = #{stBRiverId} </when> " +
            " <when test='unitId !=null'> and base.unit_id = #{unitId} </when> " +
            " <when test='workType !=null'> and gr.work_type = #{workType} </when> " +
            " <when test='sttp !=null'> and gr.sttp = #{sttp} </when> " +
            " GROUP BY gr.work_unit,gr.st_b_river_id,gr.work_type,gr.sttp,gr.stnm,gr.rea_name,gr.stcd,base.unit_id,base.unit_name )tba</script>")
    List<StGatedamReportDto> searchAll(@Param("startTime") String startTime, @Param("endTime")String endTime,
                                       @Param("workUnit")String workUnit, @Param("stBRiverId")String stBRiverId,
                                       @Param("unitId") String unitId,@Param("workType") String workType,@Param("sttp") String sttp);

    /**
     * 闸坝运行养护统计
     * @param stBRiverId
     * @param mouth
     * @return
     */
    @Select("<script>SELECT count(1) as mouthCount,gr.sttp from st_gatedam_report gr,st_company_base base  where  gr.is_delete = 0 and base.id = gr.creator " +
            " and DATE_FORMAT(gr.gmt_create,'%Y-%m') = #{mouth} " +
            " <when test='stBRiverId !=null'> and gr.st_b_river_id = #{stBRiverId} </when> " +
            " <when test='unitId !=null'> and base.unit_id = #{unitId} </when> " +
            " GROUP BY gr.sttp</script>")
    List<Map> searchCount(@Param("stBRiverId") String stBRiverId, @Param("mouth") String mouth, @Param("unitId") String unitId);

    /**
     * 当年统计
     * @param stBRiverId
     * @param year
     * @return
     */
    @Select("<script>SELECT count(1) as yearCount,sgr.sttp from st_gatedam_report sgr,st_company_base base where sgr.is_delete = 0 and base.id = sgr.creator " +
            " and DATE_FORMAT(sgr.gmt_create,'%Y') = #{year}  " +
            " <when test='stBRiverId !=null'> and sgr.st_b_river_id = #{stBRiverId} </when> " +
            " <when test='unitId !=null'> and base.unit_id = #{unitId} </when> " +
            " GROUP BY sgr.sttp</script>")
    List<Map> searchCountYear(@Param("stBRiverId") String stBRiverId, @Param("year") String year, @Param("unitId") String unitId);
}
