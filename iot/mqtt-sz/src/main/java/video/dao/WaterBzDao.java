package video.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import video.entity.StWaterBzData;
import video.entity.StWaterGateData;

@Mapper
public interface WaterBzDao extends BaseMapper<StWaterBzData> {
}
