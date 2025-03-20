package com.essence.service.impl;

import com.essence.common.constant.ItemConstant;
import com.essence.dao.SysDictionaryTypeDao;
import com.essence.dao.entity.SysDictionaryType;
import com.essence.interfaces.api.SysDictionaryTypeService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.SysDictionaryTypeEsr;
import com.essence.interfaces.model.SysDictionaryTypeEsu;
import com.essence.interfaces.param.SysDictionaryTypeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterSysDictionaryTypeEtoT;
import com.essence.service.converter.ConverterSysDictionaryTypeTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class SysDictionaryTypeServiceImpl extends BaseApiImpl<SysDictionaryTypeEsu, SysDictionaryTypeEsp, SysDictionaryTypeEsr, SysDictionaryType> implements SysDictionaryTypeService {

    @Autowired
    private SysDictionaryTypeDao sysDictionaryTypeDao;
    @Autowired
    private ConverterSysDictionaryTypeEtoT converterSysDictionaryTypeEtoT;
    @Autowired
    private ConverterSysDictionaryTypeTtoR converterSysDictionaryTypeTtoR;

    public SysDictionaryTypeServiceImpl(SysDictionaryTypeDao sysDictionaryTypeDao, ConverterSysDictionaryTypeEtoT converterSysDictionaryTypeEtoT, ConverterSysDictionaryTypeTtoR converterSysDictionaryTypeTtoR) {
        super(sysDictionaryTypeDao, converterSysDictionaryTypeEtoT, converterSysDictionaryTypeTtoR);
    }

    @Override
    public Paginator<SysDictionaryTypeEsr> findByPaginator(PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
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
}
