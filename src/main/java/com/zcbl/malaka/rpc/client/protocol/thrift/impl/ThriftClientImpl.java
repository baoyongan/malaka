/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.exception.MalakaException;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftClient;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ThriftConnectionPoolProvider;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.impl.DefaultThriftConnectionPoolImpl;
import com.zcbl.malaka.rpc.client.proxy.ProxyCache;

/**
 * @author jys 2016年8月29日
 */
public class ThriftClientImpl implements ThriftClient
{
	private ThriftConnectionPoolProvider poolProvider;

	private Supplier<ThriftServerInfo> serverInfoProvider;

	public ThriftClientImpl(Supplier<ThriftServerInfo> serverInfoProvider)
	{
		this(serverInfoProvider, DefaultThriftConnectionPoolImpl.getInstance());
	}

	public ThriftClientImpl(Supplier<ThriftServerInfo> serverInfoProvider, ThriftConnectionPoolProvider poolProvider)
	{
		this.poolProvider = poolProvider;
		this.serverInfoProvider = serverInfoProvider;
	}

	public ThriftClientImpl(ThriftConnectionPoolProvider poolProvider)
	{
		this.poolProvider = poolProvider;
	}

	public ThriftConnectionPoolProvider getPoolProvider()
	{
		return poolProvider;
	}

	public void setPoolProvider(ThriftConnectionPoolProvider poolProvider)
	{
		this.poolProvider = poolProvider;
	}

	public Supplier<ThriftServerInfo> getServerInfoProvider()
	{
		return serverInfoProvider;
	}

	public void setServerInfoProvider(Supplier<ThriftServerInfo> serverInfoProvider)
	{
		this.serverInfoProvider = serverInfoProvider;
	}

	@Override
	public <X extends TServiceClient> X iface(Context context) throws Exception
	{
		return iface(context, TCompactProtocol::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X extends TServiceClient> X iface(Context context, Function<TTransport, TProtocol> protocolProvider)
			throws Exception
	{
		ThriftServerInfo selected = null;
		try
		{
			selected = serverInfoProvider.get();
			TTransport transport = poolProvider.getConnection(selected);
			context.add(selected, transport);
			TProtocol protocol = protocolProvider.apply(transport);
			String c = context.getClassInfacePath().replace("$Iface", "");
			TMultiplexedProtocol mulpro = new TMultiplexedProtocol(protocol, c);

			Class cc = ProxyCache.getInstance().getOraddClass(c + "$Client");
			Constructor<X> cons = cc.getConstructor(new Class[]
			{ org.apache.thrift.protocol.TProtocol.class });
			X client = null;
			try
			{
				client = (X) cons.newInstance(mulpro);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e)
			{
				e.printStackTrace();
			}
			return client;
		} catch (Exception e1)
		{
			throw new MalakaException(selected.toString() + e1.getMessage());
		}
	}
}
