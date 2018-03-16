package com.zcbl.malaka.rpc.client.context;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.thrift.pool.ChannelLocal.RebackObject;
import com.zcbl.malaka.rpc.common.inter.RpcRequest;

/**
 * @author jys 2016年8月24日
 */
public class Context
{

	public Class cls = null;
	public Protocol protocol = null;
	public Route route = null;
	public String hash = null;
	private String className;
	private String classInferName;
	ThriftServerInfo lastinfo;
	Queue<RebackObject> queue = new LinkedList<RebackObject>();
	private String path = null;
	RpcRequest requset;
	public String server;
	public int times=3;
	public boolean strategy = true;
	public String from;
	public String to;

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public boolean isStrategy()
	{
		return strategy;
	}

	public void setStrategy(boolean strategy)
	{
		this.strategy = strategy;
	}

	public int getTimes()
	{
		return times;
	}

	public void setTimes(int times)
	{
		this.times = times;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public RpcRequest getRequset()
	{
		return requset;
	}

	public void setRequset(RpcRequest requset)
	{
		this.requset = requset;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Context()
	{
	}

	public Queue<RebackObject> getQueue()
	{
		return queue;
	}

	public void setQueue(Queue<RebackObject> queue)
	{
		this.queue = queue;
	}

	public void add(ThriftServerInfo info, TTransport t)
	{
		queue.add(new RebackObject(info, t));
	}

	public ThriftServerInfo getLastinfo()
	{
		return lastinfo;
	}

	public void setLastinfo(ThriftServerInfo lastinfo)
	{
		this.lastinfo = lastinfo;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public Context(Class cls)
	{
		this.cls = cls;
	}

	public Context(Class cls, String hash)
	{
		this.cls = cls;
		this.hash = hash;
	}

	public Context(Protocol protocol, Route route, Class cls)
	{
		this.cls = cls;
		this.protocol = protocol;
		this.route = route;
	}

	public Class getCls()
	{
		return cls;
	}

	public void setCls(Class cls)
	{
		this.cls = cls;
	}

	public Protocol getProtocol()
	{
		return protocol;
	}

	public void setProtocol(Protocol protocol)
	{
		this.protocol = protocol;
	}

	public Route getRoute()
	{
		return route;
	}

	public void setRoute(Route route)
	{
		this.route = route;
	}

	public String getClassPath()
	{
		if (className == null)
			return className = this.getCls().getName().replace("$Iface", "");
		else
			return className;
	}

	public String getPointer()
	{
		if ((getPath()) != null)
		{
			return getPath();
		} else
		{
			return getClassPath();
		}
	}

	public String getClassInfacePath()
	{
		if (classInferName == null)
			return classInferName = this.getCls().getName();
		else
			return classInferName;
	}

	public void clear()
	{
		this.queue.clear();
		this.classInferName = null;
		this.className = null;
		this.route = null;
		this.hash = null;
	}
}
