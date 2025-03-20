package com.essence.service.utils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

@Component
public class BatchUtils {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public <T, R> void batchInsert(List<T> list, Class<R> rClass, BiConsumer<R, T> function, int batchSize) {
        if (null == list || list.isEmpty()) {
            return;
        }
        //如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        //不自动提交
        try {
            R r = session.getMapper(rClass);

            for (int i = 0; i < list.size(); i++) {
                function.accept(r, list.get(i));
                if ((i+1) % batchSize==0 || i == list.size() - 1) {
                    //手动每{batchSize}条提交一次，提交后无法回滚
                    session.commit();
                    //清理缓存，防止溢出
                    session.clearCache();

                }
            }
        } catch (Exception e) {
            //没有提交的数据可以回滚
            session.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    //replace 函数 需要串行化 不可用多线程 同时代码块需要加锁
    public <T, R> void multiBatchInsert(List<T> list, Class<R> rClass, BiConsumer<R, T> function, int batchSize) {
        if (null == list || list.isEmpty()) {
            return;
        }
        if (list.size() < (batchSize * CPU_COUNT)) {
            batchInsert(list, rClass, function, batchSize);
            return;
        }
        MapReduceUtil.handle(list, unit -> {
            batchInsert(unit, rClass, function, batchSize);
            return null;
        });

    }


}
