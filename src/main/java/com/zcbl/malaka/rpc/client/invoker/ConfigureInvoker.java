package com.zcbl.malaka.rpc.client.invoker;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.context.Protocol;
import com.zcbl.malaka.rpc.client.context.Route;
import com.zcbl.malaka.rpc.client.xml.bean.Service;
import com.zcbl.malaka.rpc.client.xml.cache.ZookeeperClientCache;

/**
 * @author jys 2016年8月24日
 */
public class ConfigureInvoker implements Invoker
{
	private static class LazyHolder
	{
		private static final ConfigureInvoker INSTANCE = new ConfigureInvoker();
	}

	private ConfigureInvoker()
	{
	}

	public static final ConfigureInvoker getInstance()
	{
		return LazyHolder.INSTANCE;
	}

	@Override
	public void invoker(Context context)
	{
		Service service = ZookeeperClientCache.getInstance().getService(context.getClassPath());
		if (service != null)
		{
			if (service.getProtocol() != null)
			{
				if (service.getProtocol().equals(Protocol.THRIFT.getValue()))
				{
					context.setProtocol(Protocol.THRIFT);
				}
			}
			if (service.getRoute() != null)
			{
				if (service.getRoute().equals(Route.HASH.getValue()))
				{
					context.setRoute(Route.HASH);
				} else if (service.getRoute().equals(Route.RADOM.getValue()))
				{
					context.setRoute(Route.RADOM);
				} else if (service.getRoute().equals(Route.BALANCE.getValue()))
				{
					context.setRoute(Route.BALANCE);
				} else if (service.getRoute().equals(Route.BROADCAST.getValue()))
				{
					context.setRoute(Route.BROADCAST);
				}
			}
		}
	}

}
