package com.essence.tcp.netty;


import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.essence.tcp.schedul.ExplainUtil;
import com.essence.tcp.service.PumpService;
import com.essence.tcp.service.PumpServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Date;

/**
 * I/O数据读写处理类
 * cuirx
 */
@Component


public class BootNettyChannelInboundHandlerAdapter{
    @Resource
    private PumpService pumpService;

    /**
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg == null){return;}
        if(!(msg instanceof ByteBuf)){
            System.out.println(msg);
            return;
        }
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] dataType = new byte[byteBuf.readableBytes()];
        int readerIndex = byteBuf.readerIndex();
        byteBuf.getBytes(readerIndex, dataType);

        System.out.println("========收到源数据=="+Arrays.toString(dataType));
        FileUtil.writeString(Arrays.toString(dataType),new File("source.txt"),"UTF-8");

        String convert = ExplainUtil.bytes2hexDisplayHex(dataType);
        System.out.println("========收到源数据转16=="+convert);
        FileUtil.writeString(convert,new File("convert.txt"),"UTF-8");
       //心跳数据
       if (msg.equals("www.usr.cn" )  || dataType.length <= 50 ){
           System.out.println("心跳数据"+ExplainUtil.bytes2hexDisplayHex((byte[] ) msg));
       }else {
           System.out.println("开始刷新数据");
           pumpService = SpringUtil.getBean(PumpServiceImpl.class);
           byte[] data = dataType;
           String s = ExplainUtil.bytes2hexDisplayHex(data);
           System.out.println("========收到s=="+s);
           FileUtil.appendUtf8String(s+new Date(),new File("save.txt"));
           String channelId = ctx.channel().id().toString();
           pumpService.doExplainAndSave(s);

       }

    }



}