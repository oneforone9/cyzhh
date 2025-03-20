package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.FileBaseDao;
import com.essence.dao.StFloodReportDao;

import com.essence.dao.entity.FileBase;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.dao.entity.StFloodReportDto;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.api.StFloodReportService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.FileBaseEsu;
import com.essence.interfaces.model.StFloodReportEsr;
import com.essence.interfaces.model.StFloodReportEsu;
import com.essence.interfaces.param.StFloodReportEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStFloodReportEtoT;
import com.essence.service.converter.ConverterStFloodReportTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 汛情上报表(StFloodReport)业务层
 * @author liwy
 * @since 2023-03-13 14:25:27
 */
@Service
public class StFloodReportServiceImpl extends BaseApiImpl<StFloodReportEsu, StFloodReportEsp, StFloodReportEsr, StFloodReportDto> implements StFloodReportService {

    @Autowired
    private StFloodReportDao stFloodReportDao;
    @Autowired
    private ConverterStFloodReportEtoT converterStFloodReportEtoT;
    @Autowired
    private ConverterStFloodReportTtoR converterStFloodReportTtoR;
    @Autowired
    private StFloodReportService stFloodReportService;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private FileBaseDao fileBaseDao;

    public StFloodReportServiceImpl(StFloodReportDao stFloodReportDao, ConverterStFloodReportEtoT converterStFloodReportEtoT, ConverterStFloodReportTtoR converterStFloodReportTtoR) {
        super(stFloodReportDao, converterStFloodReportEtoT, converterStFloodReportTtoR);
    }

    /**
     * 添加汛情
     * @param stFloodReportEsu
     * @return
     */
    @Transactional
    @Override
    public Object addStFloodReport(StFloodReportEsu stFloodReportEsu) {
        String baseId = UUID.randomUUID().toString().replace("-","");
        stFloodReportEsu.setIsDelete(ItemConstant.REPORT_NO_DELETE);
        stFloodReportEsu.setId(baseId);
        int insert = stFloodReportService.insert(stFloodReportEsu);

        //将上传的图片进行关联
        List<String> fileIds = stFloodReportEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return insert;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.REPORT_FILE_PREFIX + stFloodReportEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });
        return insert;
    }

    /**
     * 汛情详情
     * @param id
     * @return
     */
    @Override
    public Object searchById(String id) {
        StFloodReportEsr stFloodReportEsr = super.findById(id);
        //关联获取到相关联的图片附件
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(Arrays.asList(ItemConstant.REPORT_FILE_PREFIX + stFloodReportEsr.getId()));
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
        List<FileBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return stFloodReportEsr;
        }

        Map<String, List<FileBaseEsr>> fileMap = items.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        stFloodReportEsr.setReportFileList(fileMap.get(ItemConstant.REPORT_FILE_PREFIX + stFloodReportEsr.getId()));
        return stFloodReportEsr;
    }
}
