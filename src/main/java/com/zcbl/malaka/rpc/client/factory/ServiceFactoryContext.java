package com.zcbl.malaka.rpc.client.factory;

import java.util.HashMap;
import java.util.Map;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.context.Route;

/**
 * @author jys 2016年8月24日
 */
public class ServiceFactoryContext
{
	static ServiceFactoryContext rpc = new ServiceFactoryContext();
	public Map<Route, ServiceFactory> factory = new HashMap<Route, ServiceFactory>();
	{
		factory.put(Route.BALANCE, new ServiceBalanceFactory());
		factory.put(Route.RADOM, new ServiceRandomFactory());
		factory.put(Route.HASH, new ServiceHashFactory());
		factory.put(Route.BROADCAST, new ServiceBroadcastFactory());
	}

	private ServiceFactoryContext()
	{
	}

	public static ServiceFactoryContext getInstance()
	{
		return rpc;
	}

	public ServiceFactory getFactory(Context context)
	{
		return factory.get(context.getRoute() == null ? Route.BALANCE : context.getRoute());
	}

	public void addService(Route route, ServiceFactory factory)
	{
		this.factory.put(route, factory);
	}
}
