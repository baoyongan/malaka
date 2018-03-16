/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftClient;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ThriftConnectionPoolProvider;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.impl.DefaultThriftConnectionPoolImpl;

/**
 * @author jys 2016年8月29日
 */
public class FailoverThriftClientImpl implements ThriftClient
{
	private ThriftClient thriftClient;
	private FailoverStategy failoverStategy;

	public FailoverThriftClientImpl(FailoverCheckingStrategy<ThriftServerInfo> failoverCheckingStrategy,
			Supplier<List<ThriftServerInfo>> serverInfoProvider, boolean strategy)
	{
		failoverStategy = new FailoverStategy(serverInfoProvider, DefaultThriftConnectionPoolImpl.getInstance(),
				failoverCheckingStrategy, strategy);
	}

	public List<ThriftServerInfo> getServerInfoProviderList()
	{
		return failoverStategy.get();
	}

	public void addServerInfoProvider(Supplier<ThriftServerInfo> serverInfoProvider)
	{
		thriftClient = new ThriftClientImpl(serverInfoProvider, failoverStategy);
	}

	private class FailoverStategy implements Supplier<List<ThriftServerInfo>>, ThriftConnectionPoolProvider
	{

		private final Supplier<List<ThriftServerInfo>> originalServerInfoProvider;

		private final ThriftConnectionPoolProvider connectionPoolProvider;

		private final FailoverCheckingStrategy<ThriftServerInfo> failoverCheckingStrategy;
		private boolean strategy = true;

		private FailoverStategy(Supplier<List<ThriftServerInfo>> originalServerInfoProvider,
				ThriftConnectionPoolProvider connectionPoolProvider,
				FailoverCheckingStrategy<ThriftServerInfo> failoverCheckingStrategy, boolean strategy)
		{
			this.originalServerInfoProvider = originalServerInfoProvider;
			this.connectionPoolProvider = connectionPoolProvider;
			this.failoverCheckingStrategy = failoverCheckingStrategy;
			this.strategy = strategy;
		}

		@Override
		public List<ThriftServerInfo> get()
		{
			if (originalServerInfoProvider == null || originalServerInfoProvider.get().size() == 0)
				return null;
			Set<ThriftServerInfo> failedServers = failoverCheckingStrategy.getFailed();
			if (strategy)
				return originalServerInfoProvider.get().stream().filter(i -> !failedServers.contains(i))
						.collect(toList());
			else
				return originalServerInfoProvider.get();
		}

		@Override
		public TTransport getConnection(ThriftServerInfo thriftServerInfo)
		{
			try
			{
				return connectionPoolProvider.getConnection(thriftServerInfo);
			} catch (Exception e)
			{
				if (strategy)
					failoverCheckingStrategy.fail(thriftServerInfo);
				throw e;
			}
		}

		@Override
		public void returnConnection(ThriftServerInfo thriftServerInfo, TTransport transport)
		{
			connectionPoolProvider.returnConnection(thriftServerInfo, transport);
		}

		@Override
		public void returnBrokenConnection(ThriftServerInfo thriftServerInfo, TTransport transport)
		{
			if (strategy)
				failoverCheckingStrategy.fail(thriftServerInfo);
			connectionPoolProvider.returnBrokenConnection(thriftServerInfo, transport);
		}

		@Override
		public void close()
		{
			// TODO Auto-generated method stub
			connectionPoolProvider.close();
		}

	}

	@Override
	public <T extends TServiceClient> T iface(Context context) throws Exception
	{
		// TODO Auto-generated method stub
		return thriftClient.iface(context);
	}

	@Override
	public <T extends TServiceClient> T iface(Context context, Function<TTransport, TProtocol> protocolProvider)
			throws Exception
	{
		// TODO Auto-generated method stub
		return thriftClient.iface(context, protocolProvider);
	}

}
