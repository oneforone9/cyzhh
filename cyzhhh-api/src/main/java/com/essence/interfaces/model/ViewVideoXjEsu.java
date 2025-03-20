package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author majunjie
 * @since 2025-01-08 14:13:47
 */

@Data
public class ViewVideoXjEsu extends Esu {

    private static final long serialVersionUID = -94485164134491171L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 河系名称
     */
    private String riverName;

    /**
     * 巡检单位id
     */
    private String xjdwId;

    /**
     * 单位名称
     */
    private String dwmc;

    /**
     * 负责人
     */
    private String fzr;

    /**
     * 负责人id
     */
    private String fzrId;

    /**
     * 巡检内容
     */
    private String xjnr;

    /**
     * 巡检日期
     */
    private String xjRq;
    /**
     * 地址
     */
    private String address;
    /**
     * 计划主键
     */
    private String jhid;
    /**
     * 班组id
     */
    private String bzId;

    /**
     * 班组名称
     */
    private String bzmc;
    /**
     * 海康HIV,华为HUAWEI,内网NW,萤石云YSYC,YUSY宇视云,专线ZX
     */
    private String source;
}
