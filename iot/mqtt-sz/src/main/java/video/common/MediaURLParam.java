package video.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MediaURLParam
 * @Description TODO
 * @Date 2022/10/10 10:09
 * @Created by essence
 */
@Data
public class MediaURLParam implements Serializable {

    private Integer broadCastType;
    private Integer packProtocolType;
    private Integer protocolType;
    private Integer serviceType;
    private Integer streamType;
    private Integer transMode;
    private Integer clientType;
}
