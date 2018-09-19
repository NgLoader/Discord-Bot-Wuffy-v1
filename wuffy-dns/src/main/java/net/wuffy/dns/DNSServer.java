package net.wuffy.dns;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import io.netty.handler.codec.dns.DatagramDnsQueryDecoder;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.handler.codec.dns.DatagramDnsResponseEncoder;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsSection;

public class DNSServer implements Runnable {

	private DNSConfig config;

	public DNSServer(DNSConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		new Bootstrap()
		.group(DNS.KQUEUE ? new KQueueEventLoopGroup() : DNS.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup())
		.channel(DNS.KQUEUE ? KQueueDatagramChannel.class : DNS.EPOLL ? EpollDatagramChannel.class : NioDatagramChannel.class)
		.handler(new ChannelInitializer<Channel>() {

			protected void initChannel(Channel channel) throws Exception {
				channel.pipeline()
				.addLast("dnsQueryDecoder", new DatagramDnsQueryDecoder())
				.addLast("dnsResponseEncode", new DatagramDnsResponseEncoder())
				.addLast("dnsHandler", new SimpleChannelInboundHandler<DatagramDnsQuery>() {

					@Override
					protected void channelRead0(ChannelHandlerContext ctx, DatagramDnsQuery msg) throws Exception {
						DatagramDnsResponse response = new DatagramDnsResponse(msg.recipient(), msg.sender(), msg.id());

						response.addRecord(
							DnsSection.QUESTION,
							msg.recordAt(DnsSection.QUESTION)
						).addRecord(
							DnsSection.ANSWER,
							new DefaultDnsRawRecord(
								DNSServer.this.config.recordName,
								DnsRecordType.A,
								DNSServer.this.config.recordTimeToLive,
								Unpooled.buffer(4).writeInt(DNS.currentlyMasterAddress)
							)
						);

						ctx.writeAndFlush(response, ctx.voidPromise());
					}
				});
			};
		}).localAddress(new InetSocketAddress(this.config.host, this.config.port)).bind().syncUninterruptibly();
	}
}