package com.essence.tcp.schedul;

import com.essence.tcp.dto.ReceivePumpDto;

import java.math.BigInteger;
import java.util.*;

import static cn.hutool.core.convert.Convert.toByte;

public class ExplainUtil {



    /**
     * 简洁写法 16进制字符串转成byte数组
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    public static byte[] hexStringToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) java.lang.Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    public byte[] getSendBytes(){
//        C5 FD
        byte[] body={03,03,00,00,00,32};
        String crc2 = CrcUtil.getCRC2(body);
        String end = crc2.substring(0, 2);
        String head = crc2.substring(2, 4);
        byte endByte = (byte) Integer.parseInt(end, 16);
        byte headByte = (byte) Integer.parseInt(head, 16);
        byte[] sendByte={03,03,00,00,00,32,headByte,endByte};

        return sendByte;
    }

    public static byte[] getOne(){
        byte[] body={03,03,00,00,00,100};
        String crc2 = CrcUtil.getCRC2(body);
        String end = crc2.substring(0, 2);
        String head = crc2.substring(2, 4);
        byte endByte = (byte) java.lang.Integer.parseInt(end, 16);
        byte headByte = (byte) java.lang.Integer.parseInt(head, 16);
        byte[] hzh={03,03,00,00,00,100,headByte,endByte};
        return hzh;
    }

    /**
     * 获取 需要发送站点的请求数据
     * @return
     */
    public static List<byte[]> getSendBytesList(){
        List<byte[]> res = new ArrayList<>();
        byte[] body={03,03,00,00,00,100};
        String crc2 = CrcUtil.getCRC2(body);
        String end = crc2.substring(0, 2);
        String head = crc2.substring(2, 4);
        byte endByte = (byte) java.lang.Integer.parseInt(end, 16);
        byte headByte = (byte) java.lang.Integer.parseInt(head, 16);
        byte[] hzh={03,03,00,00,00,100,headByte,endByte};
        res.add(hzh);

        byte[] body2={04,03,00,00,00,100};
        String crc22 = CrcUtil.getCRC2(body2);
        String end2 = crc22.substring(0, 2);
        String head2 = crc22.substring(2, 4);
        byte endByte2 = (byte) java.lang.Integer.parseInt(end2, 16);
        byte headByte2 = (byte) java.lang.Integer.parseInt(head2, 16);
        byte[] mjw={04,03,00,00,00,100,headByte2,endByte2};
        res.add(mjw);
        byte[] body3={05,03,00,00 ,00,100};
        String crc3 = CrcUtil.getCRC2(body3);
        String end3 = crc3.substring(0, 2);
        String head3 = crc3.substring(2, 4);
        byte endByte3 = (byte) java.lang.Integer.parseInt(end3, 16);
        byte headByte3 = (byte) java.lang.Integer.parseInt(head3, 16);
        byte[] ft={05,03,00,00,00,100,headByte3,endByte3};
        res.add(ft);

        byte[] body4={06,03,00,00 ,00,100};
        String crc4 = CrcUtil.getCRC2(body4);
        String end4 = crc4.substring(0, 2);
        String head4 = crc4.substring(2, 4);
        byte endByte4 = (byte) java.lang.Integer.parseInt(end4, 16);
        byte headByte4 = (byte) java.lang.Integer.parseInt(head4, 16);
        byte[] jxq={06,03,00,00,00,100,headByte4,endByte4};
        res.add(jxq);
        byte[] body5={07,03,00,00 ,00,100};
        String crc5 = CrcUtil.getCRC2(body5);
        String end5 = crc5.substring(0, 2);
        String head5 = crc5.substring(2, 4);
        byte endByte5 = (byte) java.lang.Integer.parseInt(end5, 16);
        byte headByte5 = (byte) java.lang.Integer.parseInt(head5, 16);
        byte[] lgy={07,03,00,00,00,100,headByte5,endByte5};
        res.add(lgy);
        return res;
    }


    public static  List<byte[]> getOpenOrCloseData(String deviceAddr,int pNum,Integer status){
        List<String> deviceIdThree = new ArrayList<>();
        deviceIdThree.add("04");
        deviceIdThree.add("05");
        deviceIdThree.add("06");
        List<String> deviceIdTwo = new ArrayList<>();
        deviceIdTwo.add("03");
        deviceIdTwo.add("07");

        if (deviceIdThree.contains(deviceAddr)){
            List<byte[]> threePump = getThreePump(deviceAddr, pNum, status);
            return threePump;
        }else if (deviceIdTwo.contains(deviceAddr)){
            List<byte[]> twoPump = getTwoPump(deviceAddr, pNum, status);
            return twoPump;
        }
        return null;
    }

