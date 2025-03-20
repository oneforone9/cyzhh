package video.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname RequestDTO
 * @Description TODO
 * @Date 2022/10/10 14:06
 * @Created by essence
 */
@Data
public class RequestDTO implements Serializable {

    private MediaURLParam mediaURLParam;

    private String cameraCode;
}
