package netty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.*;
import io.netty.util.concurrent.Promise;

public class MyNettyClient {


    Bootstrap bootstrap;
    Promise<String> result;

    public MyNettyClient()  {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        EventExecutor eventExecutor = new DefaultEventLoop();
        result = new DefaultPromise<>(eventExecutor);

        try {

            bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new HttpClientCodec());
                            p.addLast(new HttpContentDecompressor());
                            p.addLast(new HttpObjectAggregator(65535));
                            p.addLast(new MyHandler(result));
                        }
                    });
        } catch (Exception e) {

        }
    }

    public String doGet(String url) throws URISyntaxException, InterruptedException, ExecutionException {

        URI uri = new URI(url);
        ChannelFuture cf = bootstrap.connect(uri.getHost(),uri.getPort()).sync();
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,uri.getRawPath());
        request.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
        request.headers().set(HttpHeaderNames.HOST,uri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.CLOSE);

        ChannelFuture channelFuture = cf.channel().writeAndFlush(request);

        return result.get();

    }


    public String doPost(String url, String jsonBody) throws URISyntaxException, InterruptedException, ExecutionException {

        URI uri = new URI(url);
        ChannelFuture cf = bootstrap.connect(uri.getHost(),uri.getPort()).sync();
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,uri.getRawPath(), Unpooled.wrappedBuffer(jsonBody.getBytes()));
        request.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,jsonBody.length());
        request.headers().set(HttpHeaderNames.HOST,uri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.CLOSE);

        ChannelFuture channelFuture = cf.channel().writeAndFlush(request);

        return result.get();

    }




}
