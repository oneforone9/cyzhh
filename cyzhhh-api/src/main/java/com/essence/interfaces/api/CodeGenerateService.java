package com.essence.interfaces.api;

import java.util.Date;
import java.util.List;

/**
 * @author zhy
 * @since 2022/10/27 17:02
 */
public interface CodeGenerateService {

    /**
     * 获取顺序码
     * @param key key
     * @param num 个数
     * @return
     */
    List<String> getCode(String key, int num);


    /**
     * 根据时间删除数据
     */
    void clearCode(Date date);
}
