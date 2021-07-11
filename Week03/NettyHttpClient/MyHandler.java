package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.Promise;

import java.nio.charset.StandardCharsets;

public class MyHandler extends ChannelInboundHandlerAdapter {


    private Promise<String> result;

    public MyHandler(Promise<String> result) {
        this.result = result;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("msg: "+msg);
        if(msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf buf = response.content();
            String responseResult = buf.toString(StandardCharsets.UTF_8);
            this.result.setSuccess(responseResult);
            System.out.println("Response: "+responseResult);

        }

    }
}
