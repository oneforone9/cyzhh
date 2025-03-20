package video.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import video.entity.StWaterGateHistoryDto;

/**
 * (StWaterGate)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-20 15:36:28
 */
@Mapper
public interface StWaterGateHistoryDao extends BaseMapper<StWaterGateHistoryDto> {

}
