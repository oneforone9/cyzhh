package com.essence.interfaces.api;

import com.essence.dao.entity.EventBase;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.EventBaseEsp;

import java.util.Date;
import java.util.List;

/**
 * 事件基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-30 18:06:23
 */
public interface EventBaseService extends BaseApi<EventBaseEsu, EventBaseEsp, EventBaseEsr> {
    /**
     * 新增 字段不做任何处理
     *  仅用于小程序临时事件表添加到事件表中
     * @return
     */
    int add(EventBaseEsu eventBaseEsu);

    EventAnalysisDto getEventAnalysis(String unitId);

    EventChannelDto getEventChannel(String unitId);

    String uploadPictures(EventBasePictures eventBasePictures);

    String insertEventBase(EventBaseEsu eventBaseEsu);

    /**
     * 根据条件分页查询作业记录
     * @param param
     * @return
     */
    Paginator<EventBaseEsr> findByPaginatorBase(PaginatorParam param);

    List<EventBase> selectEventBase( Date start, Date end,String flag);
}
