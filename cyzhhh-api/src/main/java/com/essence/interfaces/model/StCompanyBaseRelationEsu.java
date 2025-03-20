package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-02-16 12:01:44
 */

@Data
public class StCompanyBaseRelationEsu extends Esu {

    private static final long serialVersionUID = -33900344470352941L;

    private Integer id;

    /**
     * 第三方服务公司主键id
     */
    private String  stCompanyBaseId;

    /**
     * 数据类型（1-服务类型  2-管护河道）
     */
    private String type;

    /**
     * 服务类型或管护河段id
     */
    private String dataId;

    /**
     * 备注
     */
    private String remark;
    /**
     * 服务类型或管护河段名称
     */
    private String dataName;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 创建者
     */
    private String creator;


}
