package video.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import video.entity.StWaterRateEntity;

/**
 * 水位站和流量站数据
 * 
 * @author
 * @email sunlightcs@gmail.com
 * @date 2022-10-13 19:46:43
 */
@Mapper
public interface StWaterRateDao extends BaseMapper<StWaterRateEntity> {
    @Insert("INSERT INTO `cyzhhh`.`message`(`id`, `topic`, `did`, `info`, `time`, `createTime`) VALUES (#{id}, #{topic}, #{did}, #{payload}, #{utime}, #{now})")
    void saveMessage(@Param("id") String id,@Param("topic") String topic,@Param("did") String did,@Param("payload") String payload,@Param("utime") String utime,@Param("now") String now);
}
