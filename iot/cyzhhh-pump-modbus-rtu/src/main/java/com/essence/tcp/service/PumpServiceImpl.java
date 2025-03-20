package com.essence.tcp.service;

import cn.hutool.core.io.FileUtil;
import com.essence.tcp.entity.PumpDataEntity;
import com.essence.tcp.nettyServer.core.NettyServer;
import com.essence.tcp.schedul.ExplainUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;
import com.essence.tcp.dao.PumpDataMapper;
import com.essence.tcp.dto.ReceivePumpDto;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.function.Supplier;

@Service
public class PumpServiceImpl implements PumpService {
    @Resource
    private PumpDataMapper pumpDataMapper;

//                       1           2           3           4           5           6           7           8           9          10          11          12          13          14          15          16          17          18          19          20          21          22          23          24          25          26          27          28          29          30          31          32          33          34          35          36          37          38          39          40          41          42          43          44          45          46             47          48          49    50
//    03 03 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 46 00 00 00 00 47 00 00 00 48 00 00 00 49 00 02 00 00 38 B5
//    07 03 c8 00 00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 40 51 68 60 41 70 00 00 00 00 00 00 00 00 00 00 43 75 99 9a 43 83 8c cd 43 75 4c cd 00 00 00 00 3e cc cc cd 3e cc cc cd 43 42 e6 66 44 06 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3f 19 99 9a 41 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 b6
//    06 03 C8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 9A 2B 61 00 00 00 00 00 00 00 00 00 00 00 00 43 6C B3 33 43 6C 66 66 43 6C 4C CD 00 00 00 00 3E CC CC CD 3E CC CC CD 42 C2 B3 33 45 2E 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 19 99 9A 41 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 42 59";

