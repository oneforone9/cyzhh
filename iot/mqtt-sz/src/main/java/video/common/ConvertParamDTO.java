package video.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author essence
 * @Classname ConvertParamDTO
 * @Description TODO
 * @Date 2022/10/9 17:12
 * @Created by essence
 */
@Data
public class ConvertParamDTO implements Serializable {
    private String rtspUrl ;
    private String convertName ;
}
