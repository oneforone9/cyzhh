package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author majunjie
 * @since 2024-09-11 14:21:34
 */

@Data
public class ViewOfficeBaseEsu extends Esu {

    private static final long serialVersionUID = 831644238802147241L;

    /**
     * 主键
     */        private Integer id;    

    /**
     * 科室名
     */        private String deptName;    

    /**
     * 排序
     */        private Integer sort;    

    /**
     * 科室基础表ID（st_office_base.id）
     */        private Integer officeBaseId;    

    /**
     * 使用人姓名
     */        private String userName;    

    /**
     * 职务
     */        private String job;    

    /**
     * 用户id
     */        private String userId;    


}
