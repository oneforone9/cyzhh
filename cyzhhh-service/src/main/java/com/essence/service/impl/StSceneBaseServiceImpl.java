package com.essence.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StSceneBaseDao;
import com.essence.dao.StSceneRelationDao;
import com.essence.dao.entity.ReaBase;
import com.essence.dao.entity.StSceneBaseDto;
import com.essence.dao.entity.StSceneRelationDto;
import com.essence.interfaces.api.StSceneBaseService;
import com.essence.interfaces.api.VideoInfoBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StSceneBaseEsr;
import com.essence.interfaces.model.StSceneBaseEsu;
import com.essence.interfaces.model.VideoInfoBaseEsr;
import com.essence.interfaces.param.StSceneBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSceneBaseEtoT;
import com.essence.service.converter.ConverterStSceneBaseTtoR;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 场景基本表(StSceneBase)业务层
 * @author liwy
 * @since 2023-06-01 14:47:39
 */
@Service
public class StSceneBaseServiceImpl extends BaseApiImpl<StSceneBaseEsu, StSceneBaseEsp, StSceneBaseEsr, StSceneBaseDto> implements StSceneBaseService {

    @Autowired
    private StSceneBaseDao stSceneBaseDao;
    @Autowired
    private StSceneRelationDao stSceneRelationDao;
    @Autowired
    private ConverterStSceneBaseEtoT converterStSceneBaseEtoT;
    @Autowired
    private ConverterStSceneBaseTtoR converterStSceneBaseTtoR;
    @Autowired
    private StSceneBaseService stSceneBaseService;
    @Autowired
    private VideoInfoBaseService videoInfoBaseService;

    public StSceneBaseServiceImpl(StSceneBaseDao stSceneBaseDao, ConverterStSceneBaseEtoT converterStSceneBaseEtoT, ConverterStSceneBaseTtoR converterStSceneBaseTtoR) {
        super(stSceneBaseDao, converterStSceneBaseEtoT, converterStSceneBaseTtoR);
    }

    /**
     * 新增场景
     * @param stSceneBaseEsu
     * @return
     */
    @Override
    public ResponseResult addStSceneBase(StSceneBaseEsu stSceneBaseEsu) {
        int insert;
        Integer id = stSceneBaseEsu.getId();
        if (null != id && !"".equals(id)) {
            //编辑场景名称
            insert = stSceneBaseService.update(stSceneBaseEsu);
            return ResponseResult.success("编辑场景成功",insert);
        }else{
            //新增场景
            //先判断当前登录人是否已经有场景名
            QueryWrapper queryWrapper = new  QueryWrapper();
            queryWrapper.eq("user_id",stSceneBaseEsu.getUserId());
            queryWrapper.eq("scene",stSceneBaseEsu.getScene());
            List list = stSceneBaseDao.selectList(queryWrapper);
            //场景名称不能重复
            if(list.size()!=0){
                return ResponseResult.success("场景名称不能重复",null);
            }else{
                insert= stSceneBaseService.insert(stSceneBaseEsu);//新增场景名
                return ResponseResult.success("新增场景成功",insert);
            }
        }
    }

    /**
     * 删除场景
     * @param stSceneBaseEsu
     * @return
     */
    @Override
    public ResponseResult deleteStSceneBase(StSceneBaseEsu stSceneBaseEsu) {
        //1.先将场景关联的摄像头进行删除
        QueryWrapper<StSceneRelationDto> wrapper = new QueryWrapper();
        wrapper.eq("scene_id",stSceneBaseEsu.getId());
        int delete = stSceneRelationDao.delete(wrapper);
        //2.再将场景基础表删除
        int i = stSceneBaseDao.deleteById(stSceneBaseEsu.getId());
        return ResponseResult.success("删除场景成功", delete + ";" + i);
    }

    @Override
    public ResponseResult<List<StSceneBaseEsr>> selectStSceneBase(PaginatorParam param) {
        PaginatorParam videoparam = new PaginatorParam();
        videoparam.setPageSize(0);
        videoparam.setCurrentPage(0);
        //获取所有摄像头
        Paginator<VideoInfoBaseEsr> p = videoInfoBaseService.searchAll(videoparam);
        List<VideoInfoBaseEsr> videoInfoBaseEsrs = p.getItems();
        Map<Integer, VideoInfoBaseEsr> videoMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(videoInfoBaseEsrs)){
            videoMap = videoInfoBaseEsrs.parallelStream().collect(Collectors.toMap(VideoInfoBaseEsr::getId, Function.identity()));
        }

        //1、先获取当前登录人的场景
        Paginator<StSceneBaseEsr> byPaginator = stSceneBaseService.findByPaginator(param);
        List<StSceneBaseEsr> items = byPaginator.getItems();
        //2.再关联到各个场景下的收藏的摄像头
        for (int i = 0; i < items.size(); i++) {
            StSceneBaseEsr stSceneBaseEsr = items.get(i);
            Integer id = stSceneBaseEsr.getId();//场景id
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("scene_id", id);
            List<StSceneRelationDto> list = stSceneRelationDao.selectList(queryWrapper);
            for (int k = 0; k < list.size(); k++) {
                StSceneRelationDto stSceneRelationDto = list.get(k);
                VideoInfoBaseEsr videoInfoBaseEsr = videoMap.get(stSceneRelationDto.getVideoId());
                list.get(k).setCode(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getCode());
                list.get(k).setName(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getName());
                list.get(k).setCameraType(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getCameraType());
                list.get(k).setStatus(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getStatus());
                list.get(k).setLgtd(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getLgtd());
                list.get(k).setLttd(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getLttd());
                list.get(k).setSource(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getSource());
                list.get(k).setUnitId(videoInfoBaseEsr == null  ? null : videoInfoBaseEsr.getUnitId());
            }
            items.get(i).setList(list);

        }

        return ResponseResult.success("获取场景成功", items);
    }
}
