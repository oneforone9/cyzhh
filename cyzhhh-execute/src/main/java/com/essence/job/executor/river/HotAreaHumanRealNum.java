package com.essence.job.executor.river;

import cn.hutool.core.collection.CollUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.essence.dao.StCrowdRealDao;
import com.essence.dto.HumanHotDto;

import com.essence.entity.StCrowdRealDto;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class HotAreaHumanRealNum {

    @Resource
    private StCrowdRealDao stCrowdRealDao;

    //实时游客地址
    private static final String APIREAL = "http://sw.swj.beijing.gov.cn/uacp/datainterface/getdata//bootstraptable/-1/DRLYXSRS-202061717386";
    /**
     * 获取实时实时游客
     * @throws Exception
     */
    @XxlJob("HotAreaHumanNumReal")
    public void jobHandler() throws Exception {
        doExecuteReal();

    }

    /**
     * 获取实时实时游客
     */
    private void doExecuteReal() {
        Map<String,Object> param1 = new HashMap<>();
        param1.put("ssq","朝阳区");
        System.out.println("开始请求热点数据----15个热点区域--------开始");
        HttpResponse response = HttpRequest.get(APIREAL).form(param1).timeout(90000).execute();
        int statusCode = response.getStatus();
        if (statusCode != 200) {
            System.err.println("请求热点数据-，实时实时游客，HTTP状态码: " + statusCode);
            return;
        }
        String body = response.body();
        List<StCrowdRealDto> stCrowdRealDtos = new ArrayList<>();
        if (StrUtil.isNotEmpty(body)){
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONArray rows = jsonObject.getJSONArray("rows");
            List<HumanHotDto> humanHotDtos = rows.toJavaList(HumanHotDto.class);
            for (HumanHotDto humanHotDto : humanHotDtos) {
                StCrowdRealDto stCrowdRealDto = new StCrowdRealDto();
                stCrowdRealDto.setDate(humanHotDto.getVALUE4());
                stCrowdRealDto.setArea(humanHotDto.getVALUE1());
                stCrowdRealDto.setNum(Integer.valueOf(humanHotDto.getVALUE3()) );
                stCrowdRealDto.setWaterUnit(humanHotDto.getVALUE2());
                stCrowdRealDto.setRvnm(humanHotDto.getVALUE());
                stCrowdRealDtos.add(stCrowdRealDto);
            }
        }

        if (CollUtil.isNotEmpty(stCrowdRealDtos)){
            for (StCrowdRealDto stCrowdRealDto : stCrowdRealDtos) {
                String date = stCrowdRealDto.getDate();
                String area = stCrowdRealDto.getArea();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date",date);
                wrapper.eq("area",area);
                List<StCrowdRealDto> stCrowdRealDaos1 = stCrowdRealDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stCrowdRealDaos1)){
                    stCrowdRealDao.update(stCrowdRealDto,wrapper);
                }else {
                    stCrowdRealDao.insert(stCrowdRealDto);
                }
            }
        }

        System.out.println("开始请求热点数据----15个热点区域--------结束");
    }


}
