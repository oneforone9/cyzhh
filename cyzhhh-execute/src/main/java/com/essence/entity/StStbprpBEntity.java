package com.essence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 测站基本属性表用于存储测站的基本信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:11
 */
@Data
@TableName("st_stbprp_b")
public class StStbprpBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
	 */
    @TableId(value = "stcd", type = IdType.INPUT)
	private String stcd;
	/**
	 * 测站名称：测站编码所代表测站的中文名称。
	 */
	private String stnm;
	/**
	 * 河流名称：测站所属河流的中文名称。
	 */
	private String rvnm;
	/**
	 * 水系名称：测站所属水系的中文名称。
	 */
	private String hnnm;
	/**
	 * 流域名称：测站所属流域的中文名称。
	 */
	private String bsnm;
	/**
	 * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
	 */
	private BigDecimal lttd;
	/**
	 * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
	 */
	private BigDecimal lgtd;
	/**
	 * 站址：测站代表点所在地县级以下详细地址。
	 */
	private String stlc;
	/**
	 * 行政区划码：测站代表点所在地的行政区划代码。行政区划代码编码按GB /T 2260执行。
	 */
	private String addvcd;
	/**
	 * 基面名称：测站观测水位时所采用的基面高程系的名称。除特别注明以外，本数据表中存储的关于某一测站的所有高程、水位数值均是相对于该测站基面的。
	 */
	private String dtmnm;
	/**
	 * 基面高程：测站观测水位时所采用基面高程系的基准面与该水文站所在流域的基准高程系基准面的高差，计量单位为m。
     * 底高程
	 */
	private BigDecimal dtmel;
	/**
	 * 基面修正值：测站基于基面高程的水位值，遇水位断面沉降等因素影响需要设置基面修正值来修正水位为基面高程，计量单位为m。
	 */
	private BigDecimal dtpr;
	/**
	 * 站类（数字字典）：标识测站类型的两位字母代码。测站类型代码由两位大写英文字母组成，第一位固定不变，表示大的测站类型，第二位根据情况可以扩展，表示大的测站类型的细分，如果没有细分的情况下，重复第一位。
	 */
	private String sttp;
	/**
	 * 报汛等级（数字字典）：描述测站报汛的级别
	 */
	private String frgrd;
	/**
	 * 建站年月：测站完成建站的时间。编码格式为： YYYYMM。
	 */
	private String esstym;
	/**
	 * 始报年月：测站建站后开始报汛的时间。编码格式同建站年月。
	 */
	private String bgfrym;
	/**
	 * 隶属行业单位：测站所隶属的行业管理单位。
	 */
	private String atcunit;
	/**
	 * 信息管理单位：测站信息报送质量责任单位，依据水利部水文局下发的文件《全国水情信息报送质量管理规定》（水文情〔2008〕5号），承担信息报送管理责任。
	 */
	private String admauth;
	/**
	 * 交换管理单位：测站信息交换管理单位
	 */
	private String locality;
	/**
	 * 测站岸别（数字字典）：描述测站站房位于河流的左岸或右岸的代码，取“0”表示观测站房位于河流的左岸，取“1”表示测站站房位于河流的右岸，若测站并不在河流上，则置为空值。
	 */
	private String stbk;
	/**
	 * 测站方位：测站岸边面向测验断面所处的方位，单位为(°)。取值范围为指向正北定为0°，逆时针按照45°步长取值。
	 */
	private BigDecimal stazt;
	/**
	 * 至河口距离：自测站基本水尺断面至该河直接汇入的河、库、湖、海汇合口的河流长度，灌渠或无尾河取空值，单位为km，计至一位小数。
	 */
	private BigDecimal dstrvm;
	/**
	 * 集水面积：测站上游由该站控制的流域面积，单位为km2，计至整数位。
	 */
	private BigDecimal drna;
	/**
	 * 拼音码：用于快速输入测站名称的编码，采用测站名称的汉语拼音首字母构成，不区分大小写。
	 */
	private String phcd;
	/**
	 * 启用标志（数字字典）：启用标志取值“0”和“1”。当取值为“1”时，代表启用该站报汛；当测站报汛出现异常情况无法马上排除时，启用标识应设为“0”，停止该站报汛，默认值为“1”。
	 */
	private String usfl;
	/**
	 * 备注：用于记载该条记录的一些描述性的文字，最长不超过100个汉字。
	 */
	private String comments;
	/**
	 * 时间戳：用于保存该条记录的最新插入或者修改时间，取系统日期时间，精确到秒。
	 */
	private Date moditime;
	/**
	 * 警戒水位
	 */
	private BigDecimal wrz;
	/**
	 * 最高水位
	 */
	private BigDecimal bhtz;
	/**
	 * 站点类型_雨量站使用
	 */
	private String sitelx;
	/**
	 * 观测要素_雨量站使用
	 */
	private String gcys;
	/**
	 * 降水监测时间_雨量
	 */
	private String jyjctime;
	/**
	 * 所在区域
	 */
	private String area;
	/**
	 * 所在街道
	 */
	private String street;

	private String zsection;

    /**
     * 摄像头code
     */
    @TableField("code")
    private String code;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    @TableField("source")
    private String source;

    /**
     * 摄像机类型
     */
    @TableField("camera_type")
    private String cameraType;
}
