//package com.essence.service.impl.alg;
//
//import com.sun.jna.Library;
//import com.sun.jna.Native;
//import com.sun.jna.ptr.DoubleByReference;
//
///**
// *
// */
//public interface Sprs1d extends Library {
//
//    Sprs1d model2D = Native.load("E:\\model\\alg\\SPRS1D.dll", Sprs1d.class);
//
//    /**
//     * 打开模型文件
//     * @param inpPath
//     * @return
//     */
//    int API_SPRS_1D_OPEN(String inpPath);
//
//    /**
//     * 模型初始化
//     * @return
//     */
//    int API_SPRS_1D_INIITIAL();
//
//    /**
//     * 模型单步 计算
//     * @param elapsedTime
//     * @return
//     */
//    int API_SPRS_1D_STEP(DoubleByReference elapsedTime);
//
//    /**
//     * 模型关闭
//     * @return
//     */
//    int API_SPRS_1D_CLOSE();
//
//
//
//    //获取结果，可用 elapsedTime 控制输出间隔，不建议每步获取结果
//    int API_SPRS_1D_RESULT(double[] Elevation,  double[] Depth,  double[] Flow,  double[] Velcity,  double[] Area,double[]  Width);
//
//    /**
//     * 时间序列
//     * @return
//     */
//    int API_SPRS_1D_TIMESERIES_NUMBER();
//
//    /**
//     * 时间序列名称
//     * @param index
//     * @return
//     */
//    int API_SPRS_1D_TIMESERIES_NAME(int index, byte[] ID);
//
//    /**
//     * 时间序列
//     * @param id 时间序列名称
//     * @param mode 整型，mode=1 时代表末尾追加，mode=2 时代表清空之前之后再追加， 建议第一次时设为 2，之后增加的设为 1
//     * @param cDate 拟增加的日期，注意格式，如“"02/01/2023"”
//     * @param cTime cTime，拟增加的时间，如“"0:00"”
//     * @param cValue cValue，拟增加的值，注意为字符格式（即使值为 double 或 int 类型，应 转为字符串传入）。
//     *
//     *               //修改时间序列的值
//     *               API_SPRS_1D_TIMESERIES_ADD("t1", 2, "02/01/2023", "0:00", "250");
//     *               API_SPRS_1D_TIMESERIES_ADD("t1", 1, "02/01/2023", "1:00", "250");
//     *               API_SPRS_1D_TIMESERIES_ADD("t1", 1, "02/01/2023", "2:00", "250");
//     * @return 返回错误代码，为 0 则为正确调用。
//     */
//    int API_SPRS_1D_TIMESERIES_ADD(byte[] id, int mode, String cDate, String cTime, String cValue);
//
//    /**
//     * 获取曲线数量
//     * @return
//     */
//    int API_SPRS_1D_CURVE_NUMBER();
//
//    /**
//     * 获取曲线名称
//     * @param index 曲线序号，序号从 0 开始，不得大于等于曲线数量
//     * @param ID 传入字符串地址，返回曲线名称。
//     * @return
//     */
//    int API_SPRS_1D_CURVE_NAME(int index, byte[] ID);
//
//    /**
//     * 修改曲线值
//     * @param id 曲线名称
//     * @param mode 修改模式，整型，mode=1 代表末尾追加，mode=2 代表清空之前的， 再追加
//     * @param x 曲线上 x 的值，即使为数值也需转为字符串传入。
//     * @param y 曲线上 y 的值，即使为数值也需转为字符串传入。
//     * @return 返回错误代码，为 0 则为正确调用。
//     */
//    int API_SPRS_1D_CURVE_ADD(String id, int mode, String x, String y);
//
//    /**
//     * 5.1 获取控制规则数量
//     * @return
//     */
//    int API_SPRS_1D_CONTROL_NUMBER();
//
//    /**
//     * 5.2 获取控制规则名称
//     * @param index 规则序号，序号从 0 开始，不得大于等于规则数量。
//     * @param ID 传入字符串地址，执行后返回控制规则名称。
//     * @return
//     */
//    int API_SPRS_1D_CONTROL_NAME(int index,  byte[] ID);
//
//    /**
//     * 修改控制规则内容
//     * @param id  规则的名称。
//     * @param mode 用于指明修改对象，mode=1 为修改紧跟 IF 后条件 1，mode=2 为紧 跟 AND/OR 后的条件 2，mode=3 为修改紧跟 THEN 后的动作 1，mode=4 代表修改 ELSE 后对应的动作 2
//     * @param newValue newValue，新的值，替换原有规则中条件或者动作里的值
//     *
//     *
//     *                 //RULE 为关键字，R2B 为规则名称，示例中规则的主要目标是设置当堰/闸前水位大于 10 米且流量大 于 100 立方米每秒时，堰/闸的开度设为 1.0 RULE R2B //条件 1 ：NODE 为关键字，3 为堰/闸前断面的名称，DEPTH 为水深关键词，即堰/闸上水深大于 10m， 10m 为可修改值 IF NODE 3 DEPTH > 10 //条件 2 ：NODE 为关键字，3 为堰/闸上游断面的名称，INFLOW 为流量关键词，即堰/闸上游来流量 大于 100，100 为可修改值 AND NODE 3 INFLOW > 100 //动作 1 ：WEIR 为堰的关键词，weir1 为堰的名称，SETTING 为开度，1.0 为开度，为可修改值 THEN WEIR weir1 SETTING = 1.0 //动作 2：0.5 为开度，为可修改值 ELSE WEIR weir1 SETTING = 0.5
//     * @return 返回错误代码，为 0 则为正确调用。
//     */
//    int API_SPRS_1D_CONTROL_REVISE(String id, int mode, DoubleByReference newValue);
//
//    /**
//     * 6.1获取断面名称
//     * @return
//     */
//    int API_SPRS_1D_TRANSECT_NUMBER();
//
//    /**
//     * 6.2
//     * 获取断面的名称
//     * @param index ,断面序号，序号从 0 开始，不得大于等于断面数量。
//     * @param ID 传入字符串地址，执行后返回断面名称。
//     * @return
//     */
//    int API_SPRS_1D_TRANSECT_NAME(int index, byte[] ID);
//
//    /**
//     * 7.1 获取模拟信息
//     * @param cStartDateTime cStartDateTime，返回开始计算日期和时间。
//     * @param cEndDateTime cEndDateTime，返回结束计算日期和时间。
//     * @param tStep tStep，返回计算步长，单位为秒。
//     * @return
//     */
//    int API_SPRS_1D_SIMULATION_INFO(String cStartDateTime, String cEndDateTime, DoubleByReference tStep);
//
//    /**
//     * 修改模拟时间
//     * @param cStartDate cStartDate，开始计算日期，字符串，如“02/01/2023”
//     * @param cStartTime cStartTime，开始计算时间，字符串，如“0:00
//     * @param cEndDate  cEndDate，结束计算日期，字符串，如“02/01/2023”
//     * @param cEndTime cEndTime，结束计算时间，字符串，“2:00”
//     * @return
//     */
//    int API_SPRS_1D_SIMULATION_TIME(String cStartDate, String cStartTime, String cEndDate, String cEndTime);
//}
