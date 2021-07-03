import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

public class HttpHandler extends ChannelInboundHandlerAdapter {


    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String url = fullHttpRequest.uri();
            if(url.contains("/test")) {
                handleRequest(ctx,fullHttpRequest,"test");
            } else {
                handleRequest(ctx,fullHttpRequest,"other");
            }
        } catch (Exception e) {

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handleRequest(ChannelHandlerContext ctx,FullHttpRequest request,String response) {
        FullHttpResponse response1= null;
        try {

            HttpVersion version;
            HttpResponseStatus status;
            response1 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(response.getBytes()));
            response1.headers().add("Content-Type","application/json");
            response1.headers().setInt("Content-Length",response1.content().readableBytes());
        } catch (Exception e){

        } finally {
            response1.headers().set("Connection","keep-alive");
            ctx.write(response1);
        }

    }


}
