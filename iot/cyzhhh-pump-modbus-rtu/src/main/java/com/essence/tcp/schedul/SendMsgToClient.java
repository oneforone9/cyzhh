package com.essence.tcp.schedul;

import cn.hutool.core.io.FileUtil;
import com.essence.tcp.nettyServer.core.NettyServer;
import io.netty.buffer.Unpooled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@EnableScheduling
@Component
public class SendMsgToClient {


    @Scheduled(cron = "0 0/10 * * * ?")
    public void doExecute() throws InterruptedException {
        System.out.println("预备发送消息");
        List<byte[]> sendBytesList = ExplainUtil.getSendBytesList();
//        NettyServer.sendMsg(sendBytesList.get(3));
        for (byte[] msgBytes : sendBytesList) {
            NettyServer.sendMsg(msgBytes);
        }

//        for (String key : BootNettyChannelCache.channelMapCache.keySet()) {
//            BootNettyChannel bootNettyChannel = BootNettyChannelCache.channelMapCache.get(key);
//            Channel channel = bootNettyChannel.getChannel();
//            String code = bootNettyChannel.getCode();
////            03  03  00  00  00  01  85  E8
////            String msg = "03 03 00 00 00 01 85 E8";
////            byte[] msgBytes = {0x03, 0x03, 0x00, 0x00, 0x00, 0x01,(byte) 0x85, (byte) 0xE8};
////            byte[] msgBytes = {0x03, 0x03, 0x00, 0x00, 0x00, 0x01};
////            String crc2 = CrcUtil.getCRC2(msgBytes);
//
//            List<byte[]> sendBytesList = ExplainUtil.getSendBytesList();
//            for (byte[] msgBytes : sendBytesList) {
//                String s10 = Arrays.toString(msgBytes);
//                System.out.println("发送的数据是 10========>"+s10);
//                String s = ExplainUtil.bytes2hexDisplayHex(msgBytes);
//                System.out.println("发送的数据是 16========>"+s);
//                FileUtil.appendUtf8String("发送的数据是 10========>"+s10+"@@"+"发送的数据是 16========>"+s+"!!"+FileUtil.getLineSeparator(), new File("modbus.txt"));
//                //有时候可能会有网络延迟所以 发送 3 次
//                for (int i = 0; i < 100; i++) {
//
//                    channel.writeAndFlush(Unpooled.buffer().writeBytes(msgBytes));
//                }
//
//                System.out.println("server 定时任务:服务向客户端推送 ---------------->"+ s);
//                Thread.sleep(5000);
//            }
//
//        }

    }


//    public static void main(String[] args) {
//        List<byte[]> sendBytesList = ExplainUtil.getSendBytesList();
//        for (byte[] msgBytes : sendBytesList) {
//                String s10 = Arrays.toString(msgBytes);
//                System.out.println("发送的数据是 10========>"+s10);
//                String s = ExplainUtil.bytes2hexDisplayHex(msgBytes);
//                System.out.println("发送的数据是 16========>"+s);
//
//            }
//    }





}
