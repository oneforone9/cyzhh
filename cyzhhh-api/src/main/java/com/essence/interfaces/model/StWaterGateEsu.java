package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author majunjie
 * @since 2023-04-20 15:36:40
 */

@Data
public class StWaterGateEsu extends Esu {

    private static final long serialVersionUID = -69177504964056072L;

    private String id;

    private String pid;

    private String type;

    /**
     * 采集名称
     */
    private String addr;

    /**
     * 采集数据数值
     */
    private String addrv;

    /**
     * 采集时间
     */
    private String ctime;

    /**
     * sn码  实际机器标识码与clientid对应
     */
    private String did;


}
