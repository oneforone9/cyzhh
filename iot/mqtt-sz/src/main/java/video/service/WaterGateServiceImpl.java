package video.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import video.dao.*;
import video.dto.WaterGateContentDTO;
import video.dto.WaterGateDTO;
import video.entity.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class WaterGateServiceImpl implements WaterGateService{
    @Resource
    private WaterGateDao WaterGateDao;
    @Resource
    private WaterBzDao waterBzDao;
//    @Resource
//    private StWaterGateHistoryDao stWaterGateHistoryDao;

    @Resource
    private StWaterBzHistoryDao waterBzHistoryDao;
    @Resource
    private WaterGateControlDeviceDao waterGateControlDeviceDao;

    @Resource
    private WaterPumpControlDeviceDao waterPumpControlDeviceDao;
    @Override
    public void dealWaterGate(String context) {
        Date date = new Date();
        Date lastDay = DateUtil.endOfDay(DateUtil.offsetDay(date,-1)) ;
        String format = DateUtil.format(lastDay, "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("ctime",format);
        waterBzDao.delete(wrapper);

        String end = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String start = DateUtil.format(DateUtil.offsetHour(date, -1), "yyyy-MM-dd HH:mm:ss");
        WaterGateDTO waterGateContentDTO = JSONObject.parseObject(context, WaterGateDTO.class);
        if (waterGateContentDTO != null){
            String did = waterGateContentDTO.getDid();
            List<WaterGateContentDTO> content = waterGateContentDTO.getContent();
            if (CollUtil.isNotEmpty(content)){
                for (WaterGateContentDTO gateContentDTO : content) {
                    String addr = gateContentDTO.getAddr();
                    String type = gateContentDTO.getType();
                    String pid = gateContentDTO.getPid();
                    String ctime = gateContentDTO.getCtime().replace("/", "-");
                    String addrv = gateContentDTO.getAddrv();

                    StWaterGateData stWaterGateData = new StWaterGateData();
                    stWaterGateData.setPid(pid);
                    stWaterGateData.setType(type);
                    stWaterGateData.setAddr(addr);
                    stWaterGateData.setAddrv(addrv);
                    stWaterGateData.setCtime(ctime);
                    stWaterGateData.setDid(did);
                    QueryWrapper queryWrapperHis = new QueryWrapper();
                    queryWrapperHis.eq("did",did);
                    queryWrapperHis.eq("addr",addr);
                    queryWrapperHis.between("ctime",start,end);
                    List<StWaterGateData> stWaterGateData1 = WaterGateDao.selectList(queryWrapperHis);
                    if (CollUtil.isNotEmpty(stWaterGateData1)){
                        continue;
                    }
                    WaterGateDao.insert(stWaterGateData);
//
//                    StWaterGateHistoryDto stWaterGateHistoryDto = new StWaterGateHistoryDto();
//                    BeanUtil.copyProperties(stWaterGateData,stWaterGateHistoryDto);
//                    stWaterGateHistoryDao.insert(stWaterGateHistoryDto);
                    //保存或者更新 did 和  pid 的关系
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("did",did);
                    WaterGateControlDevice waterGateControlDevice = waterGateControlDeviceDao.selectOne(queryWrapper);

                    if (waterGateControlDevice!= null){
                        waterGateControlDevice.setPid(pid);
                        waterGateControlDeviceDao.update(waterGateControlDevice,queryWrapper);
                    }else {
                        WaterGateControlDevice control = new WaterGateControlDevice();
                        control.setDid(did);
                        control.setPid(pid);
                        waterGateControlDeviceDao.insert(control);
                    }

                }
            }

        }


    }


    @Override
    public void dealWaterBz(String context) {
        Date date = new Date();
        Date lastDay = DateUtil.endOfDay(DateUtil.offsetDay(date,-1)) ;
        String format = DateUtil.format(lastDay, "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("ctime",format);
        waterBzDao.delete(wrapper);

        WaterGateDTO waterGateContentDTO = JSONObject.parseObject(context, WaterGateDTO.class);
        if (waterGateContentDTO != null){
            String did = waterGateContentDTO.getDid();
            String replace = waterGateContentDTO.getUtime().replace("/", "-");
            List<WaterGateContentDTO> content = waterGateContentDTO.getContent();
            if (CollUtil.isNotEmpty(content)){
                for (WaterGateContentDTO gateContentDTO : content) {
                    String addr = gateContentDTO.getAddr();
                    String type = gateContentDTO.getType();
                    String pid = gateContentDTO.getPid();
                    String ctime = gateContentDTO.getCtime().replace("/", "-");
                    String addrv = gateContentDTO.getAddrv();

                    StWaterBzData stWaterGateData = new StWaterBzData();
                    stWaterGateData.setPid(pid);
                    stWaterGateData.setType(type);
                    stWaterGateData.setAddr(addr);
                    stWaterGateData.setAddrv(addrv);
                    stWaterGateData.setCtime(ctime);
                    stWaterGateData.setDid(did);
                    waterBzDao.insert(stWaterGateData);
//                    StWaterBzHistoryDto stWaterGateHistoryDto = new StWaterBzHistoryDto();
//                    BeanUtil.copyProperties(stWaterGateData,stWaterGateHistoryDto);
//                    waterBzHistoryDao.insert(stWaterGateHistoryDto);
                    //保存或者更新 did 和  pid 的关系
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("did",did);
                    WaterPumpControlDevice waterGateControlDevice = waterPumpControlDeviceDao.selectOne(queryWrapper);

                    if (waterGateControlDevice!= null){
                        waterGateControlDevice.setPid(pid);
                        waterPumpControlDeviceDao.update(waterGateControlDevice,queryWrapper);
                    }else {
                        WaterPumpControlDevice control = new WaterPumpControlDevice();
                        control.setDid(did);
                        control.setPid(pid);
                        waterPumpControlDeviceDao.insert(control);
                    }
                }
            }
        }

        }
}
