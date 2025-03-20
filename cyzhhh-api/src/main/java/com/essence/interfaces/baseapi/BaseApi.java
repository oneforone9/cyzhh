package com.essence.interfaces.baseapi;

import com.essence.interfaces.entity.*;

import java.io.Serializable;
import java.text.ParseException;

/**
 * @author zhy
 * @date 2021/12/9 16:54
 * @Description E 更新实体,P 查询条件实体,R 返回结果实体
 */
public interface BaseApi<E extends Esu, P extends Esp, R extends Esr> {
    // 新增
    int insert(E e) ;

    // 修改
    int update(E e);

    // 删除
    int deleteById(Serializable id);

    // 根据主键查询
    R findById(Serializable id);

    // 条件分页查询
    Paginator<R> findByPaginator(PaginatorParam param);


}
