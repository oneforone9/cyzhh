package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StGreenReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *绿化保洁工作日志上报表(StGreenReport)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-14 15:35:48
 */
@Mapper
public interface StGreenReportDao extends BaseDao<StGreenReportDto> {

    /**
     * 绿化保洁工作报告汇总列表
     * @return
     */
    @Select("<script>SELECT tba.*," +
            " (SELECT GROUP_CONCAT(sgrr.work_content) work_content  FROM st_green_report_relation sgrr where sgrr.st_green_id= tba.id ORDER BY sgrr.work_flag ASC) as work_content " +
            " from ( select gr.work_unit,gr.st_b_river_id, gr.rea_name,gr.work_type,base.unit_id,base.unit_name, " +
            " (select stgr.id from st_green_report stgr where  stgr.work_unit = gr.work_unit and stgr.st_b_river_id = gr.st_b_river_id and stgr.work_type = gr.work_type " +
            " ORDER BY stgr.gmt_create desc LIMIT 1) as id, " +
            " (select stgr.gmt_create from st_green_report stgr where  stgr.work_unit = gr.work_unit and stgr.st_b_river_id = gr.st_b_river_id and stgr.work_type = gr.work_type " +
            " ORDER BY stgr.gmt_create desc LIMIT 1) as gmt_create, " +
            " (select count(*) from st_green_report st where  st.work_unit =  gr.work_unit and st.st_b_river_id = gr.st_b_river_id and st.work_type = gr.work_type) as sumCount " +
            " from st_green_report gr,st_company_base base where base.id = gr.creator and gr.is_delete = 0 " +
            " and DATE_FORMAT(gr.gmt_create,'%Y-%m-%d')  BETWEEN #{startTime} and  #{endTime} " +
            " <when test='workUnit !=null'> and gr.work_unit like concat('%',#{workUnit},'%') </when> " +
            " <when test='stBRiverId !=null'> and gr.st_b_river_id = #{stBRiverId} </when> " +
            " <when test='unitId !=null'> and base.unit_id = #{unitId} </when> " +
            " <when test='workType !=null'> and gr.work_type = #{workType} </when> " +
            " GROUP BY gr.work_unit,gr.st_b_river_id,gr.work_type,gr.rea_name,base.unit_id,base.unit_name )tba </script> ")
    List<StGreenReportDto> searchAll(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                     @Param("workUnit") String workUnit, @Param("stBRiverId") String stBRiverId,
                                     @Param("unitId") String unitId,@Param("workType") String workType);
}
