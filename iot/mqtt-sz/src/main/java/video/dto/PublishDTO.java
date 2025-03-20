package video.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PublishDTO implements Serializable {
    private String pid;



    private String addr;

    private String addrv;


}
