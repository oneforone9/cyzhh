package video.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WaterGateDTO implements Serializable {
    private String did;

    private String utime;

    private List<WaterGateContentDTO> content;
}
