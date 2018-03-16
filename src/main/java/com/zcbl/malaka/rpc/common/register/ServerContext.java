package com.zcbl.malaka.rpc.common.register;

import java.util.Map;

import com.zcbl.malaka.rpc.common.conf.MalakaConfig;
import com.zcbl.malaka.rpc.common.utils.NetUtil;

public class ServerContext
{
	static ServerContext text = new ServerContext();

	private ServerContext()
	{
	}

	public static ServerContext getInstance()
	{
		return text;
	}

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
		if (esb == null || esb.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("esb");
			if (map != null)
			{
				esb = map.get("esb");
			}
		}
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

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		if (version == null)
			return;
		this.version = version;
	}

	public String getHeart()
	{
		if (heart == null || heart.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
			if (map != null)
			{
				heart = map.get("heart");
			}
		}
		return heart;
	}

	public void setHeart(String heart)
	{
		if (heart == null)
			return;
		this.heart = heart;
	}

	public String getScan()
	{
		if (scan == null || scan.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
			if (map != null)
			{
				scan = map.get("scan");
			}
		}
		return scan;
	}

	public void setScan(String scan)
	{
		if (scan == null)
			return;
		this.scan = scan;
	}

	public String getServer()
	{
		if (server == null || server.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
			if (map != null)
			{
				server = map.get("server");
			}
		}
		return server;
	}

	public void setServer(String server)
	{
		if (server == null)
			return;
		this.server = server;
	}

	public String getIp()
	{
		if (ip == null || ip.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
			if (map != null)
			{
				ip = map.get("bind-ip");
			}
			if (ip == null || ip.equals(""))
			{
				ip = NetUtil.getLocalIP();
			}
		}
		return ip;
	}

	public void setIp(String ip)
	{
		if (ip == null)
			return;
		this.ip = ip;
	}

	public String getPort()
	{
		if (port == null || port.equals(""))
		{
			Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
			if (map != null)
			{
				port = map.get("port");
			}
			if (port == null || port.equals(""))
			{
				port = NetUtil.getPort();
			}
		}
		return port;
	}

	public void setPort(String port)
	{
		if (port == null)
			return;
		this.port = port;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication(String application)
	{
		if (application == null)
			return;
		this.application = application;
	}

}
