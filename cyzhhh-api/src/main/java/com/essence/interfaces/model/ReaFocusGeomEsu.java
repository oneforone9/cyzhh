package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 河道重点位置地理信息表更新实体
 *
 * @author zhy
 * @since 2022-10-26 14:06:35
 */

@Data
public class ReaFocusGeomEsu extends Esu {

    private static final long serialVersionUID = 346525835496125294L;


    /**
     * 主键
     * @mock 123
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 重点巡查位置
     * @mock 全段
     */
    @Size(max = 100, message = "重点巡查位置的最大长度:100")
    @NotEmpty(groups = Insert.class, message = "重点巡查位置不能为空")
    private String focusPosition;

    /**
     * 河道主键
     * @mock 1
     */
    @NotEmpty(groups = Insert.class, message = "主键不能为空")
    private String reaId;
    /**
     * 河道名称
     */
    private String reaName;

    /**
     * 空间数据
     */
    @NotEmpty(groups = Insert.class, message = "主键不能为空")
    private String geom;

    /**
     * 中心点-经度
     * @mock 112.23
     */
    private Double lgtd;

    /**
     * 中心点-纬度
     * @mock 23.123
     */
    private Double lttd;
    /**
     * 备注
     * @mock 备注
     */
    @Size(max = 1000, message = "备注的最大长度:1000")
    private String remark;


}
