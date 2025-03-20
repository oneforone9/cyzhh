package com.essence.interfaces.api;

import com.essence.interfaces.dot.RiverEcologyDto;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.WatchAreaDTO;

import java.util.List;

/**
 * 观景区
 */
public interface WatchAreaService {
    WatchAreaDTO insert(WatchAreaDTO watchAreaDTO);

    List<WatchAreaDTO> getWatchArea(String watchArea, String imageFlag);

    Object remove(String id);

    /**
     * 查询雨量 网格数据
     * @return
     */
    List<Shiduan> getGridRainData();

    RiverEcologyDto getRiverEcology(String riverId);
}
