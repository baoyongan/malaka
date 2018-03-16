package com.zcbl.malaka.rpc.client.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.exception.MalakaException;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.impl.FailoverCheckingStrategy;
import com.zcbl.malaka.rpc.client.protocol.thrift.impl.FailoverThriftClientImpl;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ChannelLocal;
import com.zcbl.malaka.rpc.client.protocol.zookeeper.catalog.CataLogCache;
import com.zcbl.malaka.rpc.common.inter.Bridge;
import com.zcbl.malaka.rpc.common.inter.RpcResponse;

/**
 * @author jys 2016年8月24日
 */
public abstract class ServiceCommonFactory implements ServiceFactory
{
	protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	static FailoverCheckingStrategy<ThriftServerInfo> failoverCheckingStrategy = new FailoverCheckingStrategy<>(5,
			TimeUnit.SECONDS.toMillis(30), TimeUnit.MINUTES.toMillis(1));

	public void returnService(Context context)
	{
		ChannelLocal.returnConnection(context);
	}

	public void destoryService(Context context)
	{
		ChannelLocal.returnBrokenConnection(context);
	}

	public abstract int getIndex(Context context, int length);

	public Object findService(Context context) throws Exception
	{
		FailoverThriftClientImpl failover = new FailoverThriftClientImpl(failoverCheckingStrategy, findList(context),
				context.isStrategy());
		List<ThriftServerInfo> infoList = failover.getServerInfoProviderList();
		Supplier<ThriftServerInfo> supplier = null;
		if (infoList != null && infoList.size() > 0)
		{
			ThriftServerInfo info = infoList.get(getIndex(context, infoList.size()));
			supplier = () -> info;
		}
		if (supplier == null)
		{
			logger.info("find 【" + context.getPointer() + "】 service is empty,make sure your configure is ok?");
			return null;
		}
		failover.addServerInfoProvider(supplier);
		try
		{
			context.setTo(supplier.get().getHost() + ":" + supplier.get().getPort());
			return failover.iface(context);
		} catch (Exception e)
		{
			ThriftServerInfo info = supplier.get();
			context.setLastinfo(info);
			logger.error("error get service object.", e);
			throw new MalakaException(e);
		}
	}

	public Supplier<List<ThriftServerInfo>> findList(Context context) throws Exception
	{
		List<ThriftServerInfo> infoList = new ArrayList<ThriftServerInfo>();
		Supplier<List<ThriftServerInfo>> failoverList = CataLogCache.getInstance().getCata(context.getPointer());
		ThriftServerInfo lastinfo = context.getLastinfo();
		if (lastinfo != null && infoList != null)
			infoList.remove(lastinfo);
		if (context.getServer() != null && !context.getServer().trim().equals(""))
		{
			String[] args = context.getServer().split("\\,");
			for (String server : args)
			{
				ThriftServerInfo info = ThriftServerInfo.of(server);
				infoList.add(info);
			}
		} else
		{
			return failoverList;
		}
		Supplier<List<ThriftServerInfo>> serverListProvider = () -> infoList;
		return serverListProvider;
	}

	@Override
	public Object invoker(Context context, Method method, Object... args) throws Exception
	{
		for (int i = 0; i < context.getTimes(); i++)
		{
			Long start = System.currentTimeMillis();
			Object object = null;
			try
			{
				Object service = findService(context);
				if (service == null)
				{
					return null;
				}
				object = method.invoke(service, args);
			} catch (MalakaException e)
			{
				this.destoryService(context);
				continue;
			} catch (Exception e)
			{
				logger.error("error execute service object method:" + context.getPointer() + "." + method.getName(), e);
				this.destoryService(context);
				continue;
			} finally
			{
				Long end = System.currentTimeMillis();
				logger.info(new StringBuilder().append("malaka:").append(context.getFrom()).append("--->i=")
						.append(context.getTo()).append(",p=").append(context.getPointer()).append(".")
						.append(method.getName()).append(",s=").append(end - start).append("ms").append(",t=")
						.append(i + 1).toString());
				this.returnService(context);
			}
			return object;
		}
		return null;
	}

	@Override
	public Object invoker(Context context)
	{
		for (int i = 0; i < context.getTimes(); i++)
		{
			Long start = System.currentTimeMillis();
			Object object = null;
			try
			{
				Object service = findService(context);
				if (service == null)
				{
					return null;
				}
				Bridge.Iface iface = (Bridge.Iface) service;
				object = iface.remote(context.getRequset());
			} catch (Exception e)
			{
				logger.error("error execute service object method:" + context.getPath(), e);
				this.destoryService(context);
				continue;
			} finally
			{
				this.returnService(context);
				Long end = System.currentTimeMillis();
				if (logger.isDebugEnabled())
				{
					StringBuilder sb = new StringBuilder();
					RpcResponse r = (RpcResponse) object;
					sb.append("malaka:").append(context.getFrom()).append("--->i=").append(context.getTo())
							.append(",p=").append(context.getPath()).append(",r=").append(r.getParamter().toString())
							.append(",w=").append(context.getRequset().getParamter().toString()).append(",t=")
							.append(i + 1).append(",s=").append(end - start).append("ms");
					logger.debug(sb.toString());
				} else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("malaka:").append(context.getFrom()).append("--->i=").append(context.getTo())
							.append(",p=").append(context.getPath()).append(",t=").append(i + 1).append(",s=")
							.append(end - start).append("ms");
					logger.info(sb.toString());
				}
			}
			return object;
		}
		return null;
	}

}
