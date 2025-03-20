package com.essence.common.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname StStbprpBEntityDTO
 * @Description TODO
 * @Date 2022/10/14 15:17
 * @Created by essence
 */
@Data
public class StStbprpBEntityDTO implements Serializable {
    /**
     * 当前页码
     */
    @ExcelIgnore
    private Integer current;
    /**
     * 大小
     */
    @ExcelIgnore
    private Integer size;
    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
    @ExcelIgnore
    private String stcd;
    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @ExcelProperty("站点名称")
    private String stnm;
    /**
     * 河流名称：测站所属河流的id。
     */
    @ExcelProperty("所属河道")
    private String rvnm;
    /**
     * 所属单位id
     */
    @ExcelProperty()
    private String unitId;
    /**
     * 所属单位名称
     */
    @ExcelProperty()
    private String unitName;
    /**
     * 水系名称：测站所属水系的中文名称。
     */
    @ExcelIgnore
    private String hnnm;
    /**
     * 流域名称：测站所属流域的中文名称。
     */
    @ExcelIgnore
    private String bsnm;
    /**
     * 站址：测站代表点所在地县级以下详细地址。
     */
    @ExcelProperty("所在街道")
    private String stlc;
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ExcelProperty("纬度")
    private BigDecimal lttd;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */

    @ExcelProperty("经度")
    private BigDecimal lgtd;

    /**
     * 行政区划码：测站代表点所在地的行政区划代码。行政区划代码编码按GB /T 2260执行。
     */
    @ExcelIgnore
    private String addvcd;
    /**
     * 基面名称：测站观测水位时所采用的基面高程系的名称。除特别注明以外，本数据表中存储的关于某一测站的所有高程、水位数值均是相对于该测站基面的。
     */
    @ExcelIgnore
    private String dtmnm;
    /**
     * 基面高程：测站观测水位时所采用基面高程系的基准面与该水文站所在流域的基准高程系基准面的高差，计量单位为m。
     * 底高程
     */
    @ExcelIgnore
    private BigDecimal dtmel;
    /**
     * 基面修正值：测站基于基面高程的水位值，遇水位断面沉降等因素影响需要设置基面修正值来修正水位为基面高程，计量单位为m。
     */
    @ExcelIgnore
    private BigDecimal dtpr;
    /**
     * 站类（数字字典）：标识测站类型的两位字母代码。测站类型代码由两位大写英文字母组成，第一位固定不变，表示大的测站类型，第二位根据情况可以扩展，表示大的测站类型的细分，如果没有细分的情况下，重复第一位。
     * ZZ水位的 ZQ流量的
     */
    @ExcelProperty("站点类型")
    private String sttp;

    @ExcelProperty("站点类型")
    private List<String> sttps;
    /**
     * 报汛等级（数字字典）：描述测站报汛的级别
     */
    @ExcelIgnore
    private String frgrd;
    /**
     * 建站年月：测站完成建站的时间。编码格式为： YYYYMM。
     */
    @ExcelIgnore
    private String esstym;
    /**
     * 始报年月：测站建站后开始报汛的时间。编码格式同建站年月。
     */
    @ExcelIgnore
    private String bgfrym;
    /**
     * 隶属行业单位：测站所隶属的行业管理单位。
     */
    @ExcelIgnore
    private String atcunit;
    /**
     * 信息管理单位：测站信息报送质量责任单位，依据水利部水文局下发的文件《全国水情信息报送质量管理规定》（水文情〔2008〕5号），承担信息报送管理责任。
     */
    @ExcelIgnore
    private String admauth;
    /**
     * 交换管理单位：测站信息交换管理单位
     */
    @ExcelIgnore
    private String locality;
    /**
     * 测站岸别（数字字典）：描述测站站房位于河流的左岸或右岸的代码，取“0”表示观测站房位于河流的左岸，取“1”表示测站站房位于河流的右岸，若测站并不在河流上，则置为空值。
     */
    @ExcelIgnore
    private String stbk;
    /**
     * 测站方位：测站岸边面向测验断面所处的方位，单位为(°)。取值范围为指向正北定为0°，逆时针按照45°步长取值。
     */
    @ExcelIgnore
    private BigDecimal stazt;
    /**
     * 至河口距离：自测站基本水尺断面至该河直接汇入的河、库、湖、海汇合口的河流长度，灌渠或无尾河取空值，单位为km，计至一位小数。
     */
    @ExcelIgnore
    private BigDecimal dstrvm;
    /**
     * 集水面积：测站上游由该站控制的流域面积，单位为km2，计至整数位。
     */
    @ExcelIgnore
    private BigDecimal drna;
    /**
     * 拼音码：用于快速输入测站名称的编码，采用测站名称的汉语拼音首字母构成，不区分大小写。
     */
    @ExcelIgnore
    private String phcd;
    /**
     * 启用标志（数字字典）：启用标志取值“0”和“1”。当取值为“1”时，代表启用该站报汛；当测站报汛出现异常情况无法马上排除时，启用标识应设为“0”，停止该站报汛，默认值为“1”。
     */
    @ExcelIgnore
    private String usfl;
    /**
     * 备注：用于记载该条记录的一些描述性的文字，最长不超过100个汉字。
     */
    @ExcelIgnore
    private String comments;
    /**
     * 时间戳：用于保存该条记录的最新插入或者修改时间，取系统日期时间，精确到秒。
     */
    @ExcelIgnore
    private Date moditime;
    /**
     * 在线状态 1 在线  2 不在线
     */
    @ExcelIgnore
    private String onlineStatus;

