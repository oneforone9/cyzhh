package com.essence.dao.basedao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @author zhy
 * @date 2021/12/9 17:03
 * @Description
 */
@Component
public interface BaseDao<T> extends BaseMapper<T> {
}
