package com.zcbl.malaka.rpc.service.xml.bean;

import java.io.Serializable;

public class Service implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String inter;
	String version;
	String cls;
	String port;

	public String getInter()
	{
		return inter;
	}

	public void setInter(String inter)
	{
		this.inter = inter;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getCls()
	{
		return cls;
	}

	public void setCls(String cls)
	{
		this.cls = cls;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}
}
