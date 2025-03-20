package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.dto.DeviceForRiverDTO;
import com.essence.common.dto.RiverRequestDTO;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.ReaBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.ReaBaseEsp;
import com.essence.service.baseconverter.PaginatorConverter;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterReaBaseEtoT;
import com.essence.service.converter.ConverterReaBaseRtoREX;
import com.essence.service.converter.ConverterReaBaseTtoR;
import com.essence.service.converter.ConverterReaBaseTtoREX;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class ReaBaseServiceImpl extends BaseApiImpl<ReaBaseEsu, ReaBaseEsp, ReaBaseEsr, ReaBase> implements ReaBaseService {

    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private ConverterReaBaseEtoT converterReaBaseEtoT;
    @Autowired
    private ConverterReaBaseTtoR converterReaBaseTtoR;
    @Autowired
    private ConverterReaBaseRtoREX converterReaBaseRtoREX;
    @Autowired
    private ConverterReaBaseTtoREX converterReaBaseTtoREX;
    @Resource
    private StStbprpBDao stStbprpBDao;
    @Resource
    private DeviceStatusDao deviceStatusDao;
    @Resource
    private UnitBaseDao unitBaseDao;
    @Resource
    private EventBaseDao eventBaseDao;
    @Resource
    private DepartmentBaseDao departmentBaseDao;
    @Resource
    private RelReaDepartmentDao relReaDepartmentDao;
    @Resource
    private WorkorderProcessDao workorderProcessDao;
    @Resource
    private WorkorderBaseDao workorderBaseDao;

    public ReaBaseServiceImpl(ReaBaseDao reaBaseDao, ConverterReaBaseEtoT converterReaBaseEtoT, ConverterReaBaseTtoR converterReaBaseTtoR) {
        super(reaBaseDao, converterReaBaseEtoT, converterReaBaseTtoR);
    }

    @Override
    public int insert(ReaBaseEsu reaBaseEsu) {
        reaBaseEsu.setId(UuidUtil.get32UUIDStr());
        return super.insert(reaBaseEsu);
    }

    @Override
    public Paginator<ReaBaseEsrEx> searchTree(PaginatorParam param) {
        // 1 根据分页查询获取结果（只查询一级河道 这个让前端加好条件）
        Paginator<ReaBaseEsr> paginator = this.findByPaginator(param);
        List<ReaBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return PaginatorConverter.pageToPaginator(paginator, new ArrayList<>());
        }
        // 1.1 对象转换
        List<ReaBaseEsrEx> reaBaseEsrExes = converterReaBaseRtoREX.toList(items);

        // 2 查询二三级河道
        List<ReaBaseEsrEx> reaBaseEsrExes23 = this.findrea23level();
        if (CollectionUtils.isEmpty(reaBaseEsrExes23)) {
            return PaginatorConverter.pageToPaginator(paginator, new ArrayList<>());
        }
        // 2.1 对二三级别河道分组
        Map<String, List<ReaBaseEsrEx>> rea23Map = reaBaseEsrExes23.stream().collect(Collectors.groupingBy(ReaBaseEsrEx::getReaLevel, Collectors.toList()));
        // 2.2 二级道
        List<ReaBaseEsrEx> reaBaseEsrExes2 = rea23Map.get(ItemConstant.REA_LEVEL_2);
        if (CollectionUtils.isEmpty(reaBaseEsrExes2)) {
            return PaginatorConverter.pageToPaginator(paginator, reaBaseEsrExes);
        }
        // 2.2 三级道
        List<ReaBaseEsrEx> reaBaseEsrExes3 = rea23Map.get(ItemConstant.REA_LEVEL_3);

        // 3 组装成树形数据

        // 一级河道转map
        Map<String, ReaBaseEsrEx> reaMap1 = reaBaseEsrExes.stream().collect(Collectors.toMap(ReaBaseEsrEx::getId, Function.identity(), (key1, key2) -> key2));
        // 二级河道转map
        Map<String, ReaBaseEsrEx> reaMap2 = reaBaseEsrExes2.stream().collect(Collectors.toMap(ReaBaseEsrEx::getId, Function.identity(), (key1, key2) -> key2));
        // 二级河道放入一级河道
        reaBaseEsrExes2.forEach(p -> {
            ReaBaseEsrEx reaBaseEsrEx = reaMap1.get(p.getUpId());
            if (null == reaBaseEsrEx) {
                return;
            }
            // 一级河道的行政区要从二级拼接
            reaBaseEsrEx.setAdName((null == reaBaseEsrEx.getAdName() ? "" : reaBaseEsrEx.getAdName() + "、") + (null == p.getAdName() ? "" : p.getAdName()));
            reaBaseEsrEx.getChildren().add(p);
        });

        // 三级河道放入二级河道
        if (CollectionUtils.isEmpty(reaBaseEsrExes3)) {
            return PaginatorConverter.pageToPaginator(paginator, reaBaseEsrExes);
        }
        reaBaseEsrExes3.forEach(p -> {
            ReaBaseEsrEx reaBaseEsrEx = reaMap2.get(p.getUpId());
            if (null == reaBaseEsrEx) {
                return;
            }
            // 计算面积
            // 水面面积(m2)(分岸别，左右岸共用数据只填右岸)在入数据库时控制
            reaBaseEsrEx.setWaterSpace((null == reaBaseEsrEx.getWaterSpace() ? 0 : reaBaseEsrEx.getWaterSpace()) + (null == p.getWaterSpace() ? 0 : p.getWaterSpace()));

            //  绿化面积(m2)(分岸别，左右岸共用数据只填右岸)在入数据库时控制
            reaBaseEsrEx.setGreenSpace((null == reaBaseEsrEx.getGreenSpace() ? 0 : reaBaseEsrEx.getGreenSpace()) + (null == p.getGreenSpace() ? 0 : p.getGreenSpace()));

            // 保洁面积(m2)(分岸别，左右岸共用数据只填右岸)在入数据库时控制
            reaBaseEsrEx.setCleanSpace((null == reaBaseEsrEx.getCleanSpace() ? 0 : reaBaseEsrEx.getCleanSpace()) + (null == p.getCleanSpace() ? 0 : p.getCleanSpace()));

            // 管理面积(m2)(分岸别，左右岸共用数据只填右岸)在入数据库时控制
            reaBaseEsrEx.setManageSpace((null == reaBaseEsrEx.getManageSpace() ? 0 : reaBaseEsrEx.getManageSpace()) + (null == p.getManageSpace() ? 0 : p.getManageSpace()));

            reaBaseEsrEx.getChildren().add(p);
        });
        return PaginatorConverter.pageToPaginator(paginator, reaBaseEsrExes);
    }

    /**
     * 获取2 3 级河道数据
     *
     * @return
     */
    private List<ReaBaseEsrEx> findrea23level() {
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("rea_level", Arrays.asList(ItemConstant.REA_LEVEL_2, ItemConstant.REA_LEVEL_3));

        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);

        return converterReaBaseTtoREX.toList(reaBases);
    }


    @Override
    public List<StatisticsBase> statistics() {
        // 1 查询数据
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rea_level", ItemConstant.REA_LEVEL_1);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(reaBases)) {
            return null;
        }
        // 2 分组统计
        Map<String, Long> collect = reaBases.stream().collect(Collectors.groupingBy(ReaBase::getReaType, Collectors.counting()));
        // 3 对象转换
        List<StatisticsBase> resultList = new ArrayList<>();
        collect.entrySet().forEach(p -> {
            StatisticsBase statisticsBase = new StatisticsBase();
            statisticsBase.setType(p.getKey());
            statisticsBase.setValue(p.getValue());
            resultList.add(statisticsBase);
        });
        return resultList;
    }

    @Override
    public List<ReaBaseEsr> searchLevel1() {
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rea_level", ItemConstant.REA_LEVEL_1);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        return converterReaBaseTtoR.toList(reaBases);
    }

    @Override
    public List<DeviceForRiverDTO> getDeviceOfRiver(RiverRequestDTO riverRequestDTO) {
        List<ReaBase> reaBases = new ArrayList<>();
        if(StrUtil.isNotEmpty(riverRequestDTO.getRiverId())  ){
            QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", riverRequestDTO.getRiverId());
            reaBases = reaBaseDao.selectList(queryWrapper);
        }else {
            //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
            QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.le("id", 31);
            reaBases = reaBaseDao.selectList(queryWrapper);
        }
        List<DeviceForRiverDTO> result = new ArrayList<>();

        if (CollUtil.isNotEmpty(reaBases)){
            List<String> reaIdList = reaBases.parallelStream().map(ReaBase::getId).collect(Collectors.toList());
           QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
            if(riverRequestDTO.getStnm() != null){
                stationWrapper.like("stnm",riverRequestDTO.getStnm());
            }
            if(riverRequestDTO.getRiverId() != null){
                stationWrapper.in("rvnm",reaIdList);
                stationWrapper.eq("zsection",1);
            }
            List<String> typeList  = new ArrayList<>();
            typeList.add("ZZ");
            typeList.add("ZQ");
            if(riverRequestDTO.getRiverId() == null) {
                stationWrapper.in("rvnm",reaIdList);
                stationWrapper.in("sttp",typeList);
            }
            List<StStbprpBEntity> list = stStbprpBDao.selectList(stationWrapper);

            List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(new QueryWrapper<>());
            Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
            if (CollUtil.isNotEmpty(deviceStatusEntities)){
                stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
            }
            List<StStbprpBEntityDTO> stStbprpBEntityDTOList = new ArrayList<>();
            for (StStbprpBEntity stStbprpBEntity : list) {
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                BeanUtil.copyProperties(stStbprpBEntity,stStbprpBEntityDTO);
                DeviceStatusEntity deviceStatusEntity = stcdMap.get(stStbprpBEntity.getStcd());
                if (deviceStatusEntity != null){
                    BeanUtil.copyProperties(deviceStatusEntity,stStbprpBEntityDTO);
                }else {
                    stStbprpBEntityDTO.setOnlineStatus("2");
                }
                stStbprpBEntityDTOList.add(stStbprpBEntityDTO);
            }
            Map<String, List<StStbprpBEntityDTO>> rvnmMap = new HashedMap<>();
            if (CollUtil.isNotEmpty(list)){
                rvnmMap = stStbprpBEntityDTOList.parallelStream().collect(Collectors.groupingBy(StStbprpBEntityDTO::getRvnm));
            }
            for (ReaBase reaBase : reaBases) {
                DeviceForRiverDTO deviceForRiverDTO = new DeviceForRiverDTO();
                BeanUtil.copyProperties(reaBase,deviceForRiverDTO);
                if (rvnmMap.get(reaBase.getId()) == null ){
                    continue;
                }
                deviceForRiverDTO.setDevices(rvnmMap.get(reaBase.getId()));
                result.add(deviceForRiverDTO);

            }
        }
        return result;
    }

    @Override
    public List<ReaBaseDTO> getRiverInfoList() {
        List<ReaBaseDTO> result = new ArrayList<>();
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(reaBases)){
            for (ReaBase reaBase : reaBases) {
                ReaBaseDTO reaBaseDTO = new ReaBaseDTO();
                BeanUtil.copyProperties(reaBase,reaBaseDTO);
                result.add(reaBaseDTO);
            }
        }
        return result;
    }

    @Override
    public List<UnitBaseDTO> getPatrolPreview() {
        List<UnitBaseDTO> result = new ArrayList<>();
        //查询河道班组
        List<UnitBase> unitBases = unitBaseDao.selectList(new QueryWrapper<>());
        List<String> unitIdList = unitBases.parallelStream().map(UnitBase::getId).collect(Collectors.toList());
        QueryWrapper<ReaBase> riverWrapper = new QueryWrapper();
        riverWrapper.in("unit_id",unitIdList);
        riverWrapper.le("id",31);
        List<ReaBase> reaBaseList = reaBaseDao.selectList(riverWrapper);
        //查询河道和巡查组关系
        List<RelReaDepartment> relReaDepartments = relReaDepartmentDao.selectList(new QueryWrapper<>());
        Map<String, String> realDepartMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(relReaDepartments)){
            realDepartMap = relReaDepartments.parallelStream().collect(Collectors.toMap(RelReaDepartment::getReaId, RelReaDepartment::getDepartmentId));
        }

        //默认事件需要 之前的15天
        Date endDate = new Date();
        DateTime startDate = DateUtil.offsetMonth(endDate, -15);
        QueryWrapper<EventBase> eventBaseQueryWrapper = new QueryWrapper<>();
        eventBaseQueryWrapper.le("event_time",endDate);
        eventBaseQueryWrapper.ge("event_time",startDate);
        List<EventBase> eventBases = eventBaseDao.selectList(eventBaseQueryWrapper);
        Map<String, List<EventBase>> earEventMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(eventBases)){
            earEventMap = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getReaId));
        }
        Map<String, List<ReaBase>> reaMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBaseList)){
            reaMap = reaBaseList.parallelStream().collect(Collectors.groupingBy(ReaBase::getUnitId));
        }
        if (CollUtil.isNotEmpty(unitBases)){
            for (UnitBase unitBase : unitBases) {
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("unit_id",unitBase.getId());
                List<DepartmentBase> departmentBases = departmentBaseDao.selectList(queryWrapper);
                Map<String, String> departMap = new HashedMap<>();
                if (CollUtil.isNotEmpty(departmentBases)){
                    departMap = departmentBases.parallelStream().collect(Collectors.toMap(DepartmentBase::getId,DepartmentBase::getDepartmentName,(o1,o2)->o2 ));
                }

                UnitBaseDTO unitBaseDTO = new UnitBaseDTO();
                BeanUtil.copyProperties(unitBase,unitBaseDTO);
                List<ReaBase> reaBaseList1 = reaMap.get(unitBaseDTO.getId());
                List<ReaBaseDTO> reaBaseDTOList = new ArrayList<>();
                if (CollUtil.isNotEmpty(reaBaseList1)){
                    for (ReaBase reaBase : reaBaseList1) {
                        ReaBaseDTO reaBaseDTO = new ReaBaseDTO();
                        BeanUtil.copyProperties(reaBase,reaBaseDTO);
                        List<EventBase> eventBases1 = earEventMap.get(reaBaseDTO.getId());
                        List<EventBaseDTO> eventBaseDTOList = new ArrayList<>();
                        if (CollUtil.isNotEmpty(eventBases1)){
                            for (EventBase eventBase : eventBases1) {
                                EventBaseDTO eventBaseDTO = new EventBaseDTO();
                                BeanUtil.copyProperties(eventBase,eventBaseDTO);
                                String problemDesc = eventBase.getProblemDesc();
                                if (StrUtil.isNotEmpty(problemDesc)){
                                    try {
                                        List<String> strings = JSONObject.parseArray(problemDesc, String.class);
                                        eventBaseDTO.setProblemDesc(strings);
                                    } catch (Exception e) {
                                        List<String> stringList = new ArrayList<>();
                                        eventBaseDTO.setProblemDesc(stringList);
                                        stringList.add(problemDesc);
//                                        e.printStackTrace();
                                    }
                                }
                                String departId = realDepartMap.get(eventBaseDTO.getReaId());
                                if (StrUtil.isNotEmpty(departId)){
                                    String departName = departMap.get(departId);
                                    eventBaseDTO.setUnitName(departName);
                                }else {
                                    eventBaseDTO.setUnitName(null);
                                }
                                eventBaseDTOList.add(eventBaseDTO);
                            }
                        }
                        reaBaseDTO.setEventBaseDTOList(eventBaseDTOList);
                        reaBaseDTOList.add(reaBaseDTO);
                    }
                }
                unitBaseDTO.setReaBaseDTOList(reaBaseDTOList);
                result.add(unitBaseDTO);
            }
        }
        return result;
    }

    @Override
    public ReaViewStatisticDto getReaViewData(String unitId) {
        ReaViewStatisticDto result = new ReaViewStatisticDto();
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        if (StrUtil.isNotEmpty(unitId)){
            queryWrapper.eq("unit_id", unitId);
        }
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(reaBases)){
            Map<String, List<ReaBase>> reaType = reaBases.stream().collect(Collectors.groupingBy(ReaBase::getReaType));
            List<ReaBase> rea = reaType.get("1");
            List<ReaBase> reaG = reaType.get("2");
            List<ReaBase> reaQ = reaType.get("3");
            result.setRiver(CollUtil.isNotEmpty(rea) ? rea.size() : 0);
            result.setRiverG(CollUtil.isNotEmpty(reaG) ? reaG.size() : 0);
            result.setRiverQ(CollUtil.isNotEmpty(reaQ) ? reaQ.size() : 0);

            if (CollUtil.isNotEmpty(rea)){
                List<ReaBase> second = reaBases.stream().filter(reaBase -> {return reaBase.getRank() != null;}).filter(reaBase -> {return reaBase.getRank() == 2;}).collect(Collectors.toList());
                List<ReaBase> third = reaBases.stream().filter(reaBase -> {return reaBase.getRank() != null;}).filter(reaBase -> {return reaBase.getRank() == 3;}).collect(Collectors.toList());
                result.setSecondRiver(CollUtil.isNotEmpty(second) ? second.size() : 0);
                result.setThirdRiver(CollUtil.isNotEmpty(third) ? third.size() : 0);
            }
        }
        QueryWrapper wrapper = new QueryWrapper();
        DateTime beginOfDay = DateUtil.beginOfDay(new Date());
        DateTime endOfDay = DateUtil.endOfDay(new Date());
        if (StrUtil.isEmpty(unitId)){
            wrapper.eq("unit_id", unitId);
        }
        wrapper.ge("start_time",beginOfDay);
        wrapper.le("start_time",endOfDay);
        List<WorkorderBase> selectList = workorderBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(selectList)){
            List<String> orderIds = selectList.stream().map(WorkorderBase::getId).collect(Collectors.toList());
            QueryWrapper queryProcess = new QueryWrapper();
            queryProcess.in("order_id",orderIds);
            queryProcess.ge("order_status",ItemConstant.ORDER_STATUS_EXAMINNING);
            List<WorkorderProcess> workorderProcesses = workorderProcessDao.selectList(queryProcess);
            result.setPortalNum(CollUtil.isNotEmpty(workorderProcesses) ? workorderProcesses.size() : 0);
        }
        return result;
    }
}
