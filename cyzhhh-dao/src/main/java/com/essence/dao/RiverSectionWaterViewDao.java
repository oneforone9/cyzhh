package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RiverSectionWaterViewDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 河道断面水位视图数据库访问层
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/21 18:03
 */
@Mapper
public interface RiverSectionWaterViewDao extends BaseDao<RiverSectionWaterViewDto> {
}
