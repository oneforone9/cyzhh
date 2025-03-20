package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.SectionDataViewEsr;
import com.essence.interfaces.model.SectionDataViewEsu;
import com.essence.interfaces.param.SectionDataViewEsp;

/**
 * 服务层
 * @author BINX
 * @since 2023-01-05 14:59:49
 */
public interface SectionDataViewService extends BaseApi<SectionDataViewEsu, SectionDataViewEsp, SectionDataViewEsr> {
    @Override
    int insert(SectionDataViewEsu esu);

    @Override
    int update(SectionDataViewEsu esu);

    int deleteById(String id);
}
