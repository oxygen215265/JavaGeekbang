import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyDemo {
    public static void main(String[] args) {
        int port = 8001;
        EventLoopGroup boss = new NioEventLoopGroup(2);
        EventLoopGroup worker = new NioEventLoopGroup(16);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG,128)
        .childOption(ChannelOption.TCP_NODELAY,true)
        .childOption(ChannelOption.SO_KEEPALIVE,true)
        .childOption(ChannelOption.SO_REUSEADDR,true)
        .childOption(ChannelOption.SO_RCVBUF,32*1024)
        .childOption(ChannelOption.SO_SNDBUF,32*1024)
        .childOption(EpollChannelOption.SO_REUSEPORT,true)
        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.group(boss, worker).channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpInitializer());

            Channel ch = b.bind(port).sync().channel();
            System.out.println("localhost:"+port);
            ch.closeFuture().sync();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
