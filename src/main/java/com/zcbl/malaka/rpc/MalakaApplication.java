package com.zcbl.malaka.rpc;

import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ChannelLocal;
import com.zcbl.malaka.rpc.client.protocol.zookeeper.ZookeeperListener;
import com.zcbl.malaka.rpc.client.xml.util.ZookeeperClientUtils;
import com.zcbl.malaka.rpc.common.conf.ConfigFactory;
import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.common.scan.AnnationScan;
import com.zcbl.malaka.rpc.service.protocol.esb.EsbRegister;
import com.zcbl.malaka.rpc.service.protocol.thrift.ThriftRegister;
import com.zcbl.malaka.rpc.service.protocol.zookeeper.ZookeeperRegister;
import com.zcbl.malaka.rpc.service.xml.util.ZookeeperServerUtils;

/**
 * @author jys 2016年9月1日
 */
public class MalakaApplication
{
	private static boolean start;
	private static byte[] b = new byte[0];
	public String server;
	public String ip;
	public String port;
	public String application;
	public String scan;
	public String heart;
	public String version;
	public String out;
	public String esb;

	public String getEsb()
	{
		return esb;
	}

	public void setEsb(String esb)
	{
		this.esb = esb;
	}

	public String getOut()
	{
		return out;
	}

	public void setOut(String out)
	{
		this.out = out;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication(String application)
	{
		this.application = application;
	}

	public String getScan()
	{
		return scan;
	}

	public void setScan(String scan)
	{
		this.scan = scan;
	}

	public String getHeart()
	{
		return heart;
	}

	public void setHeart(String heart)
	{
		this.heart = heart;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public MalakaApplication()
	{
	}

	public MalakaApplication(String center)
	{
		if (center != null && !center.equals(""))
			ServerContext.getInstance().setIp(center);
		start();
	}

	public MalakaApplication(String center, String port)
	{
		ServerContext.getInstance().setServer(center);
		ServerContext.getInstance().setPort(port);
		start();
	}

	public MalakaApplication(String center, String port, String local)
	{
		ServerContext.getInstance().setServer(center);
		ServerContext.getInstance().setPort(port);
		ServerContext.getInstance().setIp(local);
		start();
	}

	private void init()
	{
		ServerContext.getInstance().setApplication(application);
		ServerContext.getInstance().setHeart(heart);
		ServerContext.getInstance().setIp(ip);
		ServerContext.getInstance().setPort(port);
		ServerContext.getInstance().setScan(scan);
		ServerContext.getInstance().setServer(server);
		ServerContext.getInstance().setVersion(version);
		ServerContext.getInstance().setOut(out);
		ServerContext.getInstance().setEsb(esb);
		ZookeeperClientUtils.getInstance().start();
		ZookeeperServerUtils.getInstance().start();
		ConfigFactory.anysis();
		AnnationScan.scan();
		ThriftRegister.getInstance().start();
		ZookeeperRegister.getInstance().start();
		EsbRegister.getInstance().start();
		ZookeeperListener.getInstance().start();
	}

	public void start()
	{
		if (!start)
		{
			synchronized (b)
			{
				if (start)
				{
					return;
				}
				start = true;
				init();
				close();
			}
		}
	}

	private static void close()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				ChannelLocal.close();
			}
		});
	}
}
