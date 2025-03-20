package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_video_warning")
public class VideoWarningInfoEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    @TableField("DBAlarmID")
    public int DBAlarmID ;//报警信息 ID（唯一）
    @TableField("AlarmID")
    public int AlarmID ;
    @TableField("ScannerAlarmID")
    public String ScannerAlarmID;
    @TableField("IntellexAlarmID")
    public String IntellexAlarmID;
    @TableField("ScannerCustomerNumber")
    public String ScannerCustomerNumber;// 分析设备编号
    @TableField("ScannerFeedNumber")
    public String ScannerFeedNumber;//分析通道号
    @TableField("ScannerIP")
    public String ScannerIP;//分析设备 IP
    @TableField("VideoSourceSystemID")
    public String VideoSourceSystemID;
    @TableField("VideoSourceChannelID")
    public String VideoSourceChannelID;
    @TableField("AlarmType")
    public String AlarmType;//报警类型
    @TableField("ManagerStartTime")
    public String ManagerStartTime;//服务端收到开始报警的时间
    @TableField("ManagerAlarmTime")
    public String ManagerAlarmTime;//服务端收到开始报警的时间
    @TableField("ManagerEndTime")
    public String ManagerEndTime;//服务端收到结束报警的时间
    @TableField("VideoSourceAlarmStartTime")
    public String VideoSourceAlarmStartTime;
    @TableField("VideoSourceAlarmTime")
    public String VideoSourceAlarmTime;
    @TableField("VideoSourceAlarmEndTime")
    public String VideoSourceAlarmEndTime;
    @TableField("JPBPath")
    public String JPBPath;//报警视频路径
    @TableField("Thumbnail")
    public String Thumbnail;
    @TableField("ThumbnailRows")
    public String ThumbnailRows;
    @TableField("ThumbnailCols")
    public String ThumbnailCols;
    @TableField("ThumbnailSize")
    public String ThumbnailSize;
    @TableField("ThumbnailPath")
    public String ThumbnailPath;//报警图片路径
    @TableField("ScannerName")
    public String ScannerName;//分析设备名称
    @TableField("spareInt1")
    public String spareInt1;
    @TableField("spareInt2")
    public String spareInt2;
    @TableField("spareInt3")
    public String spareInt3;//状态
    @TableField("spareStr32_1")
    public String spareStr32_1;//设备类型
    @TableField("spareStr32_2")
    public String spareStr32_2;
    @TableField("spareStr64_2")
    public String spareStr64_2;// List<LPRDto>对象的 json 字符串，
    @TableField("ruleName")
    public String ruleName; //转码后的报警名称 public String AlarmLevelID{ get; set; }
    @TableField("UserID")
    public String UserID;
    @TableField("ackTime")
    public String ackTime;
    @TableField("RuleNameStr")
    public String RuleNameStr; //报警中文名称 ，根据此字段区分告警类型
    @TableField("PTZNumber")
    public String PTZNumber;// 预置位编号
    @TableField("SpeciesID")
    public String SpeciesID;
    @TableField("DVUID")
    public String DVUID;
    @TableField("spareStr64_1")
    public String spareStr64_1;//摄像机名称
    @TableField("DeviceID")
    public int DeviceID;//相机编号
}
