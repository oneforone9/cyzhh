package com.essence.common.constant;

/**
 * @author zhy
 * @date 2021/12/10 17:55
 * @Description 项目常量
 */
public class ItemConstant {

    /**
     * 开发环境
     */
    public final static String DEV = "dev";

    /**
     * 组织的根的父id
     */
    public final static long ORGANIZATION_PID_ROOT = 0;

    /**
     * 河道级别-区
     */
    public final static String REA_LEVEL_1 = "1";
    /**
     * 河道级别-乡镇
     */
    public final static String REA_LEVEL_2 = "2";
    /**
     * 河道级别-村
     */
    public final static String REA_LEVEL_3 = "3";
    /**
     * 组织用户同步到euauth组织下的根
     */
    public final static String CORP_ROOT_ID = "58291a8e-7231-4c5e-ae8f-93cd5e7050b8";
    /**
     * 组织用户同步到euauth组织下的默认级别
     */
    public final static Integer CORP_LEVEL_DEFAULT = 3;
    /**
     * 组织用户同步到euauth组织下的默认排序
     */
    public final static Integer CORP_ORDER_DEFAULT = 3;

    /**
     * 类型(1 内部人员 2第三方人员)
     */
    public final static String PERSON_TYPE_INSIDE = "1";
    /**
     * 状态(0启用 1停用)
     */
    public final static String PERSON_STATUS_USING = "0";
    /**
     * 用户默认密码
     */
    public final static String USER_PASSSWORD_DEFAUTL = "123qwe!@#";
    /**
     * 用户默认初次登录是否需要修改密码 0需要 1 不需要
     */
    public final static Integer USER_PASSSWORD_CHANGED = 1;
    /**
     * 用户默认角色id
     */
    public final static String USER_ROLE_DEFAUTL = "772a008eacd0466f8064dfd4e39c0b16";
    /**
     * 巡检用户默认角色id
     */
    public final static String USER_XJ_ROLE_DEFAUTL = "3";
    /**
     * 巡检用户默认部门id
     */
    public final static String USER_XJ_PUT_DEFAUTL = "3";

    /**
     * 巡检默认操作人-系统
     */
    public final static String XJCZR = "系统";
    /**
     * 巡检默认操作描述-生成工单
     */
    public final static String XJSCGD = "工单生成";
    /**
     * 巡检默认操作描述-开启工单
     */
    public final static String XJKQGD = "工单开启";
    /**
     * 巡检默认操作描述-终止工单
     */
    public final static String XJZZGD = "工单终止";
    /**
     * 巡检默认操作描述-完成工单
     */
    public final static String XJWCGD = "工单完成";
    /**
     * 巡检默认操作描述-工单负责人变更
     */
    public final static String XJFZRBGGD = "工单负责人变更";
    /**
     * 巡检默认操作描述-审核工单
     */
    public final static String XJSHGD = "工单审核不通过";
    /**
     * 巡检默认操作描述-派发工单
     */
    public final static String XJPFGD = "工单派发";
    /**
     * 巡检默认操作描述-工单巡检完成待审核工单
     */
    public final static String XJDSHGD = "工单巡检完成待审核";


    /**
     * 人员巡河排班信息表 是否删除(1是 0否)
     */
    public final static String ROSTERING_IS_DELETE = "1";
    public final static String ROSTERING_NO_DELETE = "0";

    //---------------------工单  start----------------
    /**
     * 工单状态-终止-终止
     */
    public final static String ORDER_STATUS_STOP_FINAL = "-2";
    /**
     * 工单状态-终止-审核
     */
    public final static String ORDER_STATUS_STOP_AUDIT = "-1";
    /**
     * 工单状态-未派发
     */
    public final static String ORDER_STATUS_NO_HANDOUT = "1";
    /**
     * 工单状态-未开始
     */
    public final static String ORDER_STATUS_NO_START = "2";
    /**
     * 工单状态-进行中
     */
    public final static String ORDER_STATUS_RUNNING = "3";
    /**
     * 工单状态-待审核
     */
    public final static String ORDER_STATUS_EXAMINNING = "4";
    /**
     * 工单状态-已归档
     */
    public final static String ORDER_STATUS_END = "5";
    /**
     * 工单状态-已终止
     */
    public final static String ORDER_STATUS_OVER = "6";
    /**
     * 工单状态-催办
     */
    public final static String ORDER_STATUS_URGE = "7";

