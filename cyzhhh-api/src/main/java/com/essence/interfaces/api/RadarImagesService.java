package com.essence.interfaces.api;

import com.essence.interfaces.entity.PaginatorParam;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/23 11:40
 */
public interface RadarImagesService {
    Object getRadarLdImage(PaginatorParam param);

    Object getRadarLddzImage(PaginatorParam param);

    Object getRadarWxImage(PaginatorParam param);
}
