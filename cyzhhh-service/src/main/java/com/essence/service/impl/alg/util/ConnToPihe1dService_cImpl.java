package com.essence.service.impl.alg.util;

import com.sun.jna.ptr.DoubleByReference;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.essence.service.impl.alg.util.JnaUtil.invoke;


@Service
public class ConnToPihe1dService_cImpl implements ConnToPihe1dService_c {
    //打开文件
    @Override
    public Integer apiSprs1DOpen(String inpUrl) {
        int resultCode = invoke("API_SPRS_1D_OPEN",Integer.class,inpUrl);
        return resultCode;
    }
    //自定义模拟时间
    @Override
    public Integer apiSprs1DSimulationTime(String startTime, String endTime) {
        String[] startParts = startTime.split(" ");
        String[] endParts = endTime.split(" ");
        int resultCode = invoke("API_SPRS_1D_SIMULATION_TIME",Integer.class,startParts[0],startParts[1],endParts[0],endParts[1]);
        return resultCode;
    }
    //获取模拟设置
    @Override
    public Integer apiSprs1DSimulationInfo(String start, String end, DoubleByReference step) {
        int resultCode = invoke("API_SPRS_1D_SIMULATION_INFO",Integer.class,start,end,step);
        return resultCode;
    }

    @Override
    public Integer API_SPRS_1D_SIMULATION_TIME(String cStartDate, String cStartTime,String cEndDate, String cEndTime){
        int resultCode = invoke("API_SPRS_1D_SIMULATION_TIME",Integer.class, cStartDate,  cStartTime, cEndDate,  cEndTime);
        return resultCode;
    }

    //初始化
    @Override
    public Integer apiSprs1DInitial() {
        int resultCode = invoke("API_SPRS_1D_INIITIAL",Integer.class,null);
        return resultCode;
    }
    //
    @Override
    public Integer apiSprs1DStep(DoubleByReference time){
        int resultCode = invoke("API_SPRS_1D_STEP",Integer.class,time);
        return resultCode;
    }
    @Override
    public Integer API_SPRS_1D_FIXEDSTEP(DoubleByReference elapsedTime,double timeInterval){
        int resultCode = invoke("API_SPRS_1D_FIXEDSTEP",Integer.class,elapsedTime,timeInterval);
        return resultCode;
    }
    //获取线程数
    @Override
    public Integer apiSprs1DThreadNumber(Integer threadNumber) {
        int resultCode = invoke("API_SPRS_1D_THREAD_NUMBER",Integer.class,threadNumber);
        return resultCode;
    }

    //获取单步结果
    @Override
    public Integer apiSprs1DResult(double[] Elevation, double[] Depth, double[] Flow, double[] Velcity, double[] Area, double[] Width) {
        Integer resultCode = invoke("API_SPRS_1D_RESULT",Integer.class,Elevation,Depth,Flow,Velcity,Area,Width);
        return resultCode;
    }
    //关闭资源
    @Override
    public Integer apiSprs1DClose() {
        Integer resultCode = invoke("API_SPRS_1D_CLOSE",Integer.class,null);
        return resultCode;
    }
    //获得时间序列的数量
    @Override
    public Integer apiSprs1DTimeseriesNumber() {
        Integer timeseriesNumber = invoke("API_SPRS_1D_TIMESERIES_NUMBER",Integer.class,null);
        return timeseriesNumber;
    }
    //获得时间序列的名字
    @Override
    public Integer apiSprs1DTimeseriesName(Integer index, byte[] timeseriesName) {
        Integer resultCode = invoke("API_SPRS_1D_TIMESERIES_NAME",Integer.class,index,timeseriesName);
        return resultCode;
    }
    @Override
    public Integer API_SPRS_1D_TRANSECT_NUMBER(){
        Integer resultCode = invoke("API_SPRS_1D_TRANSECT_NUMBER",Integer.class);
        return resultCode;
    }
    @Override
    public Integer API_SPRS_1D_TRANSECT_NAME(Integer index,byte[] name){
        Integer resultCode = invoke("API_SPRS_1D_TRANSECT_NAME",Integer.class,index,name);
        return resultCode;
    }

    //修改时间序列
    @Override
    public Integer apiSprs1DTimeseriesAdd(String id, Integer mode, String cDate, String cTime, String cValue) {

            int resultCode = invoke("API_SPRS_1D_TIMESERIES_ADD", Integer.class,utf8ToGbk(id),mode,utf8ToGbk(cDate),utf8ToGbk(cTime),utf8ToGbk(cValue));

        return resultCode;

    }

    @Override
    public Integer API_SPRS_1D_TIMESERIES_ADDBYINDEX(int index, int mode, String cDate, String cTime,String cValue){
        Integer resultCode = invoke("API_SPRS_1D_TIMESERIES_ADDBYINDEX",Integer.class,index,mode,cDate,cTime,cValue);
        return resultCode;
    }

    /**
     * 获取控制规则数量
     * @return
     */
    public int API_SPRS_1D_CONTROL_NUMBER(){
        Integer resultCode = invoke("API_SPRS_1D_CONTROL_NUMBER",Integer.class);
        return resultCode;
    }

    /**
     * 获取控制规则名称
     * @param index
     * @param ID
     * @return
     */
    public int API_SPRS_1D_CONTROL_NAME(int index, byte[] ID){
        Integer resultCode = invoke("API_SPRS_1D_CONTROL_NAME",Integer.class,index,ID);
        return resultCode;
    }

    /**
     * 修改控制规则
     * @param id
     * @param mode
     * @param newValue
     * @return
     */
    public int API_SPRS_1D_CONTROL_REVISE(String id, int mode, double newValue){
        Integer resultCode = invoke("API_SPRS_1D_CONTROL_REVISE",Integer.class,id,mode,newValue);
        return resultCode;
    }

    public int  API_SPRS_1D_CONTROL_REVISEBYINDEX(int index, int mode, double newValue){
        Integer resultCode = invoke("API_SPRS_1D_CONTROL_REVISEBYINDEX",Integer.class,index ,mode,newValue);
        return resultCode;
    }

    public int API_SPRS_1D_CURVE_NUMBER(){
        Integer resultCode = invoke("API_SPRS_1D_CURVE_NUMBER",Integer.class);
        return resultCode;
    }

    public int API_SPRS_1D_CURVE_NAME(int index, byte[] ID){
        Integer resultCode = invoke("API_SPRS_1D_CURVE_NAME",Integer.class,index,ID);
        return resultCode;
    }

    @Override
    public int API_SPRS_1D_CURVE_ADD(byte[] id, int mode, String x, String y) {
        Integer resultCode = invoke("API_SPRS_1D_CURVE_ADD",Integer.class,id ,mode,x  ,y);
        return resultCode;
    }

    @Override
    public int API_SPRS_1D_CURVE_ADDBYINDEX(int index, int mode, String x, String y){
        Integer resultCode = invoke("API_SPRS_1D_CURVE_ADDBYINDEX",Integer.class,index ,mode,x ,y);
        return resultCode;
    }

    private static byte[] utf8ToGbk(String s){
        try {
            byte[] gbks = s.getBytes("GBK");
//            System.out.println(new String(gbks, Charset.forName("GBK")).trim());

            return gbks;
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }

    }

}
