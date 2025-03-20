package com.essence.service.impl.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RewardDealDao;
import com.essence.dao.entity.RewardDealEntity;

import java.util.Date;
import java.util.List;

public class ManageDateServiceListener implements ReadListener <RewardDealEntity>{

    RewardDealDao rewardDealDao;
    public ManageDateServiceListener(RewardDealDao rewardDealDao ){
        this.rewardDealDao = rewardDealDao;

    }
    @Override
    public void invoke(RewardDealEntity rewardDealEntity, AnalysisContext analysisContext) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("call_time",rewardDealEntity.getCallTime());
        List<RewardDealEntity> rewardDealEntities = rewardDealDao.selectList(wrapper);
        String format = DateUtil.format(new Date(), "yyyy-MM-dd");
        String updateMonth = format.substring(0, 7);
        rewardDealEntity.setUpdate_time(updateMonth);
        if (CollUtil.isNotEmpty(rewardDealEntities)){
            rewardDealDao.update(rewardDealEntity,wrapper);
        }else {
            rewardDealDao.insert(rewardDealEntity);
        }
        System.out.printf(rewardDealEntity.toString());

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
