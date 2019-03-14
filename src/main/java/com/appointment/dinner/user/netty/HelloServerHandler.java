package com.appointment.dinner.user.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.xml.crypto.Data;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
public class HelloServerHandler extends     SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String arg1)
    {
        // TODO Auto-generated method stub
        System.out.println(arg0.channel().remoteAddress()+"   ----channelRead0");
        //收到消息直接打印
        System.out.println(arg0.channel().remoteAddress()+"   MSG:  "+ arg1);
        //回复消息
        Scanner scanner = new Scanner(System.in);
        String msgString = scanner.nextLine()+"\n";
        System.out.println(arg0.channel().remoteAddress()+"  msgString:  "+ msgString);

        arg0.writeAndFlush(msgString);
    }


    /**
     * channel被激活时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        // TODO Auto-generated method stub

        System.out.println(ctx.channel().remoteAddress()+"   ----Acrive");
        try {
            ctx.writeAndFlush("欢迎访问亿见科技(广州)即时通讯系统"+InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}