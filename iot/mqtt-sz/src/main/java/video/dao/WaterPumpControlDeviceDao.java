package video.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import video.entity.WaterGateControlDevice;
import video.entity.WaterPumpControlDevice;

@Mapper
public interface WaterPumpControlDeviceDao extends BaseMapper<WaterPumpControlDevice> {
}
