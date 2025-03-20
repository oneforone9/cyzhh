package com.essence.service.impl.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.essence.interfaces.model.HtglExcept;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HtListener extends AnalysisEventListener<HtglExcept> {

    private List<HtglExcept> records = new ArrayList<>();

    @Override
    public void invoke(HtglExcept data, AnalysisContext context) {
        records.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 可以在这里处理读取完成后的逻辑
    }

    public List<HtglExcept> getRecords() {
        return records;
    }
}
