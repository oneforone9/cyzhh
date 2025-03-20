package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.HtglscDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同管理收藏(Htglsc)表数据库访问层
 *
 * @author majunjie
 * @since 2024-09-13 16:18:29
 */
@Mapper
public interface HtglscDao extends BaseDao<HtglscDto> {
}
