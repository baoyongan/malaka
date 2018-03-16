package com.zcbl.malaka.rpc.client.factory;

import java.lang.reflect.Method;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月24日
 */
public interface ServiceFactory
{
	public void returnService(Context context);

	public void destoryService(Context context);

	public Object invoker(Context context, Method method, Object... args) throws Exception;

	public Object invoker(Context context) throws Exception;
}
