package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author bird
 * @since 2023-01-04 18:01:16
 */

@Data
public class StWellcollectFeeEsr extends Esr {

    private static final long serialVersionUID = 669893109845632708L;

    private Integer id;


    /**
     * 乡政府名称
     */
    private String townName;


    /**
     * 类型 1-乡 2-街道
     */
    private String type;


    /**
     * 乡管机井_实有
     */
    private String xzjjSy;


    /**
     * 乡管机井_装表计量
     */
    private String xzjjZbjl;


    /**
     * 乡管机井_实际发生
     */
    private Double xzjjSjfs;


    /**
     * 乡管机井_实缴
     */
    private Double xzjjSj;


    /**
     * 社会机井_实有
     */
    private String shdwjjSy;


    /**
     * 社会机井_装表计量
     */
    private String shdwjjZbjl;


    /**
     * 社会机井_实际发生
     */
    private Double shdwjjSjfs;


    /**
     * 社会机井_实缴
     */
    private Double shdwjjSj;


    /**
     * 统计年份
     */
    private String tjTime;
    /**
     * 季度
     */

    private String tjJd;


}
