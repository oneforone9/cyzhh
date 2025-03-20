package com.essence.tcp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ByteArrayDecoder extends ByteToMessageDecoder {

    private static Logger log = LoggerFactory.getLogger(ByteArrayDecoder.class);


        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
            //创建字节数组,buffer.readableBytes可读字节长度
            byte[] b = new byte[buffer.readableBytes()];
            //复制内容到字节数组b
            buffer.readBytes(b);
            //字节数组转字符串
            String str = new String(b);
            out.add(b);
        }


//        public static String toHexString1(byte[] b) {
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < b.length; ++i) {
//                buffer.append(toHexString1(b[i]));
//            }
//            return buffer.toString();
//        }
//
//        public static String toHexString1(byte b) {
//            String s = Integer.toHexString(b & 0xFF);
//            if (s.length() == 1) {
//                return "0" + s.toUpperCase();
//            } else {
//                return s.toUpperCase();
//            }
//        }
    }
