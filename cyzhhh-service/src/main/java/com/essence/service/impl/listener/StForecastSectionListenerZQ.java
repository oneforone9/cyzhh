package com.essence.service.impl.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.essence.common.utils.FieldsUtils;
import com.essence.common.utils.NumberUtils;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForecastSectionService;
import com.essence.interfaces.dot.ExcelException;
import com.essence.interfaces.model.ExcelData;
import com.essence.interfaces.model.ExcelErrorBean;
import com.essence.interfaces.model.StForecastSectionEsr;
import com.essence.interfaces.model.StForecastSectionImport;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author majunjie
 **/
public class StForecastSectionListenerZQ implements ReadListener<StForecastSectionImport> {
    //缓存的数据
    private List<ExcelData<StForecastSectionImport>> dataList = new ArrayList<>();
    // excel 错误信息
    private List<ExcelErrorBean> errorList = new ArrayList<>();
    // service
    private StForecastSectionService stForecastSectionService;
    // 方案d
    private String caseId;
    // 类型
    private String type;

    //构造器
    public StForecastSectionListenerZQ(StForecastSectionService stForecastSectionService, String caseId,String type) {
        this.stForecastSectionService = stForecastSectionService;
        this.caseId = caseId;
        this.type=type;
    }

    //这个每一条数据解析都会来调用
    @Override
    public void invoke(StForecastSectionImport data, AnalysisContext context) {
        // 所有数据加入到list中
        dataList.add(new ExcelData(data, context.readRowHolder().getRowIndex()));
    }

    //所有数据解析完成了 都会来调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 判断数据是否为空
        if (CollectionUtils.isEmpty(dataList)) {
            throw new ExcelException("excel 文件内容为空", null);
        }
        // 1 检查数据
        checkData();
        // 2 存在异常数据返回
        if (!CollectionUtils.isEmpty(errorList)) {
            throw new ExcelException("excel 文件内容错误", errorList);
        }
        // 3 数据入库
        saveData();
    }

    private void checkData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      List<ExcelErrorBean> emptyList = new ArrayList<>();
        //判断当前文档内数据是否有重复得
        List<StForecastSectionImport> lists = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            // 判断数据是否全空
            ExcelData<StForecastSectionImport> excelRow = dataList.get(i);

            StForecastSectionImport data = excelRow.getData();
            boolean isNull = FieldsUtils.checkObjAllFieldsIsNull(data);

            if (isNull) {
                emptyList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 0, "数据为空"));
                continue;
            }

            if (!isNull && !CollectionUtils.isEmpty(emptyList)) {
                errorList.addAll(emptyList);
                emptyList.clear();
            }

            if (!StringUtil.isNotBlank(data.getSectionName())) {
                errorList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 1, "站点名称未填写"));
            }
            if (!StringUtil.isNotBlank(data.getSttp())) {
                errorList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 2, "站点类型未填写"));
            }
            if (!StringUtil.isNotBlank(simpleDateFormat.format(data.getDate()))) {
                errorList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 3, "录入时间未填写"));
            }
            if (!NumberUtils.isNumber(data.getValue())) {
                errorList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 4, "站点数据值不是数字"));
            }
            if (!CollectionUtils.isEmpty(lists)) {
                List<StForecastSectionImport> collect = Optional.ofNullable(lists.stream().filter(x -> x.getSectionName().equals(data.getSectionName())&& x.getValue().equals(data.getValue())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(collect)) {
                    errorList.add(new ExcelErrorBean(excelRow.getRowIndex() + 1, 1, "站点名称对应得站点数据值在文档内部重复!"));
                }else {
                    lists.add(data);
                }
            }{
                lists.add(data);
            }

        }
    }

    private void saveData() {
        List<StForecastSectionEsr> stForecastSectionEsrList = Optional.ofNullable(dataList.stream()
                .map(p -> {
                    StForecastSectionImport data = p.getData();
                    StForecastSectionEsr stForecastSectionEsr = new StForecastSectionEsr();
                    stForecastSectionEsr.setSectionName(data.getSectionName());
                    stForecastSectionEsr.setSttp(data.getSttp());
                        stForecastSectionEsr.setDate(data.getDate());
                    stForecastSectionEsr.setValue(data.getValue());
                    return stForecastSectionEsr;
                }).collect(Collectors.toList())).orElse(Lists.newArrayList());
        stForecastSectionService.saveForecastSection(stForecastSectionEsrList, caseId,type);
    }
}
