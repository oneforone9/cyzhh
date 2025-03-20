package com.essence.common.dto.health;

import lombok.Data;

import java.io.Serializable;

@Data
public class ManageBelongDto implements Serializable {
     private String  prjadmincd;
     /**
      * 红码
      */
     private Integer cntRedCode;
     /**
      * 黄码
      */
     private Integer cntYellowCode;
     /**
      * 绿码
      */
     private Integer cntGreenCode;
     /**
      * 河流
      */
     private String prjadminnm;
}
