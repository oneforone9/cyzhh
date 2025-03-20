package com.essence.interfaces.model;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public enum UserWaterEnum {
    // 1非居民 2居民生活 3农业生产者 4园林绿地 5地热井 6水源热泵
    FEIJUMIN("非居民",1),
    JUMINSHENGHUO("居民生活",2),
    NONGYESHENGCHANZHE("农业生产者",3),
    YUANLINLVDI("园林绿地",4),
    DIREJING("地热井",5),
    SHUIYUANREBENG("水源热泵",6);

    public String NAME;
    public Integer CODE;

    UserWaterEnum(String NAME, Integer CODE) {
        this.NAME = NAME;
        this.CODE = CODE;
    }

    public static class ConvertExcel implements Converter<Integer> {
        @Override
        public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            if(value.equals(FEIJUMIN.CODE)){
                return new WriteCellData<>(FEIJUMIN.NAME);
            }else if(value.equals(JUMINSHENGHUO.CODE)){
                return new WriteCellData<>(JUMINSHENGHUO.NAME);
            }else if(value.equals(NONGYESHENGCHANZHE.CODE)){
                return new WriteCellData<>(NONGYESHENGCHANZHE.NAME);
            }else if(value.equals(YUANLINLVDI.CODE)){
                return new WriteCellData<>(YUANLINLVDI.NAME);
            }else if(value.equals(DIREJING.CODE)){
                return new WriteCellData<>(DIREJING.NAME);
            }else if(value.equals(SHUIYUANREBENG.CODE)){
                return new WriteCellData<>(SHUIYUANREBENG.NAME);
            }else{
                return new WriteCellData<>("其他类型");
            }
        }
    }
}
