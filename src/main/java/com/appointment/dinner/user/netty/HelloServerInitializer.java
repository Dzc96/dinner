package com.appointment.dinner.user.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

@Component
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel arg0) throws Exception {
        // TODO Auto-generated method stub
        //ChannelPipeline 可以理解为消息传送通道 通道一旦建立 持续存在
        ChannelPipeline channelPipeline = arg0.pipeline();
        //为通道添加功能
        //字符串解码  编码
        channelPipeline.addLast("decoder",new StringDecoder());
        channelPipeline.addLast("encoder", new StringEncoder());

        //添加自主逻辑
        channelPipeline.addLast(new HelloServerHandler());
    }
}