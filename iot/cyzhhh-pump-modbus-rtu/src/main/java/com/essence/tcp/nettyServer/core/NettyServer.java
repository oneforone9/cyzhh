package com.essence.tcp.nettyServer.core;

import com.essence.tcp.netty.BootNettyChannelInboundHandlerAdapter;
import com.essence.tcp.nettyServer.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;


public class NettyServer {

    private static final Map<String, SocketChannel> chMap = new ConcurrentHashMap<>();
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void init() {
        BootNettyChannelInboundHandlerAdapter bootNettyChannelInboundHandlerAdapter = new BootNettyChannelInboundHandlerAdapter();
        init(bootNettyChannelInboundHandlerAdapter::channelRead);
    }


    public static void init(BiConsumer<ChannelHandlerContext, ByteBuf> biConsumer) {
        EventLoopGroup selector = new NioEventLoopGroup(2);
        EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(selector, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(NettyConfig.SERVER_HOST, NettyConfig.SERVER_PORT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            String chId = socketChannel.id().toString();
                            System.out.println("createSocket:" + chId);
                            chMap.put(chId, socketChannel);

                            socketChannel.closeFuture().addListener((ChannelFutureListener) future -> {
                                ChannelId id = future.channel().id();
                                chMap.remove(id);
                                System.out.println("closeSocket:" + id);
                                future.channel().close();
                            });
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof ByteBuf) {
                                        ByteBuf byteBuf = (ByteBuf) msg;
//                                        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
                                        biConsumer.accept(ctx, byteBuf);
                                    }
                                }

                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
                                    System.err.println(throwable.getMessage());

                                    ctx.close();
                                }
                            });
                        }
                    });
            try {
                ChannelFuture f = serverBootstrap.bind().sync();
                countDownLatch.countDown();
                Channel channel = f.channel();
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } finally {
            try {
                selector.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ;
            try {

                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized void sendMsg(byte[] bytes) {
        chMap.forEach((chId, ch) -> {
            System.out.println(chId + "send:" + bytesString(bytes));
            ch.writeAndFlush(Unpooled.buffer().writeBytes(bytes));
        });
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String bytesString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(bytes[i] + " ");
        }
        return sb.toString();
    }


}
