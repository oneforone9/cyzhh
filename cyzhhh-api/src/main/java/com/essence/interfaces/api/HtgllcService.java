package com.essence.interfaces.api;


import com.essence.dao.entity.HtglDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.HtgllcEsr;
import com.essence.interfaces.model.HtgllcEsu;
import com.essence.interfaces.param.HtgllcEsp;

import java.util.List;

/**
 * 合同管理历程服务层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */
public interface HtgllcService extends BaseApi<HtgllcEsu, HtgllcEsp, HtgllcEsr> {
    void saveData(HtglDto htglDto);


    void updateData(HtglDto htglDto);

    void updateDataCws(HtglDto newHt, String userId, Integer zt);

    void deleteData(List<String> ids);

    // 新的版本合同
    HtglDto saveNewData(HtglDto htglDto);

    /**
     * 更改合同管理历程数据
     *
     * @param htglDto
     */
    HtglDto updateNewData(HtglDto htglDto, HtglDto oldVO);

}
