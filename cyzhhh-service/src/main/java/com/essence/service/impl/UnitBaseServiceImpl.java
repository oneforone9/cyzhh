package com.essence.service.impl;

import com.essence.common.constant.ItemConstant;
import com.essence.dao.DepartmentBaseDao;
import com.essence.dao.UnitBaseDao;
import com.essence.dao.entity.UnitBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.euauth.entity.PubCorp;
import com.essence.euauth.feign.CorpFeign;
import com.essence.interfaces.api.DepartmentBaseService;
import com.essence.interfaces.api.UnitBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.UnitBaseEsp;
import com.essence.service.baseconverter.PaginatorConverter;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterUnitBaseEtoT;
import com.essence.service.converter.ConverterUnitBaseRtoREX;
import com.essence.service.converter.ConverterUnitBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class UnitBaseServiceImpl extends BaseApiImpl<UnitBaseEsu, UnitBaseEsp, UnitBaseEsr, UnitBase> implements UnitBaseService {

    @Autowired
    private UnitBaseDao unitBaseDao;
    @Autowired
    private ConverterUnitBaseEtoT converterUnitBaseEtoT;
    @Autowired
    private ConverterUnitBaseTtoR converterUnitBaseTtoR;
    @Autowired
    private ConverterUnitBaseRtoREX converterUnitBaseRtoREX;

    @Autowired
    private CorpFeign corpFeign;

    @Autowired
    private DepartmentBaseService departmentBaseService;
    @Autowired
    private DepartmentBaseDao departmentBaseDao;

    public UnitBaseServiceImpl(UnitBaseDao unitBaseDao, ConverterUnitBaseEtoT converterUnitBaseEtoT, ConverterUnitBaseTtoR converterUnitBaseTtoR) {
        super(unitBaseDao, converterUnitBaseEtoT, converterUnitBaseTtoR);
    }

    @Override
    @Transactional
    public int insert(UnitBaseEsu unitBaseEsu) {
        String id = UuidUtil.get32UUIDStr();
        unitBaseEsu.setId(id);
        int insert = super.insert(unitBaseEsu);
        if (0 == insert) {
            return 0;
        }
        // 同步到euauth
        PubCorp pubCorp = new PubCorp();
        pubCorp.setCorpId(id);
        pubCorp.setCorpName(unitBaseEsu.getUnitName());
        pubCorp.setPid(ItemConstant.CORP_ROOT_ID);
        pubCorp.setOrderNum(ItemConstant.CORP_ORDER_DEFAULT);
        pubCorp.setLevel(ItemConstant.CORP_LEVEL_DEFAULT);
        corpFeign.addSubPubCorp(pubCorp);
        return insert;
    }

    @Override
    @Transactional
    public int update(UnitBaseEsu unitBaseEsu) {
        int update = super.update(unitBaseEsu);
        if (0 == update) {
            return 0;
        }
        // 同步到euauth
        PubCorp pubCorp = new PubCorp();
        pubCorp.setCorpId(unitBaseEsu.getId());
        pubCorp.setCorpName(unitBaseEsu.getUnitName());
        corpFeign.editPubCorp(pubCorp);
        return update;
    }

    @Override
    public int deleteById(Serializable id) {
        // todo 判断是否存在组织关系
        int delete = super.deleteById(id);
        if (0 == delete) {
            return 0;
        }
        // 同步到euauth
        corpFeign.deletePubCorp((String) id);
        return delete;
    }

    @Override
    public Paginator<UnitBaseEsrEx> searchDepart(PaginatorParam param) {
        // 1 根据条件查询
        Paginator<UnitBaseEsr> paginator = this.findByPaginator(param);
        List<UnitBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return PaginatorConverter.pageToPaginator(paginator, null);
        }
        // 2 更换对象
        List<UnitBaseEsrEx> unitBaseEsrExes = converterUnitBaseRtoREX.toList(items);
        // 3 获取所有班组
        List<DepartmentBaseEsr> departmentBaseEsrs = departmentBaseService.findAll();
        if (CollectionUtils.isEmpty(departmentBaseEsrs)) {
            return PaginatorConverter.pageToPaginator(paginator, unitBaseEsrExes);
        }
        Map<String, List<DepartmentBaseEsr>> departmentBaseMap = departmentBaseEsrs.stream().filter(p -> !StringUtils.isEmpty(p.getUnitId())).collect(Collectors.groupingBy(DepartmentBaseEsr::getUnitId, Collectors.toList()));
        // 4 组装班组
        unitBaseEsrExes.forEach(p->{
            p.setDepartList(departmentBaseMap.get(p.getId()));
        });
        return PaginatorConverter.pageToPaginator(paginator, unitBaseEsrExes);
    }

    /**
     * 设置巡河组长
     * @param departmentBaseEsu
     * @return
     */
    @Override
    public Integer setDepart(DepartmentBaseEsu departmentBaseEsu) {
        return departmentBaseService.setDepart(departmentBaseEsu);
    }

    /**
     * 判断是不是巡河组长
     * @param personBaseId
     * @return
     */
    @Override
    public Object selectDepart(String personBaseId) {
        return departmentBaseDao.selectDepart(personBaseId);
    }
}
