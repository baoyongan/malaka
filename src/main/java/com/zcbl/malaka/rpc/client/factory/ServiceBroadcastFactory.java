package com.zcbl.malaka.rpc.client.factory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.impl.FailoverThriftClientImpl;
import com.zcbl.malaka.rpc.common.inter.Bridge;
import com.zcbl.malaka.rpc.common.inter.RpcResponse;

public class ServiceBroadcastFactory extends ServiceCommonFactory
{

	public Object findService(Context context) throws Exception
	{
		FailoverThriftClientImpl failover = new FailoverThriftClientImpl(failoverCheckingStrategy,
				super.findList(context), context.isStrategy());
		List<ThriftServerInfo> infoList = failover.getServerInfoProviderList();
		if (infoList != null && infoList.size() > 0)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (ThriftServerInfo info : infoList)
			{
				Supplier<ThriftServerInfo> supplier = () -> info;
				failover.addServerInfoProvider(supplier);
				try
				{
					Object obj = failover.iface(context);
					map.put(info.getHost() + info.getPort(), obj);
				} catch (Exception e)
				{
					logger.error("service privider is error" + info.getHost() + ":" + info.getPort() + "{}", e);
				}
			}
			return map;
		} else
		{
			logger.info(context.getClassInfacePath() + " service privider is null");
			return null;
		}
	}

	public int getIndex(Context context, int length)
	{
		return 0;
	}

	@Override
	public Object invoker(Context context, Method method, Object... args) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			Object obj = findService(context);
			if (obj == null)
			{
				logger.info("can't not find service! make sure your configure is ok?");
				return null;
			}
			Map<String, Object> map = (Map<String, Object>) obj;
			if (map != null && map.size() > 0)
			{
				Set<String> set = map.keySet();
				Iterator<String> ite = set.iterator();
				while (ite.hasNext())
				{
					String key = ite.next();
					Object o = map.get(key);
					Object object = method.invoke(o, args);
					sb.append(key).append(object.toString()).append(",");
				}
			}
		} catch (Exception e)
		{
			logger.error("execute service method error", e);
			this.destoryService(context);
		} finally
		{
			this.returnService(context);
		}
		return sb.toString();
	}

	@Override
	public Object invoker(Context context)
	{
		RpcResponse response = new RpcResponse();
		StringBuffer sb = new StringBuffer();
		Object object = null;
		try
		{
			Object service = findService(context);
			if (service == null)
			{
				logger.error("can't not find service! make sure your configure is ok?");
				return null;
			}
			Map<String, Object> map = (Map<String, Object>) service;
			if (map != null && map.size() > 0)
			{
				Set<String> set = map.keySet();
				Iterator<String> ite = set.iterator();
				while (ite.hasNext())
				{
					String key = ite.next();
					Object o = map.get(key);
					Bridge.Iface iface = (Bridge.Iface) o;
					object = iface.remote(context.getRequset());
					sb.append(key).append(object.toString()).append(",");
				}
			}
			Map<String, String> m = new HashMap<String, String>();
			response.setParamter(m);
			m.put("result", sb.toString());
		} catch (Exception e)
		{
			logger.error("execute service remote method error", e);
			this.destoryService(context);
		} finally
		{
			this.returnService(context);
		}
		return response;
	}
}
