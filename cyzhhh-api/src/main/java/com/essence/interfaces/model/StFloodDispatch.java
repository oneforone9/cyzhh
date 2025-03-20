package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:30
 */

@Data
public class StFloodDispatch {

    /**
     * 工程调度主键
     */

    private String id;
    /**
     * 调度指令
     */
    private String schedulingCodeNew;
    /**
     * 下发级别0逐级1越级
     */
    private String rank;

    /**
     * 操作1接收
     */
    private String type;


}
