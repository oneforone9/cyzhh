package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RadarImagesWxDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 卫星云图(RadarImagesWx)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-23 10:26:06
 */
@Mapper
public interface RadarImagesWxDao extends BaseDao<RadarImagesWxDto> {
}
