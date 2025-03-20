package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esr;
import lombok.*;

/**
 * 用水户 基础信息,重复的取水权人 代表几个取水口返回实体
 *
 * @author BINX
 * @since 2024-01-03 14:04:21
 */
@Data
public class UserWaterBaseInfoEsr extends Esr {

    private static final long serialVersionUID = 825565020817856844L;

    private String id;

    private String userName;

    /**
     * 电子证号
     */
    private String eleNum;

    /**
     * 许可水量
     */
    private String promiseQuantity;

    /**
     * 1非居民 2居民生活 3农业生产者 4园林绿地 5地热井 6水源热泵
     */
    private String type;

    /**
     * 机井编码
     */
    private String code;

    /**
     * 街乡
     */
    private String jiedao;

    /**
     * 村
     */
    private String cun;

    /**
     * 机井编号
     */
    private String num;

    /**
     * 机井位置
     */
    private String location;

    /**
     * 井深
     */
    private String deep;

    /**
     * 经度
     */
    private String jd;

    /**
     * 纬度
     */
    private String wd;

    /**
     * 文件类型
     */
    private String fileType;
}