    @Override
    public void doExplainAndSave(String source) {
        List<String> deviceIdThree = new ArrayList<>();
        deviceIdThree.add("04");
        deviceIdThree.add("05");
        deviceIdThree.add("06");
        List<String> deviceIdTwo = new ArrayList<>();
        deviceIdTwo.add("03");
        deviceIdTwo.add("07");


        ReceivePumpDto explain = ExplainUtil.explain(source);
        Map<Integer, String> data = explain.getData();
        System.out.println("解析到的数据::"+explain);
        String deviceAddr = explain.getDeviceAddr();
        FileUtil.appendUtf8String(explain.toString()+new Date(),new File("explainsave.txt"));
        //如果有三个泵站
        if (deviceIdThree.contains(deviceAddr)){
            PumpDataEntity pumpDataEntity = new PumpDataEntity();
            pumpDataEntity.setDeviceAddr(deviceAddr);
            String one = data.get(1);
            String one10 = ExplainUtil.explain16to10IEE754(one);
            pumpDataEntity.setP1FeedBack(one10);


            String third = data.get(2);
            String third10 = ExplainUtil.explain16to10IEE754(third);
            pumpDataEntity.setP2FeedBack(third10);

            String five = data.get(3);
            String five10 = null;
            try {
                five10 = ExplainUtil.explain16to10IEE754(five);
            } catch (Exception e) {
                five10 = "0";
            }
            pumpDataEntity.setFlowRate(five10);

            String seven = data.get(4);
            String seven10 = ExplainUtil.explain16to10IEE754(seven);
            pumpDataEntity.setPressure(seven10);

            String nine = data.get(5);
            String nine10 = ExplainUtil.explain16to10IEE754(nine);
            pumpDataEntity.setYPosition(nine10);

            String eleven = data.get(6);
            String eleven10 = ExplainUtil.explain16to10IEE754(eleven);
            pumpDataEntity.setP1CountTime(eleven10);

            String oneThird = data.get(7);
            String oneThird10 = ExplainUtil.explain16to10IEE754(oneThird);
            pumpDataEntity.setP2CountTime(oneThird10);

            String oneFive = data.get(8);
            String oneFive10 = ExplainUtil.explain16to10IEE754(oneFive);
            pumpDataEntity.setAVoltage(oneFive10);

            String oneSeven = data.get(9);
            String oneSeven10 = ExplainUtil.explain16to10IEE754(oneSeven);
            pumpDataEntity.setBVoltage(oneSeven10);


            String oneNine = data.get(10);
            String oneNine10 = null;
            try {
                oneNine10 = ExplainUtil.explain16to10IEE754(oneNine);
            } catch (Exception e) {

                oneNine10 = "0";
            }
            pumpDataEntity.setCVoltage(oneNine10);

            String twoOne = data.get(11);
            String twoOne10 = ExplainUtil.explain16to10IEE754(twoOne);
            pumpDataEntity.setAElectric(twoOne10);

            String twoThird = data.get(12);
            String twoThird10 = ExplainUtil.explain16to10IEE754(twoThird);
            pumpDataEntity.setBElectric(twoThird10);

            String twoFive = data.get(13);
            String twoFive10 = ExplainUtil.explain16to10IEE754(twoFive);
            pumpDataEntity.setCElectric(twoFive10);

            String twoSeven = data.get(14);
            String twoSeven10 = ExplainUtil.explain16to10IEE754(twoSeven);
            pumpDataEntity.setElectric(twoSeven10);

            String twoNine = data.get(15);
            String twoNine10 = ExplainUtil.explain16to10IEE754(twoNine);
            pumpDataEntity.setAddFlowRate(twoNine10);

            String ele = data.get(16);
            String eleData = ExplainUtil.explain16to10IEE754(ele);
            pumpDataEntity.setAddFlowRate(eleData);

            String addRate = data.get(17);
            String addRateData = ExplainUtil.explain16to10IEE754(addRate);
            pumpDataEntity.setAddFlowRate(addRateData);

            String fifty = data.get(25);
            String fifty10 = ExplainUtil.explain16to2Bite(fifty);
            String[] split = fifty10.split("");
            String dot0 = split[0];
            String dot1 = split[1];
            String dot2 = split[2];

            String dot8 = split[8];
            String dot9 = split[9];
            String dot10 = split[10];
            String dot11 = split[11];
            String dot12 = split[12];
            String dot13 = split[13];
            String dot14 = split[14];
            String dot15 = split[15];
            pumpDataEntity.setLiquidHigh(dot8);
            pumpDataEntity.setLiquidLow(dot9);
            pumpDataEntity.setP1Remote(dot10);
            pumpDataEntity.setP1Run(dot11);
            pumpDataEntity.setP1Hitch(dot12);

            pumpDataEntity.setP2Remote(dot13);
            pumpDataEntity.setP2Run(dot14);

            pumpDataEntity.setP2Hitch(dot15);
            pumpDataEntity.setP3Run(dot0);
            pumpDataEntity.setP3Remote(dot1);
            pumpDataEntity.setP3Hitch(dot2);
            pumpDataEntity.setDate(new Date());
            int insert = pumpDataMapper.insert(pumpDataEntity);
        }
        //如果是两个泵
        else if (deviceIdTwo.contains(deviceAddr)){
            PumpDataEntity pumpDataEntity = new PumpDataEntity();
            pumpDataEntity.setDeviceAddr(deviceAddr);
            String one = data.get(1);
            String one10 = ExplainUtil.explain16to10IEE754(one);
            pumpDataEntity.setP1FeedBack(one10);


            String third = data.get(2);
            String third10 = ExplainUtil.explain16to10IEE754(third);
            pumpDataEntity.setP2FeedBack(third10);

            String five = data.get(3);
            String five10 = null;
            try {
                five10 = ExplainUtil.explain16to10IEE754(five);
            } catch (Exception e) {
                five10 = "0";
            }
            pumpDataEntity.setFlowRate(five10);

            String seven = data.get(4);
            String seven10 = ExplainUtil.explain16to10IEE754(seven);
            pumpDataEntity.setPressure(seven10);

            String nine = data.get(5);
            String nine10 = safeGet(()->ExplainUtil.explain16to10IEE754(nine),"0");
            pumpDataEntity.setYPosition(nine10);

            String eleven = data.get(6);
            String eleven10 = ExplainUtil.explain16to10IEE754(eleven);
            pumpDataEntity.setP1CountTime(eleven10);

            String oneThird = data.get(7);
            String oneThird10 = ExplainUtil.explain16to10IEE754(oneThird);
            pumpDataEntity.setP2CountTime(oneThird10);

            String oneFive = data.get(8);
            String oneFive10 = ExplainUtil.explain16to10IEE754(oneFive);
            pumpDataEntity.setAVoltage(oneFive10);

            String oneSeven = data.get(9);
            String oneSeven10 = ExplainUtil.explain16to10IEE754(oneSeven);
            pumpDataEntity.setBVoltage(oneSeven10);


            String oneNine = data.get(10);
            String oneNine10 = null;
            try {
                oneNine10 = ExplainUtil.explain16to10IEE754(oneNine);
            } catch (Exception e) {
                oneNine10 = "0";
            }
            pumpDataEntity.setCVoltage(oneNine10);

            String twoOne = data.get(11);
            String twoOne10 = null;
            try {
                twoOne10 = ExplainUtil.explain16to10IEE754(twoOne);
            } catch (Exception e) {
                twoOne10 = "0";
            }
            pumpDataEntity.setAElectric(twoOne10);

            String twoThird = data.get(12);
            String twoThird10 = ExplainUtil.explain16to10IEE754(twoThird);
            pumpDataEntity.setBElectric(twoThird10);

            String twoFive = data.get(13);
            String twoFive10 = ExplainUtil.explain16to10IEE754(twoFive);
            pumpDataEntity.setCElectric(twoFive10);

            String twoSeven = data.get(14);
            String twoSeven10 = ExplainUtil.explain16to10IEE754(twoSeven);
            pumpDataEntity.setElectric(twoSeven10);

            String twoNine = data.get(15);
            String twoNine10 = ExplainUtil.explain16to10IEE754(twoNine);
            pumpDataEntity.setAddFlowRate(twoNine10);

            String ele = data.get(16);
            String eleData = ExplainUtil.explain16to10IEE754(ele);
            pumpDataEntity.setAddFlowRate(eleData);

            String addRate = data.get(17);
            String addRateData = ExplainUtil.explain16to10IEE754(addRate);
            pumpDataEntity.setAddFlowRate(addRateData);

            String fifty = data.get(25);
            String fifty10 = ExplainUtil.explain16to2Bite(fifty);
            String[] split = fifty10.split("");
            String dot0 = split[0];
            String dot1 = split[1];
            String dot2 = split[2];

            String dot8 = split[8];
            String dot9 = split[9];
            String dot10 = split[10];
            String dot11 = split[11];
            String dot12 = split[12];
            String dot13 = split[13];
            String dot14 = split[14];
            String dot15 = split[15];
            pumpDataEntity.setLiquidHigh(dot8);
            pumpDataEntity.setLiquidLow(dot9);
            pumpDataEntity.setP1Remote(dot10);
            pumpDataEntity.setP1Run(dot11);
            pumpDataEntity.setP1Hitch(dot12);

            pumpDataEntity.setP2Remote(dot13);
            pumpDataEntity.setP2Run(dot14);

            pumpDataEntity.setP2Hitch(dot15);
            pumpDataEntity.setDate(new Date());
            int insert = pumpDataMapper.insert(pumpDataEntity);
        }

    }

