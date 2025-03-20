package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.RadarImagesLdEsp;
import com.essence.interfaces.model.RadarImagesLdEsu;
import com.essence.interfaces.model.RadarImagesLdEsr;

/**
 * 雷达回波图  --华北地区的服务层
 * @author BINX
 * @since 2023-04-23 10:22:02
 */
public interface RadarImagesLdService extends BaseApi<RadarImagesLdEsu, RadarImagesLdEsp, RadarImagesLdEsr> {
}
