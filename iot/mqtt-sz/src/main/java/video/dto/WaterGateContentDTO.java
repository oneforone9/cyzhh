package video.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WaterGateContentDTO implements Serializable {
    private String pid;

    private String type;

    private String addr;

    private String addrv;

    private String ctime;
}