    @Override
    public void pumpOpenOrClose(String deviceAddr, Integer pNum, Integer status) throws InterruptedException {

            List<byte[]> ListData = ExplainUtil.getOpenOrCloseData(deviceAddr,pNum, status);
            for (byte[] openOrCloseData : ListData) {
                NettyServer.sendMsg(openOrCloseData);

            }


    }
    public  static <T> T safeGet(Supplier<T> supplier,T defaultValue){
        T t;
        try {
            t =supplier.get();
        }catch (Exception e){
            t  = defaultValue;
        }
        return t;
    }

    /**
     * 0 关 1 开
     * 07 已经测试 成功 开 8  关 11
     * @param args
     */
//    public static void main(String[] args) {
////        List<byte[]> ListData = ExplainUtil.getOpenOrCloseData("07", 1, 1);
//        List<byte[]> end = ExplainUtil.getOpenOrCloseData("04", 3, 0);
//        for (byte[] openOrCloseData : end) {
//            String s10 = Arrays.toString(openOrCloseData);
//            System.out.println("发送的开关状态数据是 10========>"+s10);
//            String s = ExplainUtil.bytes2hexDisplayHex(openOrCloseData);
//            System.out.println("发送的开关状态数据是 16========>"+s);
//
//        }
//    }

//                       1           2           3           4           5           6           7           8           9          10          11          12          13          14          15          16          17          18          19          20          21          22          23          24          25          26          27          28          29          30          31          32          33          34          35          36          37          38          39          40          41          42          43          44          45          46             47          48          49    50
//    03 03 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 46 00 00 00 00 47 00 00 00 48 00 00 00 49 00 02 00 00 38 B5
//    真实数据 :
//    03 03 C8 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 5F 1B
//
//    06 03 C8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 9A 2B 61 00 00 00 00 00 00 00 00 00 00 00 00 43 6C B3 33 43 6C 66 66 43 6C 4C CD 00 00 00 00 3E CC CC CD 3E CC CC CD 42 C2 B3 33 45 2E 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 19 99 9A 41 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 42 59
//
}
