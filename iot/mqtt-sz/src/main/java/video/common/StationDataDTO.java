package video.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cuirx
 * @Classname StationDataDTO
 * @Description TODO
 * @Date 2022/10/11 19:44
 * @Created by essence
 */
@Data
public class StationDataDTO implements Serializable {
    /**
     * PLCID
     */
    private String pid;
    /**
     * “0” 为模拟量  “1” 为遥信号
     */
    private String type;
    /**
     * 数据类型 id
     */
    private String addr;
    /**
     * 场站数据值
     */
    private String addrv;
    /**
     * 采集时间
     */
    private String ctime;
}
