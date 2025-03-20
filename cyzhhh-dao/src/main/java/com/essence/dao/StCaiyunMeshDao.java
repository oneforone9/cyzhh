package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* 彩云预报网格编号 (st_caiyun_mesh)表数据库访问层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Mapper
public interface StCaiyunMeshDao extends BaseDao<StCaiyunMeshDto> {

    @Select("select `value` as lttd from `cyzhhh`.`setting` where `key` = #{key};")
    public StCaiyunMeshDto getValue(@Param("key") String key);

    @Update("UPDATE `cyzhhh`.`setting` SET `value` = #{value} WHERE `key` = #{key};")
    public void updateKeyValue(@Param("key") String key,@Param("value") String value);

    @Update("INSERT INTO `cyzhhh`.`setting`(`key`, `value`) VALUES ('caiYunEnable', 'false');")
    public void insertKeyValue(@Param("key") String key,@Param("value") String value);

    @Select("select `key` as lgtd,`value` as lttd from `cyzhhh`.`setting` where `key` = #{key};")
    public StCaiyunMeshDto kvList(@Param("key") String key);


}