    /**
     * 两个泵站
     * @return
     */
    public static List<byte[]> getTwoPump(String deviceAddr,int pNum,Integer status){
        List<byte[]> res = new ArrayList<>();

        String str="";
        String ovr="";
        //p1 开          二进制 左 高位 右 低位
        if (pNum == 1 && status ==1){
            //          8->9
            str="0000000100000000";
            ovr="0000000000000000";
        }
        // p2开
        else if (pNum == 2 && status ==1){
            //          9->10
            str="0000001000000000";
            ovr="0000000000000000";
        }
        // p1 关闭
        else if (pNum == 1 && status ==0){
            //            10-> 11
            str="0000010000000000";
            ovr="0000000000000000";
        }
        //p2 关闭
        else if (pNum == 2 && status ==0){
            //             11-> 12
            str="0000100000000000";
            ovr="0000000000000000";
        }
        byte[] start = getResFromBody(deviceAddr, str);
        byte[] reStart = getResFromBody(deviceAddr, ovr);
        res.add(start);
        res.add(reStart);
        return res;
    }

    /**
     * 三个泵站
     * @return
     */
    public static List<byte[]> getThreePump(String deviceAddr,int pNum,Integer status){
        List<byte[]> res = new ArrayList<>();


        String str="";
        String ovr="";
        //p1 开          二进制 左 高位 右 低位
        if (pNum == 1 && status ==1){
            //          8->9
            str="0000000100000000";
            ovr="0000000000000000";

        }
        // p2开
        else if (pNum == 2 && status ==1){
            //          9->10
            str="0000001000000000";
            ovr="0000000000000000";
        }
        // p3开
        else if (pNum == 3 && status ==1){
            //         10-> 11
            str="0000010000000000";
            ovr="0000000000000000";
        }
        // p1 关闭
        else if (pNum == 1 && status ==0){
            //             11->12
            str="0000100000000000";
            ovr="0000000000000000";
        }
        //p2 关闭
        else if (pNum == 2 && status ==0){
            //              12->13
            str="0001000000000000";
            ovr="0000000000000000";
        }
        // p3 关闭
        else if (pNum == 3 && status ==0){
            //            13-> 14
            str="0010000000000000";
            ovr="0000000000000000";
        }
        byte[] start = getResFromBody(deviceAddr, str);
        byte[] reStart = getResFromBody(deviceAddr, ovr);
        res.add(start);
        res.add(reStart);
        return res;
    }

    public static byte[] getResFromBody(String deviceAddr,String str){
        //16 进制 的数据
        String start = get2to16(str);
        if (start.length() <4){
            int num = 4 - start.length();
            String prefix = "";
            for (int i = 0; i < num; i++) {
                prefix=prefix+"0";
            }
            start = prefix + start;
        }
        String addr = "00"+get10to16(89);
        String startBody = deviceAddr+"06"+addr+start;
        //将整体 转换位 10 进制
        List<String> data = new ArrayList<>();
        for (int i = 0; i < startBody.length(); i = i+2) {
            String substring = startBody.substring(i, i + 2);
            data.add(substring);
        }
        byte[] bytes = new byte[6];
        for (int i = 0; i < data.size(); i++) {
            String s = data.get(i);
            String s1 = explain16to10Bite(s);
            bytes[i] = new Byte(s1)  ;
        }
        System.out.println(Arrays.asList(bytes).toString());
        String crc22 = CrcUtil.getCRC2(bytes);
        String end2 = crc22.substring(0, 2);
        String head2 = crc22.substring(2, 4);
        byte endByte2 = (byte) java.lang.Integer.parseInt(end2, 16);
        byte headByte2 = (byte) java.lang.Integer.parseInt(head2, 16);
        byte[] sendStartByte = new byte[8];
        for (int i = 0; i < sendStartByte.length; i++) {
            if (i == 6 || i == 7){
                sendStartByte [6] = headByte2;
                sendStartByte [7] = endByte2;
            }else {
                sendStartByte[i] = bytes[i];
            }
        }
        return sendStartByte;
    }

    //                       1           2           3           4           5           6           7           8           9          10          11          12          13          14          15          16          17          18          19          20          21          22          23          24          25          26          27          28          29          30          31          32          33          34          35          36          37          38          39          40          41          42          43          44          45          46             47          48          49    50
//    03 03 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 64 42 0C 00 00 41 84 00 00 41 30 00 00 43 B1 99 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 41 58 CC CD 40 C6 66 66 41 B4 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 46 00 00 00 00 47 00 00 00 48 00 00 00 49 00 02 00 00 38 B5

