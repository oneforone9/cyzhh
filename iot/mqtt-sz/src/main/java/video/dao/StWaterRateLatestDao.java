package video.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import video.entity.StWaterRateLatestDto;

/**
 * 水位站和流量站测站最新数据(StWaterRateLatest)表数据库访问层
 *
 * @author BINX
 * @since 2023-06-08 16:42:16
 */
@Mapper
public interface StWaterRateLatestDao extends BaseMapper<StWaterRateLatestDto> {
}
