package com.zcbl.malaka.rpc.service.xml.bean;

import java.io.Serializable;

public class Register implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String server;
	String application;
	String heart;
	String servicePort;
	String scan;
	String esb;

	public String getEsb()
	{
		return esb;
	}

	public void setEsb(String esb)
	{
		this.esb = esb;
	}

	public String getScan()
	{
		return scan;
	}

	public void setScan(String scan)
	{
		this.scan = scan;
	}

	public String getServicePort()
	{
		return servicePort;
	}

	public void setServicePort(String servicePort)
	{
		this.servicePort = servicePort;
	}

	public String getHeart()
	{
		
		return heart;
	}

	public void setHeart(String heart)
	{
		this.heart = heart;
	}

	public String getLocal()
	{
		return local;
	}

	public void setLocal(String local)
	{
		this.local = local;
	}

	String local;

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