    /**
     * 开始时间
     */
    @ExcelIgnore
    private String startTime;
    /**
     * 结束时间
     */
    @ExcelIgnore
    private String endTime;
    /**
     * 离线时长
     */
    @ExcelIgnore
    private String lineDown;

    /**
     * 1 正常  2 超警戒水位 3 流速低
     */
    @ExcelIgnore
    private Integer warningStatus;

    /**
     * 警戒水位
     */
    @ExcelIgnore
    private BigDecimal wrz;

    /**
     * 最高水位(改为"保证水位")
     */
    @ExcelIgnore
    private BigDecimal bhtz;

    /**
     * key:测站编码
     * value:sn码
     */
    @ExcelIgnore
    Map<String, String> stcdMap = new HashedMap<>();

    /**
     * 实际水位(水深)
     */
    @ExcelIgnore
    private BigDecimal waterPosition;

    /**
     * 实时流量
     */
    @ExcelIgnore
    private BigDecimal waterRate;
    /**
     * 站点类型_雨量站使用
     */
    @ExcelIgnore
    private String sitelx;
    /**
     * 观测要素_雨量站使用
     */
    @ExcelIgnore
    private String gcys;
    /**
     * 降水监测时间_雨量
     */
    @ExcelIgnore
    private String jyjctime;
    /**
     * 所在区域
     */
    @ExcelIgnore
    private String area;
    /**
     * 所在街道
     */
    @ExcelIgnore
    private String street;

    /**
     * 时间
     */
    private String time;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 河系id
     */
    private Integer riverId;

    /**
     * 是否为闸站工程
     */
    private String gateProject;

    /**
     * 工程建设情况
     */
    private String projectConstruction;

    /**
     * 建成时间（年）
     */
    private String buildTime;

    /**
     * 闸门型式/泵站类型
     */
    private String gateType;

    /**
     * 启闭型式
     */
    private String hoistType;

    /**
     * 是否有启闭机房
     */
    private String hoistRoom;

    /**
     * 是否有备用电源
     */
    private String sparePower;

    /**
     * 闸孔数量（孔）
     */
    private String holesNumber;

    /**
     * 水闸类型/水泵类型
     */
    private String sluiceType;

    /**
     * 是否进行过抗震复核
     */
    private String seismicReview;

    /**
     * 是否有水位观测设施
     */
    private String observeDevice;

    /**
     * 是否有视频图像监测设施
     */
    private String videoCems;

    /**
     * 监测设施
     */
    private String cems;

    /**
     * 水闸安全鉴定时间
     */
    private String safetyAppraisalTime;

    /**
     * 水闸管理单位名称
     */
    private String managementUnit;

    /**
     * 水闸归口管理部门
     */
    private String managementDept;

    /**
     * 是否完成确权
     */
    private String wcqq;

    /**
     * 是否完成划界
     */
    private String wchj;

    /**
     * 是否有流量监测设施
     */
    private String flowDevice;

    /**
     * 流量监测设施
     */
    private String flow;

    /**
     * 设计过闸流量(m³/s）
     */
    private String designFlow;

    /**
     * 闸底高程（m）
     */
    private String gateBottom;

    /**
     * 闸顶高程（m）
     */
    private String gateTop;

    /**
     * 报警水位(去掉)
     */
    private BigDecimal alarmLevelWater;
    /**
     * 报警流量
     */
    private BigDecimal alarmFlow;

    /**
     * 水位状态: 1 正常  2 超警戒 3 超保证
     */
    @ExcelIgnore
    private Integer waterStatus;

    /**
     * 流量状态: 1 正常 2 五年一遇 3 二十年一遇 4 五十年一遇 5 一百年一遇
     */
    @ExcelIgnore
    private Integer flowStatus;

    /**
     * 平均流量
     */
    @ExcelIgnore
    private BigDecimal avgRate;
    /**
     * 日供水量
     */
    @ExcelIgnore
    private BigDecimal dayProvideWater;
    /**
     * 日供水量
     */
    @ExcelIgnore
    private Boolean requireProvide = false;

    /**
     * 流量站检测类型
     */
    private String flowType;
    /**
     * 可供水量使用1是
     */
    private String flowTypeG;
    /**
     * 能否远控 1-能远控
     */
    @ExcelIgnore
    private String remoteControl;

    /**
     * 远控故障 2-远控故障
     */
    @ExcelIgnore
    private String remoteControl2;

    /**
     * 关联的摄像头code
     */
    @ExcelIgnore
    private String videoCode;
    /**
     * 水位(实时水位)
     */
    @ExcelIgnore
    private BigDecimal position;
    /**
     * 负责人a
     */
    @ExcelIgnore
    private String namea;
    /**
     * 负责人b
     */
    @ExcelIgnore
    private String nameb;
    /**
     * 负责人c
     */
    @ExcelIgnore
    private String namec;

    /**
     * 摄像头code
     */
    @ExcelIgnore
    private String code;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    @ExcelIgnore
    private String source;

    /**
     * 摄像机类型
     */
    @ExcelIgnore
    private String cameraType;

}
