package com.essence.tcp.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class MyClientNetty {


//    public static void main(String[] args) throws Exception {
//
//        start("192.168.0.187",19902);
//
//    }


    public static void start(String host ,int port) throws Exception {
         EventLoopGroup group = new NioEventLoopGroup();
         try {
             Bootstrap b = new Bootstrap();
             b.group(group) // 注册线程池
                     .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                     .remoteAddress(new InetSocketAddress(host, port)) // 绑定连接端口和host信息
                     .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                   @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                                                 System.out.println("connected...");
                                                 ch.pipeline().addLast(new EchoClientHandler());
                                           }
                     });
                 System.out.println("created..");

                 ChannelFuture cf = b.connect(); // 异步连接服务器
                 System.out.println("connected..."); // 连接完成

//                 cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
//                 System.out.println("closed.."); // 关闭完成
             } finally {

             }
    }
}

