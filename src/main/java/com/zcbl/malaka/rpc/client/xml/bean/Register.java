package com.zcbl.malaka.rpc.client.xml.bean;

import java.io.Serializable;

/**
 * @author jys 2016年8月24日
 */
public class Register implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String server;
	String application;
	String version;
	String esb;
	String ip;
	
	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getEsb()
	{
		return esb;
	}

	public void setEsb(String esb)
	{
		this.esb = esb;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getServer()
	{

		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication(String application)
	{
		this.application = application;
	}
}
