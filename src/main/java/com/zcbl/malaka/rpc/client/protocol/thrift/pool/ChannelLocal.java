package com.zcbl.malaka.rpc.client.protocol.thrift.pool;

import java.util.Iterator;
import java.util.Queue;

import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.impl.DefaultThriftConnectionPoolImpl;

/**
 * @author jys 2016年9月8日
 */
public class ChannelLocal
{
	public static void returnConnection(Context context)
	{
		Queue<RebackObject> q = context.getQueue();
		if (q != null && q.size() > 0)
		{
			Iterator<RebackObject> ite = q.iterator();
			while (ite.hasNext())
			{
				RebackObject obj = ite.next();
				DefaultThriftConnectionPoolImpl.getInstance().returnConnection(obj.info, obj.transport);
				ite.remove();
			}
		}
	}

	public static void returnBrokenConnection(Context context)
	{
		Queue<RebackObject> q = context.getQueue();
		if (q != null && q.size() > 0)
		{
			Iterator<RebackObject> ite = q.iterator();
			while (ite.hasNext())
			{
				RebackObject obj = ite.next();
				DefaultThriftConnectionPoolImpl.getInstance().returnBrokenConnection(obj.info, obj.transport);
				ite.remove();
			}
		}
	}

	public static class RebackObject
	{
		ThriftServerInfo info;
		TTransport transport;

		public RebackObject(ThriftServerInfo i, TTransport t)
		{
			this.info = i;
			this.transport = t;
		}
	}

	public static void close()
	{
		DefaultThriftConnectionPoolImpl.getInstance().close();
	}
}
