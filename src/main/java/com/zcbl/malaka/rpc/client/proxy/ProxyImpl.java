package com.zcbl.malaka.rpc.client.proxy;

import java.lang.reflect.Method;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.factory.ServiceFactory;
import com.zcbl.malaka.rpc.client.factory.ServiceFactoryContext;
import com.zcbl.malaka.rpc.client.invoker.ServiceInvoker;

/**
 * @author jys 2016年9月8日
 */
public class ProxyImpl
{
	public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProxyImpl.class);

	public static Object invoker(String className, String method, Object[] args) throws Throwable
	{
		ServiceFactory fa = null;
		Method m = null;
		Class c = null;
		Context text = null;
		try
		{
			m = ProxyCache.getInstance().getMethod(className, method);
			if (m == null)
				return null;
			m.setAccessible(true);
			c = ProxyCache.getInstance().getOraddClass(className);
			if (c == null)
			{
				logger.error("interface class load error!");
				return null;
			}
			text = new Context(c);
			ServiceInvoker invoker = ServiceInvoker.getInstance();
			invoker.Invoker(text);
			fa = ServiceFactoryContext.getInstance().getFactory(text);
			if (fa == null)
			{
				logger.error("servicefacotry is null");
				return null;
			}
			Object obj = fa.invoker(text, m, args);
			return obj;
		} catch (Exception e)
		{
			logger.error("return service error", e);
		} finally
		{
			text.clear();
			text = null;
			m = null;
			c = null;
			fa = null;
		}
		return null;
	}
}
