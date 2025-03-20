package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.UserWaterBaseInfoDto;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用水户 基础信息,重复的取水权人 代表几个取水口(UserWaterBaseInfo)表数据库访问层
 *
 * @author BINX
 * @since 2024-01-03 14:04:19
 */
@Mapper
public interface UserWaterBaseInfoDao extends BaseDao<UserWaterBaseInfoDto> {
}
