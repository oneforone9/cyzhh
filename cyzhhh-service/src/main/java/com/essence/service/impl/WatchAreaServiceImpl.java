package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.WatchAreaService;
import com.essence.interfaces.dot.RiverEcologyDto;
import com.essence.interfaces.dot.RiverImageDto;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.WatchAreaDTO;
import com.essence.service.utils.RainFallLevelHd;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WatchAreaServiceImpl implements WatchAreaService {
    @Resource
    private WatchAreaDao watchAreaDao;
    @Resource
    private GridRainDataDao gridRainDataDao;
    @Resource
    private RiverEcologyDao riverEcologyDao;
    @Resource
    private RiverImageDao riverImageDao;
    @Resource
    private StStbprpBDao stStbprpBDao;
    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Resource
    private SectionBaseDao sectionBaseDao;
    @Resource
    private TownCheckDao townCheckDao ;

    @Override
    public WatchAreaDTO insert(WatchAreaDTO watchAreaDTO) {
        WatchAreaEntity watchAreaEntity = new WatchAreaEntity();
        List<String> image_urls = watchAreaDTO.getImageUrls();
        BeanUtil.copyProperties(watchAreaDTO,watchAreaEntity);
        watchAreaEntity.setGmtCreate(new Date());
        if (CollUtil.isNotEmpty(image_urls)){
            String s = JSONObject.toJSONString(image_urls);
            watchAreaEntity.setImageUrls(s);
        }
        if (StrUtil.isNotEmpty(watchAreaEntity.getId()) ){
            watchAreaDao.updateById(watchAreaEntity);
        }else {
            watchAreaDao.insert(watchAreaEntity);
        }
        return watchAreaDTO;
    }

    @Override
    public List<WatchAreaDTO> getWatchArea(String watchArea, String imageFlag) {
        if (null == imageFlag || "".equals(imageFlag)) {
            imageFlag= "1";//默认1查看图片
        }
        List<WatchAreaDTO> result = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("watch_area", watchArea);
        wrapper.eq("image_flag", imageFlag);
        List<WatchAreaEntity> watchAreaEntities = watchAreaDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(watchAreaEntities)){
            for (WatchAreaEntity watchAreaEntity : watchAreaEntities) {
                WatchAreaDTO watchAreaDTO = new WatchAreaDTO();
                BeanUtil.copyProperties(watchAreaEntity,watchAreaDTO);
                String image_urls = watchAreaEntity.getImageUrls();
                if (StrUtil.isNotEmpty(image_urls)){
                    List<String> strings = JSONObject.parseArray(image_urls, String.class);
                    watchAreaDTO.setImageUrls(strings);

                }
                result.add(watchAreaDTO);
            }
        }
        return result;
    }

    @Override
    public Object remove(String id) {
        watchAreaDao.deleteById(id);
        return null;
    }

    @Override
    public  List<Shiduan> getGridRainData() {
        List<Shiduan> res = new ArrayList<>();
        List<GridRainDataEntity> gridRainDataEntities = gridRainDataDao.selectList(new QueryWrapper<>());
        //网格 数值
        Map<String, BigDecimal> gridMap21 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH21));
        Map<String, BigDecimal> gridMap22 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH22));
        Map<String, BigDecimal> gridMap23 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH23));
        Map<String, BigDecimal> gridMap0 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH0));
        Map<String, BigDecimal> gridMap1 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH1));
        Map<String, BigDecimal> gridMap2 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH2));
        Map<String, BigDecimal> gridMap3 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH3));
        Map<String, BigDecimal> gridMap4 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH4));
        Map<String, BigDecimal> gridMap5 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH5));
        Map<String, BigDecimal> gridMap6 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH6));
        Map<String, BigDecimal> gridMap7 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH7));
        Map<String, BigDecimal> gridMap8 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH8));
        Map<String, BigDecimal> gridMap9 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH9));
        Map<String, BigDecimal> gridMap10 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH10));
        Map<String, BigDecimal> gridMap11 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH11));
        Map<String, BigDecimal> gridMap12 = gridRainDataEntities.parallelStream().collect(Collectors.toMap(GridRainDataEntity::getId, GridRainDataEntity::getH12));
        Shiduan dealData21 = getDealData(gridMap21, 21);
        Shiduan dealData22 = getDealData(gridMap22, 22);
        Shiduan dealData23 = getDealData(gridMap23, 23);
        Shiduan dealData0 = getDealData(gridMap0, 0);
        Shiduan dealData1 = getDealData(gridMap1, 1);
        Shiduan dealData2 = getDealData(gridMap2, 2);
        Shiduan dealData3 = getDealData(gridMap3, 3);
        Shiduan dealData4 = getDealData(gridMap4, 4);
        Shiduan dealData5 = getDealData(gridMap5, 5);
        Shiduan dealData6 = getDealData(gridMap6, 6);
        Shiduan dealData7 = getDealData(gridMap7, 7);
        Shiduan dealData8 = getDealData(gridMap8, 8);
        Shiduan dealData9 = getDealData(gridMap9, 9);
        Shiduan dealData10 = getDealData(gridMap10, 10);
        Shiduan dealData11 = getDealData(gridMap11, 11);
        Shiduan dealData12 = getDealData(gridMap12, 12);
        res.add(dealData21);
        res.add(dealData22);
        res.add(dealData23);
        res.add(dealData0);
        res.add(dealData1);
        res.add(dealData2);
        res.add(dealData3);
        res.add(dealData4);
        res.add(dealData5);
        res.add(dealData6);
        res.add(dealData7);
        res.add(dealData8);
        res.add(dealData9);
        res.add(dealData10);
        res.add(dealData11);
        res.add(dealData12);

        return res;
    }

    @Override
    public RiverEcologyDto getRiverEcology(String riverId) {
        QueryWrapper stWrapper = new QueryWrapper();
        stWrapper.eq("rvnm",riverId);
        List<StStbprpBEntity> stbprpBEntities = stStbprpBDao.selectList(stWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stbprpBEntities)){
            sttpMap = stbprpBEntities.parallelStream().collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        }

        QueryWrapper<VideoInfoBase> videoWrapper = new QueryWrapper();
        videoWrapper.eq("st_b_river_id",riverId);
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(videoWrapper);
        Map<String, List<VideoInfoBase>> videoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(videoInfoBases)){
            videoMap = videoInfoBases.parallelStream().filter(videoInfoBase -> {
                return StrUtil.isNotEmpty(videoInfoBase.getFunctionType());
            }).collect(Collectors.groupingBy(VideoInfoBase::getFunctionType));
        }
        QueryWrapper<SectionBase> secWrapper = new QueryWrapper();
        List<SectionBase> sectionBases = sectionBaseDao.selectList(secWrapper);
        //国考 0  市考 1  断面
        Map<Integer, List<SectionBase>> selectMap = new HashMap<>();
        if (CollUtil.isNotEmpty(sectionBases) ){
            selectMap = sectionBases.parallelStream().collect(Collectors.groupingBy(SectionBase::getSectionType));

        }

        QueryWrapper townWrapper = new QueryWrapper();
        townWrapper.eq("river_id",riverId);
        List<TownCheckEntity> townCheckEntities = townCheckDao.selectList(townWrapper);


        RiverEcologyDto ecologyDto = new RiverEcologyDto();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("river_id",riverId);
        RiverEcologyEntity riverEcologyEntity = riverEcologyDao.selectOne(queryWrapper);
        if (riverEcologyEntity != null){
            BeanUtil.copyProperties(riverEcologyEntity,ecologyDto);
            QueryWrapper<RiverImageEntity> imageQuery = new QueryWrapper();
            imageQuery.eq("t_river_ecology_id",riverEcologyEntity.getId());
            List<RiverImageEntity> list = riverImageDao.selectList(imageQuery);
            List<RiverImageDto> riverImageDtos = new ArrayList<>();
            if (CollUtil.isNotEmpty(list)){
                riverImageDtos = BeanUtil.copyToList(list,RiverImageDto.class);
            }
            ecologyDto.setRiverImageDtos(riverImageDtos);
        }
        ecologyDto.setDD(CollUtil.isNotEmpty(sttpMap.get("DD")) ? sttpMap.get("DD").size() : 0);
        ecologyDto.setDP(CollUtil.isNotEmpty(sttpMap.get("DP")) ? sttpMap.get("DP").size() : 0);
        ecologyDto.setSB(CollUtil.isNotEmpty(sttpMap.get("SB")) ? sttpMap.get("SB").size() : 0);
        ecologyDto.setZQ(CollUtil.isNotEmpty(sttpMap.get("ZQ")) ? sttpMap.get("ZQ").size() : 0);
        ecologyDto.setZZ(CollUtil.isNotEmpty(sttpMap.get("ZZ")) ? sttpMap.get("ZZ").size() : 0);
        ecologyDto.setSecurityMonitor(CollUtil.isNotEmpty(videoMap.get("2")) ? videoMap.get("2").size() : 0);
        ecologyDto.setFunctionMonitor(CollUtil.isNotEmpty(videoMap.get("1")) ? videoMap.get("1").size() : 0);

        if (CollUtil.isNotEmpty(selectMap.get("0"))){
            List<String> selectNames = selectMap.get("0").parallelStream().map(SectionBase::getSectionName).collect(Collectors.toList());
            ecologyDto.setCountrySelection(selectNames.size());
            ecologyDto.setCountrySelectionNames(selectNames);
        }

        if (CollUtil.isNotEmpty(selectMap.get("1")) ){
            List<String> selectNames = selectMap.get("1").parallelStream().map(SectionBase::getSectionName).collect(Collectors.toList());
            ecologyDto.setCitySelection(selectNames.size() );
            ecologyDto.setCitySelectionNames(selectNames);
        }
        if (CollUtil.isNotEmpty(townCheckEntities) ){
            List<String> selectNames = townCheckEntities.parallelStream().map(TownCheckEntity::getSelection).collect(Collectors.toList());
            ecologyDto.setTownSelection(selectNames.size() );
            ecologyDto.setTownSelectionNames(selectNames);
        }

        return ecologyDto;
    }

    public Shiduan getDealData(Map<String, BigDecimal> gridMap,int step){
        Shiduan shiduan = new Shiduan();
        Map<String,String> map = new HashMap<>();
        for (String s : gridMap.keySet()) {
            BigDecimal bigDecimal = gridMap.get(s);
            //将 cm 转化为 mm
            BigDecimal multiply = bigDecimal.multiply(new BigDecimal(10));
            BigDecimal divide = multiply.divide(new BigDecimal(1), 3, BigDecimal.ROUND_HALF_UP);
            String value =  "l:" + divide  + ",v:" + RainFallLevelHd.getRainFallLevelNew(bigDecimal);
            map.put(s,value);
        }
        shiduan.setList(map);
        shiduan.setT(step);
        return shiduan;
    }
}
