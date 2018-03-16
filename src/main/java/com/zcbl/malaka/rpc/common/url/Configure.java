package com.zcbl.malaka.rpc.common.url;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.context.Protocol;
import com.zcbl.malaka.rpc.client.context.Route;
import com.zcbl.malaka.rpc.client.factory.ServiceFactory;
import com.zcbl.malaka.rpc.client.factory.ServiceFactoryContext;
import com.zcbl.malaka.rpc.client.invoker.ServiceInvoker;
import com.zcbl.malaka.rpc.common.inter.Bridge;
import com.zcbl.malaka.rpc.common.inter.RpcRequest;
import com.zcbl.malaka.rpc.common.inter.RpcResponse;
import com.zcbl.malaka.rpc.common.register.ServerContext;

public class Configure implements ProtocolInter, RouteInter, Draft, Timer
{
	public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Configure.class);
	Context text = new Context();
	Map<String, String> sys;
	Map<String, String> user;

	public Configure(Map<String, String> sys)
	{
		this.sys = sys;
		sys.put("ip", ServerContext.getInstance().getIp());
	}

	@Override
	public Configure pro(Protocol p)
	{
		text.setProtocol(p);
		return this;
	}

	@Override
	public Configure server(String server)
	{
		text.setServer(server);
		return this;
	}

	public Configure strategy(boolean strategy)
	{
		text.setStrategy(strategy);
		return this;
	}

	@Override
	public Configure route(Route r)
	{
		text.setRoute(r);
		return this;
	}

	public Configure hidden(boolean hidden)
	{
		if (hidden)
			sys.remove("ip");
		else
			sys.put("ip", ServerContext.getInstance().getIp());
		return this;
	}

	public Configure request(Map<String, String> paramter)
	{
		validate(paramter);
		this.user = paramter;
		return this;
	}

	public Response result()
	{
		Response res = new Response();
		RpcRequest arg0 = new RpcRequest();
		RpcResponse arg1 = new RpcResponse();
		arg0.setSys(sys);
		if (user == null)
			user = new HashMap<String, String>();
		arg0.setParamter(user);
		text.setCls(Bridge.Iface.class);
		ServiceInvoker invoker = ServiceInvoker.getInstance();
		invoker.Invoker(text);
		text.setPath(sys.get("url").replaceAll("\\/", "."));
		text.setRequset(arg0);
		ServiceFactory fa = ServiceFactoryContext.getInstance().getFactory(text);
		try
		{
			arg1 = (RpcResponse) fa.invoker(text);
			if (arg1 != null && arg1.getParamter() != null)
			{
				res.getResponse().putAll(arg1.getParamter());
			}
		} catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		text = null;
		return res;
	}

	@Override
	public Configure times(int times)
	{
		if (times > 0)
			text.setTimes(times);
		return this;
	}

	private void validate(Map<String, String> map)
	{
		if (map != null && !map.isEmpty())
		{
			Iterator<String> ite = map.keySet().iterator();
			while (ite.hasNext())
			{
				String key = ite.next();
				Object obj = map.get(key);
				if (obj == null)
				{
					ite.remove();
				}
			}
		}
	}
}
