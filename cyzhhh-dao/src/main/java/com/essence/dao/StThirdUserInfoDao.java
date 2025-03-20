package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StThirdUserInfoDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对接第三方用户基本信息 单点登录(StThirdUserInfo)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-03 14:31:20
 */
@Mapper
public interface StThirdUserInfoDao extends BaseDao<StThirdUserInfoDto> {
}
