package com.essence.interfaces.api;


import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.UserWaterEsr;
import com.essence.interfaces.model.UserWaterEsu;
import com.essence.interfaces.param.UserWaterEsp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用水户取水量服务层
 * @author BINX
 * @since 2023-01-04 17:50:31
 */
public interface UserWaterService extends BaseApi<UserWaterEsu, UserWaterEsp, UserWaterEsr> {
    List<UserWaterStatisticDto> getStatistic();

    List<UserWaterStatisticDto> getStatistic2(String fileType);

    void inputExcel(MultipartFile file) throws IOException;


}
