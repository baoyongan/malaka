package com.zcbl.malaka.rpc.common.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import com.zcbl.malaka.rpc.common.annation.Malaka;
import com.zcbl.malaka.rpc.common.annation.Url;
import com.zcbl.malaka.rpc.common.impl.RemoteBride;
import com.zcbl.malaka.rpc.common.inter.Bridge;
import com.zcbl.malaka.rpc.common.proxy.UrlCache;
import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.service.service.ServiceCacheManager;

public class AnnationScan
{
	public static String pack = "com.zcbl";

	public static void scan()
	{
		pack = ServerContext.getInstance().getScan();
		Set<Class<?>> set = ClassUtils.getClasses(pack == null ? "com.zcbl" : pack,
				ServerContext.getInstance().getOut());
		if (set != null && !set.isEmpty())
		{
			for (Class c : set)
			{
				try
				{
					if (c != null && null != c.getAnnotation(Malaka.class))
					{
						String inter = "";
						int port = 0;
						String path = "";
						String version = "";
						for (Annotation anno : c.getDeclaredAnnotations())
						{
							if (anno.annotationType().equals(Malaka.class))
							{
								if (!((Malaka) anno).inter().equals(""))
								{
									path = ((Malaka) anno).inter();
									port = ((Malaka) anno).port();
									version = ((Malaka) anno).version();
								} else if (!((Malaka) anno).value().equals(""))
								{
									path = ((Malaka) anno).value();
									port = ((Malaka) anno).port();
									version = ((Malaka) anno).version();
								} else
								{
									inter = c.getSuperclass().getName().replaceAll("$Iface", "");
									port = ((Malaka) anno).port();
									Object impl = c.newInstance();
									ServiceCacheManager.getInstance().addService(inter, inter, impl, port);
								}
							}
						}
						for (Method method : c.getDeclaredMethods())
						{
							if (method.isAnnotationPresent(Url.class))
							{
								Annotation p = method.getAnnotation(Url.class);
								String murl = ((Url) p).value();
								String result = path + murl;
								String com = result.replaceAll("\\/", ".");
								method.setAccessible(true);
								UrlCache.getInstance().addMethod(result, method);
								UrlCache.getInstance().addObject(result, c.newInstance());
								ServiceCacheManager.getInstance().addService(Bridge.class.getName(), com,
										RemoteBride.getInstance(), port);
							}
						}
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		//ServiceCacheManager.getInstance().addService(Bridge.class.getName(), "", RemoteBride.getInstance(), 0);
	}

	public static void main(String[] args)
	{
		String p="/app/message/push";
		String com = p.replaceAll("\\/", ".");
		System.out.println(com);
	}
}
