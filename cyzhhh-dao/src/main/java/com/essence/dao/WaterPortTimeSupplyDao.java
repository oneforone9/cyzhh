package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WaterPortTimeSupplyDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 补水口导入时间序列流量数据无caseId回显(WaterPortTimeSupply)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-06 15:47:02
 */
@Mapper
public interface WaterPortTimeSupplyDao extends BaseDao<WaterPortTimeSupplyDto> {
}
