package com.zcbl.malaka.rpc.client.proxy;

public class ServiceProxy implements Proxy
{
	static ServiceProxy proxy = new ServiceProxy();

	private ServiceProxy()
	{
	}

	public static ServiceProxy getInstance()
	{
		return proxy;
	}

	public Object getProxyService(Class c)
	{
		String className = c.getName();
		if (!c.isInterface())
		{
			return null;
		}
		return ProxyUtils.getProxyInstance(className);
	}
}
