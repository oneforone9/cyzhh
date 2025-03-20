package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-04-20 17:29:36
 */

@Data
public class VPumpDynameicDataEsu extends Esu {

    private static final long serialVersionUID = 752923102472935675L;

                /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */            private String stcd;
        
            /**
     * 测站名称：测站编码所代表测站的中文名称。
     */            private String stnm;
        
            /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */            private Double lgtd;
        
            /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */            private Double lttd;
        
            /**
     * 河系id
     */            private Integer riverId;
        
            /**
     * 是否为闸站工程
     */            private String gateProject;
        
            /**
     * 闸门型式/泵站类型
     */            private String gateType;
        
            /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */            private String sttp;
        
            /**
     * 工程建设情况
     */            private String projectConstruction;
        
            /**
     * 建成时间（年）
     */            private String buildTime;
        
            /**
     * 启闭型式
     */            private String hoistType;
        
            /**
     * 是否有启闭机房
     */            private String hoistRoom;
        
            /**
     * 是否有备用电源
     */            private String sparePower;
        
            /**
     * 闸孔数量（孔）
     */            private String holesNumber;
        
            /**
     * 水闸类型/水泵类型
     */            private String sluiceType;
        
            /**
     * 是否进行过抗震复核
     */            private String seismicReview;
        
            /**
     * 是否有水位观测设施
     */            private String observeDevice;
        
            /**
     * 是否有视频图像监测设施
     */            private String videoCems;
        
            /**
     * 水闸安全鉴定时间
     */            private String safetyAppraisalTime;
        
            /**
     * 单位id
     */            private String unitId;
        
            /**
     * 主键
     */            private String id;
        
            /**
     * 设备物理地址
     */            private String deviceAddr;
        
            /**
     * 泵站1 反馈
     */            private String p1FeedBack;
        
            /**
     * 泵站2 反馈
     */            private String p2FeedBack;
        
            /**
     * 流量
     */            private String flowRate;
        
            /**
     * 压力
     */            private String pressure;
        
            /**
     * 液位
     */            private String yPosition;
        
            /**
     * 泵站1 计时
     */            private String p1CountTime;
        
            /**
     * 泵2 计时
     */            private String p2CountTime;
        
            /**
     * A相电压
     */            private String aVoltage;
        
            /**
     * B相电压
     */            private String bVoltage;
        
            /**
     *  C相电压
     */            private String cVoltage;
        
            /**
     * 电流A
     */            private String aElectric;
        
            /**
     * 电流B
     */            private String bElectric;
        
            /**
     * 电流C
     */            private String cElectric;
        
            /**
     * 电能
     */            private String electric;
        
            /**
     * 累计流量
     */            private String addFlowRate;
        
            /**
     * 液位 高
     */            private String liquidHigh;
        
            /**
     * 液位 低
     */            private String liquidLow;
        
            /**
     * 泵1 远程
     */            private String p1Remote;
        
            /**
     * 泵1 运行
     */            private String p1Run;
        
            /**
     * 泵1 故障
     */            private String p1Hitch;
        
            /**
     * 泵2 远程
     */            private String p2Remote;
        
            /**
     * 泵2 运行
     */            private String p2Run;
        
            /**
     * 泵2 故障
     */            private String p2Hitch;
        
                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date date;


        /**
         * 泵1 远程
         */
        private String p3Remote;
        /**
         * 泵1 运行
         */
        private String p3Run;
        /**
         * 泵1 故障
         */
        private String p3Hitch;
}
