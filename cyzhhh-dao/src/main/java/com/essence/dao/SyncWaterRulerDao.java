package com.essence.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.SyncWaterRulerDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 电子水尺数据同步表(SyncWaterRuler)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-24 11:46:49
 */
@Mapper
public interface SyncWaterRulerDao extends BaseDao<SyncWaterRulerDto> {

    @Insert("<script>" +
            "INSERT INTO sync_water_ruler " +
            "(id, water_code, time, amount, swm, rksj, create_time) " +
            "VALUES" +
            "<foreach item='item' index='index' collection='list' separator=','>" +
            "(#{item.id}, #{item.waterCode}, #{item.time}, #{item.amount}, #{item.swm}, #{item.rksj}, #{item.createTime})" +
            "</foreach>" +
            "</script>")
    Integer insertAll(@Param("list") List<SyncWaterRulerDto> list);
}
