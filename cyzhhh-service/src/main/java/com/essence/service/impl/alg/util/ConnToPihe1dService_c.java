package com.essence.service.impl.alg.util;


import com.sun.jna.ptr.DoubleByReference;

public interface ConnToPihe1dService_c {
    Integer apiSprs1DOpen(String inpUrl);
    Integer apiSprs1DSimulationTime(String startTime, String endTime);

    /**
     * 修改模拟时间
     * @param cStartDate  "02/01/2023";
     * @param cStartTime "0:00";
     * @param cEndDate "02/01/2023";
     * @param cEndTime "4:00";
     * @return
     */
    Integer API_SPRS_1D_SIMULATION_TIME(String cStartDate, String cStartTime,String cEndDate, String cEndTime);
    Integer apiSprs1DSimulationInfo(String start, String end , DoubleByReference step);
    Integer apiSprs1DInitial();
    Integer apiSprs1DStep(DoubleByReference time);
    Integer API_SPRS_1D_FIXEDSTEP(DoubleByReference elapsedTime,double timeInterval);
    Integer apiSprs1DThreadNumber(Integer threadNumber);
    Integer apiSprs1DResult(double[] Elevation, double[] Depth, double[] Flow, double[] Velcity, double[] Area, double[] Width);
    Integer apiSprs1DClose();
    Integer apiSprs1DTimeseriesNumber();

    /**
     * 获取控制规则数量
     * @return
     */
    int API_SPRS_1D_CONTROL_NUMBER();

    /**
     * 获取控制规则名称
     * @param index
     * @param ID
     * @return
     */
    int API_SPRS_1D_CONTROL_NAME(int index, byte[] ID);

    /**
     * 修改控制规则
     * @param id
     * @param mode
     * @param newValue
     * @return
     */
    int API_SPRS_1D_CONTROL_REVISE(String id, int mode, double newValue);

    /**
     * 修改曲线 通过下标
     * @param index
     * @param mode
     * @param newValue
     * @return
     */
    int  API_SPRS_1D_CONTROL_REVISEBYINDEX(int index, int mode, double newValue);

    Integer apiSprs1DTimeseriesName(Integer index, byte[] timeseriesName);

    Integer API_SPRS_1D_TRANSECT_NUMBER();

    Integer API_SPRS_1D_TRANSECT_NAME(Integer idex,byte[] name);
//    Integer apiSprs1DTimeseriesAdd(String id, Integer mode, String cDate, String cTime, String cValue);
    Integer apiSprs1DTimeseriesAdd(String id, Integer mode, String cDate, String cTime, String cValue);

    Integer API_SPRS_1D_TIMESERIES_ADDBYINDEX(int index, int mode, String  cDate, String cTime, String cValue);
    static ConnToPihe1dService_c build(){
        return new ConnToPihe1dService_cImpl();
    }

    /**
     * 获取曲线数量
     * @return
     */
    int API_SPRS_1D_CURVE_NUMBER();

    /**
     * 获取可控曲线名称
     * @param index
     * @param ID
     * @return
     */
    int API_SPRS_1D_CURVE_NAME(int index, byte[] ID);

    int API_SPRS_1D_CURVE_ADD(byte[] id, int mode, String x, String y);

    /**
     * 修改泵站控制接口 通过id
     * @param index
     * @param mode
     * @param x
     * @param y
     * @return
     */
    int API_SPRS_1D_CURVE_ADDBYINDEX(int index, int mode, String x, String y);
}
