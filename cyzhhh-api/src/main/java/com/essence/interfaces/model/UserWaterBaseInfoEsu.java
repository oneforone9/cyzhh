package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esu;
import lombok.*;


/**
 * 用水户 基础信息,重复的取水权人 代表几个取水口更新实体
 *
 * @author BINX
 * @since 2024-01-03 14:04:20
 */
@Data
public class UserWaterBaseInfoEsu extends Esu {

    @ExcelProperty(value = "序号",index = 0)
    private String id;

    @ExcelProperty(value = "机井编码",index = 1)
    private String code;

    @ExcelProperty(value = "街乡",index = 2)
    private String jiedao;

    @ExcelProperty(value = "村",index = 3)
    private String cun;

    @ExcelProperty(value = "取水权人",index = 4)
    private String userName;

    @ExcelProperty(value = "电子证号",index = 5)
    private String eleNum;

    @ExcelProperty(value = "许可水量",index = 6)
    private String promiseQuantity;

    @ExcelProperty(value = "机井编号",index = 7)
    private String num;

    @ExcelProperty(value = "机井位置",index = 8)
    private String location;

    @ExcelProperty(value = "井深",index = 9)
    private String deep;

    @ExcelProperty(value = "经度",index = 10)
    private String jd;

    @ExcelProperty(value = "纬度",index = 11)
    private String wd;

    /**
     * 1非居民 2居民生活 3农业生产者 4园林绿地 5地热井 6水源热泵
     */
    @ExcelProperty(value = "机井类别",index = 12)
    private String type;

    @ExcelIgnore
    private String fileType;

}