    /**
     * 工单状态-养护后待审核
     */
    public final static String ORDER_STATUS_YHWC = "8";
    /**
     * 工单状态-驳回
     */
    public final static String ORDER_STATUS_REFUSE = "9";
    /**
     * 工单-是否关注（1是 0否）
     */
    public final static String ORDER_NO_ATTENTION = "0";
    /**
     * 工单-是否关注（1是 0否）
     */
    public final static String ORDER_IS_ATTENTION = "1";
    /**
     * 工单-是否删除 (1是 0否)
     */
    public final static String ORDER_NO_DELETE = "0";
    /**
     * 工单-是否删除 (1是 0否)
     */
    public final static String ORDER_IS_DELETE = "1";
    /**
     * 工单类型-巡查
     */
    public final static String ORDER_TYPE_XC = "1";
    /**
     * 工单类型-保洁
     */
    public final static String ORDER_TYPE_BJ = "2";
    /**
     * 工单类型-绿化
     */
    public final static String ORDER_TYPE_LH = "3";
    /**
     * 工单类型-维保
     */
    public final static String ORDER_TYPE_WB = "4";
    /**
     * 工单类型-运行
     */
    public final static String ORDER_TYPE_YX = "5";
    /**
     * 工单类型-养护
     */
    public final static String ORDER_TYPE_YH = "6";

    /**
     * 工单类型-问题工单
     */
    public final static String ORDER_TYPE_WT = "7";

    /**
     * 工单来源-计划生成
     */
    public final static String ORDER_SOURCE_JH = "1";
    /**
     * 工单来源-巡查上报
     */
    public final static String ORDER_SOURCE_XC = "2";

    /**
     * 工单来源-临时生成
     */
    public final static String ORDER_SOURCE_LSSC = "3";
    /**
     * 工单来源-群众上报
     */
    public final static String ORDER_SOURCE_QZSB = "4";
    /**
     * 派发方式-人工派发
     */
    public final static String ORDER_DISRRIBUTE_RG = "1";
    /**
     * 派发方式-自动派发
     */
    public final static String ORDER_DISRRIBUTE_ZD = "2";
    /**
     * 巡河工单后缀
     */
    public final static String ORDER_NAME_SUFFIX_XC= "巡河工单";
    /**
     * 巡河工单后缀
     */
    public final static String ORDER_NAME_SUFFIX_YH= "养护工单";

    //---------------------工单    end----------------

    /**
     * 文件管理- 是否删除(1是 0否)
     */
    public final static String FILE_IS_DELETE = "1";

    public final static String FILE_NO_DELETE = "0";

    /**
     * 文件目录
     */
    public final static String FILE_LIST_NAME = "cyzhhhfile";

    /**
     * 工单流程中的文件外键前缀（处理后的图片）
     */
    public final static String ORDER_FILE_PREFIX = "ANSWER";

    /**
     * 待解决的文件外键前缀（处理前的图片）
     */
    public final static String QUES_FILE_PREFIX = "QUES";

    //---------------------事件  start----------------
    /**
     * 案件渠道-市级交办
     */
    public final static String EVENT_CHANNEL_CITY = "1";
    /**
     * 案件渠道-区级交办
     */
    public final static String EVENT_CHANNEL_COUNTY = "2";
    /**
     * 案件渠道-自查
     */
    public final static String EVENT_CHANNEL_NO_APPEAL = "3";
    /**
     * 案件渠道-12345
     */
    public final static String EVENT_CHANNEL_12345 = "4";
    /**
     * 案件渠道-网络上报
     */
    public final static String EVENT_CHANNEL_NETWORK = "5";
    /**
     * 案件渠道-网络上报
     */
    public final static String EVENT_CHANNEL_AI = "6";

    /**
     * 案件渠道-水环境案件
     */
    public final static String EVENT_TYPE_SHJ = "1";
    /**
     * 案件渠道-涉河工程和有关活动案件
     */
    public final static String EVENT_TYPE_SHGC = "2";
    /**
     * 案件渠道-河道附属设施案件
     */
    public final static String EVENT_TYPE_HDSS = "3";

    /**
     * 案件渠道-群众上报
     */
    public final static String EVENT_TYPE_QZSB = "5";
    /**
     * 案件分类-水环境案件-11
     */
    public final static String EVENT_CLASS_11 = "11";
    /**
     * 案件分类-水环境案件-12
     */
    public final static String EVENT_CLASS_12 = "12";
    /**
     * 案件分类-水环境案件-13
     */
    public final static String EVENT_CLASS_13 = "13";
    /**
     * 案件分类-水环境案件-14
     */
    public final static String EVENT_CLASS_14 = "14";
    /**
     * 案件分类-水环境案件-15
     */
    public final static String EVENT_CLASS_15 = "15";
    /**
     * 案件分类-涉河工程和有关活动案件-21
     */
    public final static String EVENT_CLASS_21 = "21";
    /**
     * 案件分类-涉河工程和有关活动案件-22
     */
    public final static String EVENT_CLASS_22 = "22";
    /**
     * 案件分类-涉河工程和有关活动案件-23
     */
    public final static String EVENT_CLASS_23 = "23";

