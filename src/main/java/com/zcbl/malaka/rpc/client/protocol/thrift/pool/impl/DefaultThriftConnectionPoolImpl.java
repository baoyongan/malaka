/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.pool.impl;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.function.Function;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;

import com.zcbl.malaka.rpc.client.exception.MalakaException;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ThriftConnectionPoolProvider;

/**
 * @author jys 2016年8月29日
 */
public final class DefaultThriftConnectionPoolImpl implements ThriftConnectionPoolProvider
{

	private static final Logger logger = getLogger(DefaultThriftConnectionPoolImpl.class);
	private static final int MIN_CONN = 1;
	private static final int MAX_CONN = 1000;
	private static final int SESSIONTIMEOUT = 20000;
	private static final int CON_TIME_OUT = 300;
	private final GenericKeyedObjectPool<ThriftServerInfo, TTransport> connections;

	public DefaultThriftConnectionPoolImpl(GenericKeyedObjectPoolConfig config,
			Function<ThriftServerInfo, TTransport> transportProvider)
	{
		connections = new GenericKeyedObjectPool<>(new ThriftConnectionFactory(transportProvider), config);
	}

	public DefaultThriftConnectionPoolImpl(GenericKeyedObjectPoolConfig config)
	{
		this(config, info ->
		{
			try
			{
				TSocket tsocket = new TSocket(info.getHost(), info.getPort());
				tsocket.setConnectTimeout(CON_TIME_OUT);
				tsocket.setSocketTimeout(SESSIONTIMEOUT);
				return new TFramedTransport(tsocket);
			} catch (Exception e)
			{
				logger.error(e.getMessage());
			}
			return null;
		});
	}

	public static DefaultThriftConnectionPoolImpl getInstance()
	{
		return LazyHolder.INSTANCE;
	}

	@Override
	public TTransport getConnection(ThriftServerInfo thriftServerInfo)
	{
		try
		{
			return connections.borrowObject(thriftServerInfo);
		} catch (java.net.ConnectException e)
		{
			throw new MalakaException("connect refuse", e);
		} catch (java.net.SocketTimeoutException e)
		{
			throw new MalakaException("connect timeout", e);
		} catch (Exception e)
		{
			throw new MalakaException(e);
		}
	}

	@Override
	public void returnConnection(ThriftServerInfo thriftServerInfo, TTransport transport)
	{
		if (thriftServerInfo == null && transport == null)
			return;
		connections.returnObject(thriftServerInfo, transport);
	}

	@Override
	public void returnBrokenConnection(ThriftServerInfo thriftServerInfo, TTransport transport)
	{
		if (thriftServerInfo == null && transport == null)
			return;
		try
		{
			connections.invalidateObject(thriftServerInfo, transport);
		} catch (Exception e)
		{
			logger.error("fail to invalid object:{},{}", thriftServerInfo, transport, e);
		}
	}

	private static class LazyHolder
	{

		private static final DefaultThriftConnectionPoolImpl INSTANCE;
		static
		{
			GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
			config.setMaxTotal(MAX_CONN);
			config.setMaxTotalPerKey(MAX_CONN);
			config.setMaxIdlePerKey(MAX_CONN);
			config.setMinIdlePerKey(MIN_CONN);
			config.setTestOnBorrow(true);
			config.setMinEvictableIdleTimeMillis(MINUTES.toMillis(1));
			config.setSoftMinEvictableIdleTimeMillis(MINUTES.toMillis(1));
			config.setJmxEnabled(false);
			INSTANCE = new DefaultThriftConnectionPoolImpl(config);
		}
	}

	public static final class ThriftConnectionFactory implements KeyedPooledObjectFactory<ThriftServerInfo, TTransport>
	{

		private final Function<ThriftServerInfo, TTransport> transportProvider;

		public ThriftConnectionFactory(Function<ThriftServerInfo, TTransport> transportProvider)
		{
			this.transportProvider = transportProvider;
		}

		@Override
		public PooledObject<TTransport> makeObject(ThriftServerInfo info) throws Exception
		{
			TTransport transport = transportProvider.apply(info);
			transport.open();
			DefaultPooledObject<TTransport> result = new DefaultPooledObject<>(transport);
			logger.info("make new thrift connection:{}", info);
			return result;
		}

		@Override
		public void destroyObject(ThriftServerInfo info, PooledObject<TTransport> p) throws Exception
		{
			TTransport transport = p.getObject();
			logger.info("close thrift connection:{}", info);
			if (transport != null && transport.isOpen())
			{
				transport.close();
				logger.info("close thrift connection:{}", info);
			}
		}

		@Override
		public boolean validateObject(ThriftServerInfo info, PooledObject<TTransport> p)
		{
			try
			{
				return p.getObject().isOpen();
			} catch (Throwable e)
			{
				logger.error("fail to validate tsocket:{}", info, e);
				return false;
			}
		}

		@Override
		public void activateObject(ThriftServerInfo info, PooledObject<TTransport> p) throws Exception
		{
			// do nothing
		}

		@Override
		public void passivateObject(ThriftServerInfo info, PooledObject<TTransport> p) throws Exception
		{
			// do nothing
		}

	}

	@Override
	public void close()
	{
		connections.close();
	}
}
