package de.ngloader.network;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Ingrim4
 */
public class NettyUtil {

	private static final boolean PREFER_NIO = Boolean.getBoolean("imprex.netty.preferNio"); //TODO property
	private static final boolean EPOLL = !PREFER_NIO && Epoll.isAvailable();
	private static final boolean KQUEUE = !PREFER_NIO && KQueue.isAvailable();

	private static final ThreadGroup NETTY_THREAD_GROUP = new ThreadGroup("netty");
	private static final AtomicInteger NETTY_THREAD_ID = new AtomicInteger();

	static {
		NETTY_THREAD_GROUP.setDaemon(true);
	}

	private static ThreadFactory getThreadFactory(final String name) {
		return runnable -> {
			Thread thread = new Thread(NETTY_THREAD_GROUP, runnable, String.format(name, NETTY_THREAD_ID.getAndIncrement()));
			thread.setDaemon(true);
			return thread;
		};
	}

	public static final EventLoopGroup createEventLoopGroup(int threads) {
		if(EPOLL)
			return new EpollEventLoopGroup(threads, getThreadFactory("netty-epoll-%d"));
		else if(KQUEUE)
			return new KQueueEventLoopGroup(threads, getThreadFactory("netty-kqueue-%d"));
		return new NioEventLoopGroup(threads, getThreadFactory("netty-nio-%d"));
	}

	public static final Class<? extends ServerSocketChannel> getServerChannelClass() {
		if(EPOLL)
			return EpollServerSocketChannel.class;
		else if(KQUEUE)
			return KQueueServerSocketChannel.class;
		return NioServerSocketChannel.class;
	}

	public static final Class<? extends SocketChannel> getClientChannelClass() {
		if(EPOLL)
			return EpollSocketChannel.class;
		else if(KQUEUE)
			return KQueueSocketChannel.class;
		return NioSocketChannel.class;
	}
}
