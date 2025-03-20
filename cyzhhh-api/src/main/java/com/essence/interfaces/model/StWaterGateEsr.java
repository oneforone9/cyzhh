package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-04-20 15:36:44
 */

@Data
public class StWaterGateEsr extends Esr {

    private static final long serialVersionUID = 706521332234028755L;

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
