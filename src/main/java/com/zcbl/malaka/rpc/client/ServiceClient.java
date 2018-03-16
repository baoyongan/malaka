package com.zcbl.malaka.rpc.client;

import com.zcbl.malaka.rpc.MalakaApplication;
import com.zcbl.malaka.rpc.client.proxy.ServiceProxy;

/**
 * @author jys 2016年8月24日
 */
public class ServiceClient
{
	protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	static
	{
		new MalakaApplication().start();
	}
	static ServiceClient client = new ServiceClient();

	private ServiceClient()
	{
	}

	public static ServiceClient getInstance()
	{
		return client;
	}

	public Object findService(Class cls, String hash)
	{
		return findService(cls);
	}

	public Object findService(Class text)
	{
		Object obj = null;
		try
		{
			obj = ServiceProxy.getInstance().getProxyService(text);
		} finally
		{
			// close(text);
		}
		return obj;
	}

}
