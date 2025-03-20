package com.essence.service.impl.third;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.DtoData;
import com.essence.dao.VideoWarningInfoDao;
import com.essence.dao.entity.VideoWarningInfoEntity;
import com.essence.interfaces.api.third.VideoReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class VideoReceiveServiceImpl implements VideoReceiveService {
    @Resource
    private VideoWarningInfoDao videoWarningInfoDao;
    @Override
    public void dealReceiveDate(List<DtoData> dtoDataList) {
        if (CollUtil.isNotEmpty(dtoDataList)){
            for (DtoData dtoData : dtoDataList) {
                VideoWarningInfoEntity videoWarningInfoEntity = new VideoWarningInfoEntity();
                BeanUtil.copyProperties(dtoData,videoWarningInfoEntity);
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("ManagerStartTime",videoWarningInfoEntity.getManagerStartTime());
                wrapper.eq("DeviceID",videoWarningInfoEntity.getDeviceID());
                VideoWarningInfoEntity history = videoWarningInfoDao.selectOne(wrapper);
                if (history !=null){
                    videoWarningInfoDao.update(videoWarningInfoEntity,wrapper);
                }else {
                    // 插入前 先进行过滤限制下 一天同一个ip只允许一个推送两个报警
                    String scannerIP = videoWarningInfoEntity.getScannerIP();
                    String managerStartTime = videoWarningInfoEntity.getManagerStartTime();
                    if (StrUtil.isNotEmpty(managerStartTime)) {
                        managerStartTime = managerStartTime.substring(0, managerStartTime.indexOf("T"));
                    }
                    QueryWrapper wrapper2 = new QueryWrapper();
                    wrapper2.eq("scannerIP", scannerIP);
                    wrapper2.like("ManagerStartTime", managerStartTime);
                    List<VideoWarningInfoEntity> history2 = videoWarningInfoDao.selectList(wrapper2);
                    if (CollUtil.isNotEmpty(history2) && history2.size() >= 2) {
                        log.info("报警推送限制，一天同一个ip只允许一个推送两个报警===============>");
                        continue;
                    }
                    videoWarningInfoDao.insert(videoWarningInfoEntity);
                }

            }
        }
    }
}
