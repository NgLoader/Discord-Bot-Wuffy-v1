package net.wuffy.loadbalancer.network.dns;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import io.netty.handler.codec.dns.DatagramDnsQueryDecoder;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.handler.codec.dns.DatagramDnsResponseEncoder;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsSection;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.loadbalancer.LoadBalancer;
import net.wuffy.loadbalancer.network.NetHandlerLoadBalancerServer;
import net.wuffy.network.AbstractServer;
import net.wuffy.network.EnumProtocolState;
import net.wuffy.network.NettyUtil;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerMasterUpdate;

public class DNSServer extends AbstractServer implements ITickable {

	public static final List<NetworkManager> MASTERS = new LinkedList<NetworkManager>();

	private DNSConfig config;
	private Long lastUpdate = -1L;

	private NetworkManager currentlyMaster;
	private UUID currentlyMasterId;
	private int currentlyMasterTrys = 0;

	private Long lastStartTry = 0L;
	private boolean starting = false;
	private Channel channel;

	public DNSServer(DNSConfig config) {
		this.config = config;
	}

	@Override
	public void update() {
		if(this.channel != null && !this.channel.isOpen() && !this.starting)
			if(lastStartTry < System.currentTimeMillis()) {
				this.lastStartTry = System.currentTimeMillis() + 30000;
				this.start();
			}

		if(this.lastUpdate > System.currentTimeMillis())
			return;
		this.lastUpdate = System.currentTimeMillis() + 1000;

		if(currentlyMaster != null) {
			if(currentlyMaster.isConnected())
				return;

			if(++this.currentlyMasterTrys < this.config.master.reconnectTrys) {
				for(NetworkManager master : DNSServer.MASTERS) {
					if(master.getProtocol() == EnumProtocolState.LOADBALANCER) {
						NetHandlerLoadBalancerServer handler = ((NetHandlerLoadBalancerServer)master.getHandler());

						if(!handler.isInit() && handler.getUUID().equals(this.currentlyMasterId)) { //Checking if the currently master the old master
							this.currentlyMaster = master;
							this.currentlyMasterTrys = 0;
							LoadBalancer.currentlyMasterAddress = LoadBalancer.defaultMasterAddress;
							handler.getNetworkManager().sendPacket(new CPacketLoadBalancerMasterUpdate()); //Send again while the master think when the connection is lost he is no longer the main master

							Logger.info("DNSServer", "Master connection was rebuilt.");
							return;
						}
					}
				}
				return;
			}

			//Resetting
			this.currentlyMaster = null;
			this.currentlyMasterId = null;
			this.currentlyMasterTrys = 0;
			Logger.info("DNSServer", "Not able the resume master connecting. Searching new master...");
		}

		for(NetworkManager master : DNSServer.MASTERS) {
			if(master.getProtocol() == EnumProtocolState.LOADBALANCER) {
				NetHandlerLoadBalancerServer handler = ((NetHandlerLoadBalancerServer)master.getHandler());

				if(!handler.isInit()) { //Get the next master
					this.currentlyMaster = master;
					this.currentlyMasterId = handler.getUUID();
					this.currentlyMasterTrys = 0;

					master.sendPacket(new CPacketLoadBalancerMasterUpdate());
					LoadBalancer.currentlyMasterAddress = handler.getAddressAsInt();

					Logger.info("DNSServer", String.format("New Master found \"%s\".", handler.getUUID().toString()));
					return;
				}
			}
		}
	}

	public void start() {
		this.starting = true;

		try {
			this.channel = new Bootstrap()
			.group(NettyUtil.createEventLoopGroup(0))
			.channel(NettyUtil.getDatagramChannelClass())
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
									DNSServer.this.config.dns.recordName,
									DnsRecordType.A,
									DNSServer.this.config.dns.recordTimeToLive,
									Unpooled.buffer(4).writeInt(LoadBalancer.currentlyMasterAddress)
								)
							);

							ctx.writeAndFlush(response, ctx.voidPromise());
						}
					});
				};
			}).localAddress(new InetSocketAddress(this.config.dns.host, this.config.dns.port)).bind().syncUninterruptibly().channel();
		} catch (Exception e) {
			Logger.fatal("DNSServer", "Error by starting DNS server.", e);
		}
		this.starting = false;
	}
}