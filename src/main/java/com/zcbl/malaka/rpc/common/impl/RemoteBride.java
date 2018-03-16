package com.zcbl.malaka.rpc.common.impl;

import java.lang.reflect.Method;

import org.apache.thrift.TException;

import com.zcbl.malaka.rpc.common.inter.Bridge;
import com.zcbl.malaka.rpc.common.inter.RpcRequest;
import com.zcbl.malaka.rpc.common.inter.RpcResponse;
import com.zcbl.malaka.rpc.common.proxy.UrlCache;
import com.zcbl.malaka.rpc.common.url.Request;
import com.zcbl.malaka.rpc.common.url.Response;

public class RemoteBride implements Bridge.Iface
{
	public final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RemoteBride.class);
	private static RemoteBride bridge = new RemoteBride();

	public static RemoteBride getInstance()
	{
		return bridge;
	}

	@Override
	public RpcResponse remote(RpcRequest arg0) throws TException
	{
		Long start = System.currentTimeMillis();
		RpcResponse res = new RpcResponse();
		Response response = new Response();
		Request request = new Request();
		String url = arg0.getSys().get("url");
		String ip = arg0.getSys().get("ip");
		Object obj = UrlCache.getInstance().getObject(url);
		if (obj == null)
		{
			StringBuilder sb = new StringBuilder().append("malaka:").append(ip).append("--->").append("【").append(url)
					.append("】 ");
			sb.append(",but url mapping instance is not exist!");
			logger.error(sb.toString());
			return res;
		} else
		{
			Method method = UrlCache.getInstance().getMethod(url);
			if (method == null)
			{
				logger.error(url + ",but url method mapping instance is not exist!");
				return res;
			}
			if (arg0.getParamter() != null)
			{
				request.getRequest().putAll(arg0.getParamter());
			}
			try
			{
				method.invoke(obj, request, response);
			} catch (Exception e)
			{
				logger.error("request 【" + url + "】 : " + e.getCause().getMessage());
			}
			if (response.getResponse() != null)
				res.setParamter(response.getResponse());
			Long end = System.currentTimeMillis();
			if (logger.isDebugEnabled())
			{
				StringBuilder sb = new StringBuilder().append("malaka:").append(ip).append("--->").append("p=")
						.append(url).append(",r=").append(response.getResponse().toString()).append(",w=")
						.append(request.getRequest().toString()).append(",s=").append(end - start).append("ms");
				logger.debug(sb.toString());
			} else
			{
				StringBuilder sb = new StringBuilder().append("malaka:").append(ip).append("--->").append("p=")
						.append(url).append(",s=").append(end - start).append("ms");
				logger.info(sb.toString());
			}
			return res;
		}
	}
}
