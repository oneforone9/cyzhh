package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运行块样式实体类
 *
 * @author majunjie
 * @since 2024-09-09 17:48:33
 */
@Data
public class RunStyle extends Esr {

    private static final long serialVersionUID = 984533482413267853L;


    private String fontFamily;
    private int fontSize = -1;
    private boolean bold;
    private boolean italic;
    private boolean strike;
    private String color;
    private int textPosition;
    private UnderlinePatterns underline;
    private int characterSpacing;

}
