package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 设备巡检会议室关联巡检信息更新实体
 *
 * @author majunjie
 * @since 2025-01-09 14:00:53
 */

@Data
public class XjHysxjxxEsu extends Esu {

    private static final long serialVersionUID = -84935584560332424L;

    /**
     * 主键
     */        private String id;    

    /**
     * 会议室id
     */        private String hysId;    

    /**
     * 会议室巡检项目
     */        private String hysXjxm;    

    /**
     * 排序
     */        private Integer px;    


}