    /**
     * 解析返回的数据
     * @param msg
     */
    public static ReceivePumpDto explain(String msg){
        ReceivePumpDto receivePumpDto = new ReceivePumpDto();
        Map<Integer,String> dateMap = new HashMap<>();
        String message = msg.replace(" ", "");
        //机器码 标识 具体的设备
        String deviceCode = message.substring(0, 2);
        //去掉 头部
        message = message.substring(6);
        //去掉尾部
        message = message.substring(0,message.length()-4);
        int m = 0;
        int position = 1;
        for (int i = 0; i < message.length(); i++) {
            if (position == 25){
                //第 50 位置的时候 取8个 作为状态数据
                String substring = message.substring(m,m+8);
                dateMap.put(position,substring);
//                    m = m + 4;
//                    position += 1;
                break;
            }
            // 刚开始取 8 作为浮点数据
            if ((i+1) % 8 == 0){

                String substring = message.substring(m, i + 1);
                dateMap.put(position,substring);
                m = m + 8;
                position += 1;
            }

        }
        receivePumpDto.setData(dateMap);
        receivePumpDto.setDeviceAddr(deviceCode);
        return receivePumpDto;
    }


//    public static void main(String[] args) {
//        String str = "06 03 C8 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 9A 2B 61 00 00 00 00 00 00 00 00 00 00 00 00 43 6C B3 33 43 6C 66 66 43 6C 4C CD 00 00 00 00 3E CC CC CD 3E CC CC CD 42 C2 B3 33 45 2E 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3F 19 99 9A 41 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 42 59";
//        ReceivePumpDto explain = explain(str);
//        System.out.println("");
//    }

    public static String get2to16(String str){
//        String i="1001";
        System.out.println(java.lang.Integer.toHexString(java.lang.Integer.parseInt(str, 2)));//二进制转十六进制
        String s = java.lang.Integer.toHexString(java.lang.Integer.parseInt(str, 2));
        return s;
    }

    public static String  get10to16(Integer i){
//        String i="1001";
        System.out.println(Integer.toHexString(i));//十进制转十六进制
        String s = Integer.toHexString(i);
        return s;
    }


    /**
     * 将16进制数据 转换为 16 位 的 2进制字符串
     * @param msg
     * @return
     */
    public static String explain16to2Bite(String msg){
        //16进制转10进制
        BigInteger sint = new BigInteger(msg, 16);
        //10进制转2进制
        String second = sint.toString(2);
        //补充前缀0 一共16 位置
        if (second.length() <16){
            StringBuffer buffer = new StringBuffer();
            Integer num = 16 - second.length();
            for (Integer integer = num; integer > 0; integer--) {
                buffer.append("0");
            }
            String prefix = buffer.toString();
            second = prefix+ second;
            System.out.println(second);
        }

        return second;
    }

    /**
     * 16 静止转换10 进制 整数
     * @param msg
     * @return
     */
    public static String explain16to10Bite(String msg){
        //16进制转10进制
        BigInteger sint = new BigInteger(msg, 16);


        return sint.toString();
    }

    /**
     * 16 静止转换10进制 IEE754 浮点数
     * @param msg
     * @return
     */
    public static String explain16to10IEE754(String msg){
        //16进制转10进制
        int ieee754Int = java.lang.Integer.parseInt(msg, 16);
        Float realValue = null;
        try {
            realValue = Float.intBitsToFloat(ieee754Int);
        } catch (Exception e) {
            realValue = 0f;
        }

        return realValue.toString();
    }

    public static String bytes2hexDisplayHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp;
//        sb.append("[");
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = java.lang.Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;//只有一位的前面补个0
            }
            sb.append(tmp).append(" ");//每个字节用空格断开
        }
        sb.delete(sb.length() - 1, sb.length());//删除最后一个字节后面对于的空格
//        sb.append("]");
        return sb.toString();
    }
//
//    public static void main(String[] args) {
////        byte[] msgBytes = {0x03, 0x03, 0x00, 0x00, 0x00, 0x01};
////        String crc2 = CrcUtil.getCRC2(msgBytes);
////        String end = crc2.substring(0, 2);
////        String head = crc2.substring(2, 4);
////        byte endByte = (byte) Integer.parseInt(end, 16);
////        byte headByte = (byte) Integer.parseInt(head, 16);
////        byte[] sendBytes = {0x03, 0x03, 0x00, 0x00, 0x00, 0x01,headByte, endByte};
//
////        String str =    "03 03 64 00 01 00 00 00 0B 00 00 00 00 00 00 00 01 00 01 00 01 00 00 00 01 00 00 00 00 00 00 47 82 35 4D 41 08 00 00 42 C7 CC CD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 A1 B9";
////        explain(str);
//
////        explain16to2Bite("E8");
//
//        String s = explain16to10IEE754("41B4CCCC");
//        System.out.println(s);
//
//    }

    public static void main(String[] args) {
        List<byte[]> sendBytesList = getSendBytesList();

        sendBytesList.forEach(item->{
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < item.length; i++) {
                byte b = item[i];
                String tmp = Integer.toHexString(0xFF & b);
                if (tmp.length() == 1) {
                    tmp = "0" + tmp;//只有一位的前面补个0
                }
                stringBuilder.append(tmp+" ");

            }
            System.out.println(stringBuilder);
        });

    }


}
