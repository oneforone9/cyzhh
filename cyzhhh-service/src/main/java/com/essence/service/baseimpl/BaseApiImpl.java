package com.essence.service.baseimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.*;
import com.essence.service.baseconverter.BaseConverter;
import com.essence.service.baseconverter.PaginatorConverter;
import com.essence.service.exception.PaginatorException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zhy
 * @date 2021/12/9 16:58
 * @Description
 */

public abstract class BaseApiImpl<E extends Esu, P extends Esp, R extends Esr, T extends Model<T>> implements BaseApi<E, P, R> {


    private BaseDao<T> baseDao;

    private BaseConverter<E, T> baseConverter;

    private BaseConverter<T, R> resultConverter;

    public BaseApiImpl(BaseDao<T> baseDao, BaseConverter<E, T> baseConverter, BaseConverter<T, R> resultConverter) {
        this.baseDao = baseDao;
        this.baseConverter = baseConverter;
        this.resultConverter = resultConverter;
    }

    /**
     * 新增
     *
     * @param e
     * @return
     */
    @Override
    public int insert(E e) {
        T t = baseConverter.toBean(e);
        return baseDao.insert(t);
    }

    /**
     * 更新
     *
     * @param e
     * @return
     */
    @Override
    public int update(E e) {
        T t = baseConverter.toBean(e);
        return baseDao.updateById(t);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Serializable id) {
        return baseDao.deleteById(id);
    }


    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @Override
    public R findById(Serializable id) {
        T t = baseDao.selectById(id);
        return resultConverter.toBean(t);
    }

    @Override
    public Paginator<R> findByPaginator(PaginatorParam param) {
        // 获取param的class
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actType = parameterizedType.getActualTypeArguments()[1];
        // 拼装查询条件
        QueryWrapper<T> wrapper = queryWrapper(actType, param);
        // 创建分页对象
        Page<T> tPage = new Page<>();
        // 设置分页 page size都为0时查所有
        if (0 == param.getCurrentPage() && 0 == param.getPageSize()) {
            // 查询
            List<T> ts = baseDao.selectList(wrapper);
            // 按分页返回
            tPage.setPages(0);
            tPage.setSize(0);
            tPage.setTotal(null == ts ? 0 : ts.size());
            return PaginatorConverter.IPageToPaginator(tPage, resultConverter.toList(ts));

        }
        // 设置页码
        tPage.setCurrent(param.getCurrentPage());
        tPage.setSize(param.getPageSize());
        // 查询
        IPage<T> iPage = baseDao.selectPage(tPage, wrapper);
        // 封装返回值
        Paginator<R> paginator = PaginatorConverter.IPageToPaginator(iPage, resultConverter.toList(iPage.getRecords()));
        return paginator;
    }

    /**
     * @param actType param对应的泛型
     * @param param   查询条件
     * @return
     */
    public QueryWrapper<T> queryWrapper(Type actType, PaginatorParam param) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        // 条件查询
        List<Criterion> conditions = param.getConditions();
        if (!CollectionUtils.isEmpty(conditions)) {
            for (Criterion condition : conditions) {
                // and 不需要拼接 默认就是and
                if (Criterion.AND.equals(condition.getOperator())) {
                    continue;
                }
                // 拼接or
                if (Criterion.OR.equals(condition.getOperator())) {
                    if (!CollectionUtils.isEmpty(param.getCurrency())) {
                        for (Criterion criterion : param.getCurrency()) {
                            try {
                                // 组装共用查询条件
                                assembleQuery(wrapper, actType, criterion.getFieldName(), criterion.getOperator(), criterion.getValue(), "查询条件错误");
                            } catch (NoSuchFieldException | ClassNotFoundException e) {
                                e.printStackTrace();
                                throw new PaginatorException("查询条件错误");
                            }
                        }
                    }
                    wrapper.or();
                    continue;
                }
                // 条件属性 三缺一抛异常  fix:将传空数的条件跳过取消报错
                if (StringUtils.isEmpty(condition.getFieldName()) ||  StringUtils.isEmpty(condition.getOperator())) {
//                    throw new PaginatorException("查询条件错误");
                }
                if (ObjectUtils.isEmpty(condition.getValue()) ){
                    continue;
                }

                try {
                    // 组装查询条件
                    assembleQuery(wrapper, actType, condition.getFieldName(), condition.getOperator(), condition.getValue(), "查询条件错误");
                    // 共同查询条件
                    if (!CollectionUtils.isEmpty(param.getCurrency())) {
                        for (Criterion criterion : param.getCurrency()) {
                            // 组装共用查询条件
                            assembleQuery(wrapper, actType, criterion.getFieldName(), criterion.getOperator(), criterion.getValue(), "查询条件错误");
                        }
                    }
                } catch (NoSuchFieldException | ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new PaginatorException("查询条件错误");
                }
            }
        } else {
            // 共同查询条件
            if (!CollectionUtils.isEmpty(param.getCurrency())) {
                for (Criterion criterion : param.getCurrency()) {
                    // 组装共用查询条件
                    try {
                        assembleQuery(wrapper, actType, criterion.getFieldName(), criterion.getOperator(), criterion.getValue(), "查询条件错误");
                    } catch (NoSuchFieldException | ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new PaginatorException("查询条件错误");
                    }
                }
            }
        }

