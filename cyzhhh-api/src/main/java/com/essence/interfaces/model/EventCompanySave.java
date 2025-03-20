package com.essence.interfaces.model;


import com.essence.interfaces.vaild.Insert;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 第三方服务公司人员配置更新实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:58
 */

@Data
public class EventCompanySave implements Serializable {


    /**
     * 第三方服务公司主键id
     */

    private String companyId;

    /**
     * 公司名称
     */
    @Size(max = 255, message = "公司名称的最大长度：255")
    @NotEmpty(groups = Insert.class, message = "公司名称不能为空")
    private String company;

    /**
     * 服务年份
     */
    @Size(max = 10, message = "服务年份的最大长度：10")
    @NotEmpty(groups = Insert.class, message = "服务年份不能为空")
    private String serviceYear;

    /**
     * 所属单位主键
     */
    @Size(max = 32, message = "所属单位主键的最大长度：32")
    @NotEmpty(groups = Insert.class, message = "所属单位主键不能为空")
    private String unitId;

    /**
     * 所属管辖单位
     */
    @Size(max = 20, message = "所属管辖单位的最大长度：20")
    @NotEmpty(groups = Insert.class, message = "所属管辖单位不能为空")
    private String unitName;

    /**
     * 服务类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    @NotNull(groups = Insert.class, message = "服务类型不能为空")
    private Integer type;

    /**
     * 河段id
     */
    private List<String> riverIdList;

    /**
     * 河名称
     */
    private List<String> rvnmList;

    /**
     * 处理人
     */
    @Size(max = 255, message = "处理人的最大长度：255")
    @NotEmpty(groups = Insert.class, message = "处理人不能为空")
    private String name;

    /**
     * 处理人id
     */
    private String userId;

    /**
     * 处理人联系方式
     */
    @Size(max = 255, message = "联系方式的最大长度：255")
    @NotEmpty(groups = Insert.class, message = "联系方式不能为空")
    private String phone;

    /**
     * 是否负责人0否1是
     */
    private Integer fType;
}
