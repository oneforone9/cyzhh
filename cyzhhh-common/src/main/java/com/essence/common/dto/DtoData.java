package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class DtoData implements Serializable {
    public int DBAlarmID ;//报警信息 ID（唯一）
    public int AlarmID ;
    public String ScannerAlarmID;
    public String IntellexAlarmID;
    public String ScannerCustomerNumber;// 分析设备编号
    public String ScannerFeedNumber;//分析通道号
    public String ScannerIP;//分析设备 IP
    public String VideoSourceSystemID;
    public String VideoSourceChannelID;
    public String AlarmType;//报警类型
    public String ManagerStartTime;//服务端收到开始报警的时间
    public String ManagerAlarmTime;//服务端收到开始报警的时间
    public String ManagerEndTime;//服务端收到结束报警的时间
    public String VideoSourceAlarmStartTime;
    public String VideoSourceAlarmTime;
    public String VideoSourceAlarmEndTime;
    public String JPBPath;//报警视频路径
    public String Thumbnail;
    public String ThumbnailRows;
    public String ThumbnailCols;
    public String ThumbnailSize;
    public String ThumbnailPath;//报警图片路径
    public String ScannerName;//分析设备名称
    public String spareInt1;
    public String spareInt2;
    public String spareInt3;//状态
    public String spareStr32_1;//设备类型
    public String spareStr32_2;
    public String spareStr64_2;// List<LPRDto>对象的 json 字符串，
    public String ruleName; //转码后的报警名称 public String AlarmLevelID{ get; set; }
    public String UserID;
    public String ackTime;
    public String RuleNameStr; //报警中文名称 ，根据此字段区分告警类型
    public String PTZNumber;// 预置位编号
    public String SpeciesID;
    public String DVUID;
    public String spareStr64_1;//摄像机名称
    public int DeviceID;//相机编号
}
