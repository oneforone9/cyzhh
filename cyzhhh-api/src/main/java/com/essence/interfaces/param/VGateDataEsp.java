package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2023-04-20 17:35:50
 */

@Data
public class VGateDataEsp extends Esp {

    private static final long serialVersionUID = -82099884210576452L;

    @ColumnMean("id")
    private String id;
    @ColumnMean("pid")
    private String pid;
    @ColumnMean("type")
    private String type;
    /**
     * 采集名称
     */
    @ColumnMean("addr")
    private String addr;
    /**
     * 采集数据数值
     */
    @ColumnMean("addrv")
    private String addrv;
    /**
     * 采集时间
     */
    @ColumnMean("ctime")
    private String ctime;
    /**
     * sn码  实际机器标识码与clientid对应
     */
    @ColumnMean("did")
    private String did;
    /**
     * 站点编码
     */
    @ColumnMean("stcd")
    private String stcd;

}
