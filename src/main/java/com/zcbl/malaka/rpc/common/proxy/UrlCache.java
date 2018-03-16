package com.zcbl.malaka.rpc.common.proxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlCache
{
	private static UrlCache cache = new UrlCache();
	private static Map<String, Object> urlObject = new ConcurrentHashMap<String, Object>();
	private static Map<String, Method> urlMethod = new ConcurrentHashMap<String, Method>();
	public final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UrlCache.class);

	private UrlCache()
	{
	}

	public static UrlCache getInstance()
	{
		return cache;
	}

	public Method getMethod(String url)
	{
		Method m = urlMethod.get(url);
		return m;
	}

	public void addMethod(String url, Method m)
	{
		urlMethod.put(url, m);
	}

	public Object getObject(String url)
	{
		Object obj = urlObject.get(url);
		return obj;
	}

	public void addObject(String url, Object obj)
	{
		urlObject.put(url, obj);
	}

}
