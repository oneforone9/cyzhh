package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RadarImagesLdDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 雷达回波图  --华北地区的(RadarImagesLd)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-23 10:25:21
 */
@Mapper
public interface RadarImagesLdDao extends BaseDao<RadarImagesLdDto> {
}
