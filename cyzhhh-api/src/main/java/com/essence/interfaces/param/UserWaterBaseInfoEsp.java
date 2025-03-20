package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Esp;
import lombok.*;

import java.util.List;

/**
 * 用水户 基础信息,重复的取水权人 代表几个取水口参数实体
 *
 * @author BINX
 * @since 2024-01-03 14:04:20
 */
@Data
public class UserWaterBaseInfoEsp extends Esp {

    private static final long serialVersionUID = -45972574546570119L;

    /**
     * 页码 默认 1
     * @mock 1
     */
    private int currentPage = 1;

    /**
     * 个数 默认 10
     * @mock 10
     */
    private int pageSize = 10;

    /**
     * 查询条件
     */
    private List<Criterion> conditions;
    
    /**
     * 排序条件
     */
    private List<Criterion> orders;

    /**
     * 共通查询条件
     * 加载or关键字前，建议后端追加参数使用此集合，不暴露给前端
     * @ignore
     */
    private List<Criterion> currency;
    
        @ColumnMean("id")
    private String id;

    /**
     * 批量操作
     */
    private List<String> ids;
    @ColumnMean("user_name")
    private String userName;

    /**
     * 电子证号
     */
    @ColumnMean("ele_num")
    private String eleNum;

    /**
     * 许可水量
     */
    @ColumnMean("promise_quantity")
    private String promiseQuantity;


    /**
     * 1非居民 2居民生活 3农业生产者 4园林绿地 5地热井 6水源热泵
     */
    @ColumnMean("type")
    private String type;

    /**
     * 机井编码
     */
    @ColumnMean("code")
    private String code;

    /**
     * 街乡
     */
    @ColumnMean("jiedao")
    private String jiedao;

    /**
     * 村
     */
    @ColumnMean("cun")
    private String cun;

    /**
     * 机井编号
     */
    @ColumnMean("num")
    private String num;

    /**
     * 机井位置
     */
    @ColumnMean("location")
    private String location;

    /**
     * 井深
     */
    @ColumnMean("deep")
    private String deep;

    /**
     * 经度
     */
    @ColumnMean("jd")
    private String jd;

    /**
     * 纬度
     */
    @ColumnMean("wd")
    private String wd;

    /**
     * 文件类型
     */
    @ColumnMean("file_type")
    private String fileType;

}
