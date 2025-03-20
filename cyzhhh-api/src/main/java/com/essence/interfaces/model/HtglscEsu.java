package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 合同管理收藏更新实体
 *
 * @author majunjie
 * @since 2024-09-13 16:18:30
 */

@Data
public class HtglscEsu extends Esu {

    private static final long serialVersionUID = -98754413216516419L;

    /**
     * 主键
     */        private String id;    

    /**
     * 合同编号
     */        private String htid;

    /**
     * 用户id
     */        private String userId;    


}
