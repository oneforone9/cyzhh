package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-04-20 17:35:52
 */

@Data
public class VGateDataEsr extends Esr {

    private static final long serialVersionUID = 175264504952858410L;

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
    /**
     * 站点编码
     */
    private String stcd;


}
