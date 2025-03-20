package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RadarImagesLddzDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 雷达回波图  --单站雷达-大兴的(RadarImagesLddz)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-23 10:25:49
 */
@Mapper
public interface RadarImagesLddzDao extends BaseDao<RadarImagesLddzDto> {
}
