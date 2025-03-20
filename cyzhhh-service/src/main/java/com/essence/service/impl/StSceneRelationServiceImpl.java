package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StSceneRelationDao;
import com.essence.dao.entity.StSceneRelationDto;
import com.essence.interfaces.api.StSceneRelationService;
import com.essence.interfaces.model.StSceneRelationEsr;
import com.essence.interfaces.model.StSceneRelationEsu;
import com.essence.interfaces.param.StSceneRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSceneRelationEtoT;
import com.essence.service.converter.ConverterStSceneRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (StSceneRelation)场景关联表业务层
 * @author liwy
 * @since 2023-06-01 14:48:19
 */
@Service
public class StSceneRelationServiceImpl extends BaseApiImpl<StSceneRelationEsu, StSceneRelationEsp, StSceneRelationEsr, StSceneRelationDto> implements StSceneRelationService {

    @Autowired
    private StSceneRelationDao stSceneRelationDao;
    @Autowired
    private ConverterStSceneRelationEtoT converterStSceneRelationEtoT;
    @Autowired
    private ConverterStSceneRelationTtoR converterStSceneRelationTtoR;
    @Autowired
    private StSceneRelationService stSceneRelationService;

    public StSceneRelationServiceImpl(StSceneRelationDao stSceneRelationDao, ConverterStSceneRelationEtoT converterStSceneRelationEtoT, ConverterStSceneRelationTtoR converterStSceneRelationTtoR) {
        super(stSceneRelationDao, converterStSceneRelationEtoT, converterStSceneRelationTtoR);
    }

    /**
     * 收藏摄像头到场景
     * @param stSceneRelationEsu
     * @return
     */
    @Override
    public ResponseResult addStSceneRelation(StSceneRelationEsu stSceneRelationEsu) {
        Integer insert;
        //新增场景
        QueryWrapper queryWrapper = new  QueryWrapper();
        queryWrapper.eq("scene_id",stSceneRelationEsu.getSceneId());
        queryWrapper.eq("video_id",stSceneRelationEsu.getVideoId());
        List list = stSceneRelationDao.selectList(queryWrapper);

        if(list.size()!=0){
            return ResponseResult.success("摄像头已收藏到该场景，不能重复收藏",0);
        }else{
            insert = stSceneRelationService.insert(stSceneRelationEsu);//新增场景名
            return ResponseResult.success("收藏摄像头成功",insert);
        }

    }
}
