package com.essence.interfaces.api;

import com.essence.common.utils.PageUtil;
import com.essence.dao.entity.EventCompanyDto;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.EventCompanyEsp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 第三方服务公司人员配置服务层
 * @author majunjie
 * @since 2023-06-05 11:24:54
 */
public interface EventCompanyService extends BaseApi<EventCompanyEsu, EventCompanyEsp, EventCompanyEsr> {
    String addTCompanyBase(EventCompanySave eventCompanySave);

    String updateTCompanyBase(EventCompanySave eventCompanySave);

    PageUtil<EventCompanyList> selectTCompanyBaseList(EventCompanySelect eventCompanySelect);

    String deleteTCompanyBase(EventCompanyList eventCompanyList);

    Map<String, List<EventCompanyPerson>> selectTCompanyByRiverId(EventCompanyQuery eventCompanyQuery);

    List<EventCompanyDto> selectEventCompany(String userId);
    //管河成效-第三方服务公司处理工单统计
    List<StCompanyBaseRelationDto> searchCount(String unitId, String company);

    Integer searchByStCompanyBaseId(String stCompanyBaseId, String orderType, Date start, Date end);

    Integer searchByStCompanyBaseId5(String stCompanyBaseId, Date start, Date end);

    Integer searchByStCompanyBaseId6(String stCompanyBaseId, Date start, Date end);

    List<StCompanyBaseRelationDto> searchCount2(String unitId, String company);

    List<StCompanyBaseRelationDto> searchCount3(String unitId);

    List<StCompanyBaseRelationDto> selectCompany(String unitId,String flag,Date start);

    Integer searchByStCompanyBaseId2(String stCompanyBaseId, Date start, Date end);

    Integer searchByStCompanyBaseId3(String stCompanyBaseId, Date start, Date end);

    Integer searchByStCompanyBaseId4(String s, String s1, Date start, Date end);

    /**
     * 根据三方人员userId获取功能手机号等
     * @param userId
     * @return
     */
    Object selectTCompanyBase(String userId);

    List<StCompanyBaseRelationDto> selectCompanys(String unitId,String company, String flag, Date start);

    /**
     * 获取第三方公司下拉列表
     * @param eventCompanyEsu
     * @return
     */
    List<EventCompanyRes> selectEventCompanyList(EventCompanyEsu eventCompanyEsu);

    /**
     * 获取三方养护人员的手机号等
     * @param orderManager
     */
    List<EventCompanyDto> selectManagerPhone(String orderManager);
}
