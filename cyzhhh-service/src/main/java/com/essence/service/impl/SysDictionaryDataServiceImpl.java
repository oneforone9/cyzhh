package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.SysDictionaryDataDao;
import com.essence.dao.entity.SysDictionaryData;
import com.essence.interfaces.api.SysDictionaryDataService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.SysDictionaryDataEsr;
import com.essence.interfaces.model.SysDictionaryDataEsrEX;
import com.essence.interfaces.model.SysDictionaryDataEsu;
import com.essence.interfaces.param.SysDictionaryDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterSysDictionaryDataEtoT;
import com.essence.service.converter.ConverterSysDictionaryDataTtoEX;
import com.essence.service.converter.ConverterSysDictionaryDataTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class SysDictionaryDataServiceImpl extends BaseApiImpl<SysDictionaryDataEsu, SysDictionaryDataEsp, SysDictionaryDataEsr, SysDictionaryData> implements SysDictionaryDataService {

    @Autowired
    private SysDictionaryDataDao sysDictionaryDataDao;
    @Autowired
    private ConverterSysDictionaryDataEtoT converterSysDictionaryDataEtoT;
    @Autowired
    private ConverterSysDictionaryDataTtoR converterSysDictionaryDataTtoR;
    @Autowired
    private ConverterSysDictionaryDataTtoEX converterSysDictionaryDataTtoEX;

    public SysDictionaryDataServiceImpl(SysDictionaryDataDao sysDictionaryDataDao, ConverterSysDictionaryDataEtoT converterSysDictionaryDataEtoT, ConverterSysDictionaryDataTtoR converterSysDictionaryDataTtoR) {
        super(sysDictionaryDataDao, converterSysDictionaryDataEtoT, converterSysDictionaryDataTtoR);
    }


    @Override
    public Paginator<SysDictionaryDataEsr> findByPaginator(PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)) {
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.DICTTYPE_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);

        return super.findByPaginator(param);
    }

    @Override
    public List<SysDictionaryDataEsrEX> findTreeByType(String type) {
        QueryWrapper<SysDictionaryData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dictionary_type", type);
        queryWrapper.eq("is_deleted", ItemConstant.DICTDATA_NO_DELETE);
        queryWrapper.orderByAsc("sort");
        List<SysDictionaryDataEsrEX> sysDictionaryDataEsrEXList = converterSysDictionaryDataTtoEX.toList(sysDictionaryDataDao.selectList(queryWrapper));
        if (CollectionUtils.isEmpty(sysDictionaryDataEsrEXList)) {
            return null;
        }
        SysDictionaryDataEsrEX root = new SysDictionaryDataEsrEX();
        Map<String, SysDictionaryDataEsrEX> DataMap = sysDictionaryDataEsrEXList.stream().collect(Collectors.toMap(SysDictionaryDataEsrEX::getId, Function.identity(), (key1, key2) -> key1));
        sysDictionaryDataEsrEXList.forEach(p->{
            // 父id不存在放入root
            if (null == p.getParentId() || "0".equals(p.getParentId()) ){
                root.getChildren().add(p);
                return;
            }
            // 父id存在,放入父级
            SysDictionaryDataEsrEX sysDictionaryDataEsrEX = DataMap.get(p.getParentId());
            if (null != sysDictionaryDataEsrEX){
                sysDictionaryDataEsrEX.getChildren().add(p);
            }
        });

        return root.getChildren();
    }
}
