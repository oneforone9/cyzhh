package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 防汛调度-方案基础表更新实体
 *
 * @author zhy
 * @since 2023-04-17 16:29:55
 */

@Data
public class StCaseBaseInfoEsu extends Esu {

    private static final long serialVersionUID = -32525955590968092L;

    /**
     * 主键
     */
    private String id;

    /**
     * 方案名称
     */
    @NotEmpty(groups = Insert.class, message = "方案名称不能为空")
    private String caseName;

    /**
     * 预报开始时间 (中间时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class, message = "预测时间不能为空")
    private Date forecastStartTime;

    /**
     * 前置时间-预热期往前推两小时
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class, message = "预热时间不能为空")
    private Date preFixTime;

    /**
     * 预热时间（期）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class, message = "预热时间不能为空")
    private Date preHotTime;

    /**
     * 预见期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class, message = "预见期不能为空")
    private Date preSeeTime;

    /**
     * 步长
     */
    @NotNull(groups = Insert.class, message = "步长不能为空")
    private Integer step;

    /**
     * 雨型
     */
    private String rainType;

    /**
     * 雨型名称
     */
    private String rainTypeName;

    /**
     * 降雨总量
     */
    private String rainTotal;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */

    private String status;

    /**
     * 雨形 id
     */
    private String rainId;

    /**
     * 河流 riverId
     */
    private String riverId;

    /**
     * 断面名称
     */
    private String sectionName;
    /**
     * 模型存放路径
     */
    private String modelPath;
    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    private Integer modelType;
    /**
     * 预报方式 1 气象预测 2 设计雨型
     */
    private String forecastType;
}
