package com.essence.interfaces.dot;

import com.essence.interfaces.vaild.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 9:39
 */
@Data
public class RiverStepSectionDto {

    /**
     * 步长
     */
    String step;

    /**
     * 步长对应的断面列表
     */
    List<RiverSectionViewDto> sectionList;
    /**
     * 步长时间 (中间时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stepTime;
}
