package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 河道信息表更新实体
 *
 * @author zhy
 * @since 2022-10-18 17:22:23
 */

@Data
public class ReaBaseEsu extends Esu {

    private static final long serialVersionUID = -55064091444950806L;


    /**
     * 主键
     *
     * @mock 1232123
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 河道名称
     *
     * @mock 河道1
     */
    @Size(max = 50, message = "河道名称的最大长度:50")
    private String reaName;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 管理单位
     *
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "管理单位的最大长度:20")
    private String unitName;

    /**
     * 长度(km)
     *
     * @mock 222.111
     */
    private Double reaLength;

    /**
     * 宽度(m)
     *
     * @mock 232.3
     */
    private Double reaWidth;

    /**
     * 行政区名称
     *
     * @mock 刘各庄
     */
    @Size(max = 30, message = "行政区名称的最大长度:30")
    private String adName;

    /**
     * 水面面积(m2)(分岸别，左右岸共用数据只填右岸)
     *
     * @mock 11.22
     */
    private Double waterSpace;

    /**
     * 绿化面积(m2)(分岸别，左右岸共用数据只填右岸)
     *
     * @mock 11.22
     */
    private Double greenSpace;

    /**
     * 保洁面积(m2)(分岸别，左右岸共用数据只填右岸)
     *
     * @mock 11.22
     */
    private Double cleanSpace;

    /**
     * 管理面积(m2)(分岸别，左右岸共用数据只填右岸)
     *
     * @mock 11.22
     */
    private Double manageSpace;

    /**
     * 起点位置
     *
     * @mock 起点位置
     */
    @Size(max = 100, message = "起点位置的最大长度:100")
    private String startPosition;

    /**
     * 终点位置
     *
     * @mock 终点位置
     */
    @Size(max = 100, message = "终点位置的最大长度:100")
    private String endPosition;

    /**
     * 岸别(0不分,1左岸,2右岸)
     *
     * @mock 0
     */
    @Size(max = 1, message = "岸别的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "岸别不能为空")
    private String shore;

    /**
     * 河道类型（1河 2沟 3渠）
     * @mock 1
     */
    @Size(max = 1, message = "河道类型的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "河道类型不能为空")
    private String reaType;

    /**
     * 上级河道主键(一级河道传0)
     * @mock 0
     */
    @NotEmpty(groups = Insert.class, message = "上级河道主键不能为空")
    private String upId;

    /**
     * 河道级别（1区-一级河道 2乡镇-二级河道 3村-三级河道）
     * @mock 1
     */
    @Size(max = 1, message = "河道级别的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "河道级别不能为空")
    private String reaLevel;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注的最大长度:1000")
    private String remark;

    /**
     * 河流等级
     */
    private Integer rank;

    /**
     * 作用
     */
    private String function;

    /**
     * 起点
     */
    private String begin;

    /**
     * 止点
     */
    private String end;


}
