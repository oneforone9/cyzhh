package com.essence.service.listener;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.essence.dao.UserWaterBaseInfoDao;
import com.essence.dao.entity.UserWaterBaseInfoDto;
import com.essence.interfaces.model.UserWaterBaseInfoEsr;
import com.essence.interfaces.model.UserWaterBaseInfoEsu;
import com.google.common.collect.Lists;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class UserWaterBaeInfoListener implements ReadListener<UserWaterBaseInfoEsu> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<UserWaterBaseInfoEsu> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    UserWaterBaseInfoDao userWaterBaseInfoDao;
    String fileType;



    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param userWaterBaseInfoDao
     */
    public UserWaterBaeInfoListener(UserWaterBaseInfoDao userWaterBaseInfoDao,String fileType) {
        this.userWaterBaseInfoDao = userWaterBaseInfoDao;
        this.fileType = fileType;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(UserWaterBaseInfoEsu data, AnalysisContext context) {
        data.setFileType(fileType);
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (cachedDataList.size() >= BATCH_COUNT) {
//            saveData();
            // 存储完成清理 list
//            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//        }
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
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<List<UserWaterBaseInfoEsu>> partition = Lists.partition(cachedDataList, 50);

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (List<UserWaterBaseInfoEsu> userWaterBaseInfoEsus : partition) {
            completableFutures.add(CompletableFuture.runAsync(() -> {
                for (UserWaterBaseInfoEsu userWaterBaseInfoEsu : userWaterBaseInfoEsus) {
                    UserWaterBaseInfoDto userWaterBaseInfoDto = new UserWaterBaseInfoDto();
                    BeanUtil.copyProperties(userWaterBaseInfoEsu, userWaterBaseInfoDto);
                    userWaterBaseInfoDto.setId(IdWorker.get32UUID());
                    userWaterBaseInfoDao.insert(userWaterBaseInfoDto);
                }
            }, executorService));
        }
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
            voidCompletableFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
