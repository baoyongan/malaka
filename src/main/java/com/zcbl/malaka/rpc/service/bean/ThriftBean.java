package com.zcbl.malaka.rpc.service.bean;

public class ThriftBean
{
	public String inter;
	public Object impl;
	public int port;
	public Object data;
	public String path;

	public String getPath()
	{
		if (path == null)
			return getInter();
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public String getInter()
	{
		return inter;
	}

	public void setInter(String inter)
	{
		this.inter = inter;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public Object getImpl()
	{
		return impl;
	}

	public void setImpl(Object impl)
	{
		this.impl = impl;
	}
}
