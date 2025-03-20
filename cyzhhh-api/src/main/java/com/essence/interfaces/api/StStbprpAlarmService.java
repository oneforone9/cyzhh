package com.essence.interfaces.api;


import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StStbprpAlarmEsr;
import com.essence.interfaces.model.StStbprpAlarmEsu;
import com.essence.interfaces.param.StStbprpAlarmEsp;

/**
 * 水位流量站报警阀值配置表服务层
 * @author BINX
 * @since 2023-02-25 16:56:28
 */
public interface StStbprpAlarmService extends BaseApi<StStbprpAlarmEsu, StStbprpAlarmEsp, StStbprpAlarmEsr> {

    StStbprpAlarmDto selectStStbprpAlarm(StStbprpAlarmEsu stStbprpAlarmEsu);

    /**
     * 删除数据配置
     * @param stcd
     * @return
     */
    Integer deleteStStbprpAlarm(String stcd);
}
