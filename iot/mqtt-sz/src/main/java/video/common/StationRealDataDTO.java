package video.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author cuirx
 * @Classname StationRealDataDTO
 * @Description TODO
 * @Date 2022/10/11 19:34
 * @Created by essence
 */
@Data
public class StationRealDataDTO implements Serializable {
    /**
     * PLCID
     */
    private String did;
    /**
     * 上报时间
     */
    private String utime;

    private List<StationDataDTO> content;


}