    /**
     * 案件分类-河道附属设施案件-31
     */
    public final static String EVENT_CLASS_31 = "31";
    /**
     * 案件分类-河道附属设施案件-32
     */
    public final static String EVENT_CLASS_32 = "32";
    /**
     * 案件分类-河道附属设施案件-33
     */
    public final static String EVENT_CLASS_33 = "33";
    /**
     * 案件分类-河道附属设施案件-34
     */
    public final static String EVENT_CLASS_34 = "34";

    /**
     * 案件-是否删除（1是 0否）
     */
    public final static String EVENT_IS_DELETE = "1";

    /**
     * 案件-是否删除（1是 0否）
     */
    public final static String EVENT_NO_DELETE = "0";

    //---------------------事件    end----------------
    /**
     * 案件-字典类型（1是 0否）
     */
    public final static String DICTTYPE_IS_DELETE = "1";

    /**
     * 案件-字典类型（1是 0否）
     */
    public final static String DICTTYPE_NO_DELETE = "0";
    /**
     * 案件-字典数据（1是 0否）
     */
    public final static String DICTDATA_IS_DELETE = "1";

    /**
     * 案件-字典数据（1是 0否）
     */
    public final static String DICTDATA_NO_DELETE = "0";



    //-------------------汛情上报  begin----------------------

    /**
     * 上报-是否删除（1是 0否）
     */
    public final static String REPORT_NO_DELETE = "0";

    /**
     * 汛情上报哦文件外键前缀（上报时）
     */
    public final static String REPORT_FILE_PREFIX = "REPORT";


    //-------------------汛情上报  begin----------------------



    //------------绿化保洁工作日志上报----------
    /**
     * 绿化保洁工作日志上报前缀（上报时）
     */
    public final static String GREEN_FILE_PREFIX = "GREENREPORT";
    //------------绿化保洁工作日志上报-----------




    //------------闸坝运行维保日志上报----------

    /**
     *闸坝运行维保日志上报前缀（上报时）
     */
    public final static String GATE_FILE_PREFIX = "GATEREPORT";
    /**
     *闸坝运行维保日志上报前缀（上报时）
     */
    public final static String GATEBASE_FILE_PREFIX = "GATEBASEREPORT";
    //------------闸坝运行维保日志上报-----------

    /**
     * 合同历程--0未办
     */
    public final static Integer HT_LCWB = 0;
    /**
     * 合同历程--1已办
     */
    public final static Integer HT_LCYB = 1;
    /**
     * 合同描述--0
     */
    public final static String HT_MS0 ="合法性申请已填写" ;
    /**
     * 合同描述--0
     */
    public final static String HT_MS1 ="合法性申请已审核" ;
    /**
     * 合同描述--0
     */
    public final static String HT_MS2 ="合法性申请已确认" ;


    /**
     * 合同--0申请人（保存）草稿
     */
    public final static Integer HT__1 = -1;

    /**
     * 合同--0申请人（保存）待提交
     */
    public final static Integer HT_0 = 0;

    /**
     * 合同--1律师（保存）待提交
     */
    public final static Integer HT_1 = 1;

    /**
     * 合同--2申请人待确认审查单
     */
    public final static Integer HT_2 = 2;

    /**
     * 合同--3科长待确认预审单
     */
    public final static Integer HT_3 = 3;
    /**
     * 合同--4主管领导待确认预审单
     */
    public final static Integer HT_4 = 4;
    /**
     * 合同--5申请人待填写会签单
     */
    public final static Integer HT_5 = 5;
    /**
     * 合同--6科长待确认会签单
     */
    public final static Integer HT_6 = 6;

    /**
     * 合同--7申请人待选择财务科人员和办公室人员
     */
    public final static Integer HT_7 = 7;
    /**
     * 合同--8财务科待确认会签单
     */
    public final static Integer HT_8= 8;
    /**
     * 合同--8办公室待确认会签单
     */
    public final static Integer HT_8B= 8;
    /**
     * 合同--9申请人待确认会签单并选择领导
     */
    public final static Integer HT_9 = 9;
    /**
     * 合同--10主管领导待确认会签单
     */
    public final static Integer HT_10 = 10;
    /**
     * 合同--11主要领导待确认会签单
     */
    public final static Integer HT_11 = 11;
    /**
     * 合同--12办公室待审查
     */
    public final static Integer HT_12 = 12;
    /**
     * 合同--13申请人待确认会签
     */
    public final static Integer HT_13 = 13;
    /**
     * 合同--14完成
     */
    public final static Integer HT_WC14 = 14;
}
