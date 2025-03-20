package com.essence.service.baseconverter;

import java.util.List;

/**
 * @author zhy
 * @date 2021/12/9 17:33
 * @Description
 */
public interface BaseConverter<D,T> {
    /**
     * D 转 T
     * @param d
     * @return
     */
    T toBean(D d);

    /**
     * List<D> 转 List<T>
     * @param list
     * @return
     */
    List<T> toList(List<D> list);

}
