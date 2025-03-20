package com.essence.job.executor.delete;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StPumpDataDao;
import com.essence.dao.StWaterGateDao;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 定时删除数据 水闸 泵站数据 推送太多  每天执行一次定时删除 前5天的数据
 */
@Component
public class DeleteDataOfMonitor {
    @Resource
    private StWaterGateDao waterGateDao;
    @Resource
    private StPumpDataDao pumpDataDao;

    @XxlJob("DeleteDataOfMonitor")
    public void demoJobHandler() throws Exception {
        run();
        System.out.println("删除水闸 泵站数据" + new Date());
    }


    public void run(){
        //从今天开始往前推5 天开始删除数据
        DateTime r = DateUtil.offsetDay(new Date(), -5);
        String format = DateUtil.format(r, "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("ctime",format);
        waterGateDao.delete(wrapper);


        QueryWrapper query = new QueryWrapper();
        query.le("date",r);
        pumpDataDao.delete(query);

    }
}
