package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.water.StQpModelDto;
import org.apache.ibatis.annotations.Mapper;

/**
* 水系联通-预报水位-河段断面关联表 (st_qp_model)表数据库访问层
*
* @author BINX
* @since 2023年5月11日 下午4:34:54
*/
@Mapper
public interface StQpModelDao extends BaseDao<StQpModelDto> {

}
