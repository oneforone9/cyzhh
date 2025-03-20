package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝计划排班信息表更新实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:11
 */

@Data
public class StPlanInfoEsuParam extends Esu {

    private static final long serialVersionUID = -17571540500092428L;

    /**
     * 页码
     *
     * @mock 1
     */
    private int currentPage;
    /**
     * 个数
     *
     * @mock 10
     */
    private int pageSize;

    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    private String sttp;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;

    /**
     * 河道名称
     */
    private String riverName;

    /**
     * 负责人
     */
    private String name;


}
