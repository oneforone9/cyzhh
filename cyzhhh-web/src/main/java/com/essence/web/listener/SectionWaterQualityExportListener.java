package com.essence.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.essence.interfaces.api.SectionWaterQualityService;
import com.essence.interfaces.model.SectionWaterQualityEsu;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * SectionWaterQualityExportListener
 *
 * @author bird
 * @date 2022/11/19 17:39
 * @Description
 */
@Slf4j
public class SectionWaterQualityExportListener implements ReadListener<SectionWaterQualityEsu> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<SectionWaterQualityEsu> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private SectionWaterQualityService sectionWaterQualityService;


    private String userId;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param sectionWaterQualityService
     */
    public SectionWaterQualityExportListener(SectionWaterQualityService sectionWaterQualityService,String userId) {
        this.sectionWaterQualityService = sectionWaterQualityService;
        this.userId = userId;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(SectionWaterQualityEsu data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        sectionWaterQualityService.insertUpdateData(cachedDataList,this.userId);
        log.info("存储数据库成功！");
    }
}