        // 排序
        List<Criterion> orders = param.getOrders();
        if (CollectionUtils.isEmpty(orders)) {
            return wrapper;
        }
        for (Criterion condition : orders) {
            // 排序属性 二缺一跳过
            if (StringUtils.isEmpty(condition.getFieldName()) || StringUtils.isEmpty(condition.getOperator())) {
                throw new PaginatorException("排序条件错误");
            }
            // 根据属性名获取表字段
            try {
                // 获取属性
                Class<?> aClass = Class.forName(actType.getTypeName());
                Field[] declaredFields = aClass.getDeclaredFields();
                Field field = null;
                try {
                    field = Class.forName(actType.getTypeName()).getDeclaredField(condition.getFieldName());
                } catch (NoSuchFieldException e) {
                    field = aClass.getSuperclass().getDeclaredField(condition.getFieldName());
                } catch (SecurityException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (null == field) {
                    throw new PaginatorException("排序条件错误");
                }
                field.setAccessible(true);

                // 获取注解
                ColumnMean columnMean = field.getAnnotation(ColumnMean.class);
                // 为空跳过
                if (null == columnMean) {
                    continue;
                }
                // 获取表字段
                String column = columnMean.value();

                // 根据操作确定调用的方法
                switch (condition.getOperator()) {
                    case Criterion.ASC:
                        wrapper.orderByAsc(column);
                        break;
                    case Criterion.DESC:
                        wrapper.orderByDesc(column);
                        break;
                }
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new PaginatorException("排序条件错误");
            }

        }

        return wrapper;

    }


    /**
     * 组装查询条件
     *
     * @param wrapper      查询包装对象
     * @param actType      泛型类
     * @param fieldName    属性字段
     * @param operator     操作
     * @param value        值
     * @param exceptionMsg 提示异常信息
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     */
    private void assembleQuery(QueryWrapper<T> wrapper, Type actType, String fieldName, String operator, Object value, String exceptionMsg) throws ClassNotFoundException, NoSuchFieldException {
        // 获取属性
        Class<?> aClass = Class.forName(actType.getTypeName());
        Field[] declaredFields = aClass.getDeclaredFields();
        Field field = null;
        try {
            field = Class.forName(actType.getTypeName()).getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = aClass.getSuperclass().getDeclaredField(fieldName);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (null == field) {
            throw new PaginatorException(exceptionMsg);
        }
        field.setAccessible(true);
        // 获取注解
        ColumnMean columnMean = field.getAnnotation(ColumnMean.class);
        // 为空跳过
        if (null == columnMean) {
            throw new PaginatorException(exceptionMsg);
        }
        // 获取表字段
        String column = columnMean.value();
        // 封装查询条件
        stitchQueries(wrapper, column, operator, value);
    }

    /**
     * 封装查询条件
     *
     * @param wrapper  查询包装对象
     * @param column   表字段名称
     * @param operator 操作
     * @param value    值
     */
    private void stitchQueries(QueryWrapper<T> wrapper, String column, String operator, Object value) {
        // 根据操作确定调用的方法
        switch (operator) {
            case Criterion.EQ:
                wrapper.eq(column, value);
                break;
            case Criterion.NE:
                wrapper.ne(column, value);
                break;
            case Criterion.NOTIN:
                wrapper.notIn(column, value);
                break;
            case Criterion.LIKE:
                wrapper.like(column, value);
                break;
            case Criterion.GT:
                wrapper.gt(column, value);
                break;
            case Criterion.LT:
                wrapper.lt(column, value);
                break;
            case Criterion.GTE:
                wrapper.ge(column, value);
                break;
            case Criterion.LTE:
                wrapper.le(column, value);
                break;
            case Criterion.IS_NULL:
                wrapper.isNull(column);
                break;
            case Criterion.IS_NOT_NULL:
                wrapper.isNotNull(column);
                break;
            case Criterion.IN:
                wrapper.in(column, JSONArray.parseArray(JSON.toJSONString(value)));
                break;
            default:
                throw new PaginatorException("查询条件错误");
        }
    }

}
