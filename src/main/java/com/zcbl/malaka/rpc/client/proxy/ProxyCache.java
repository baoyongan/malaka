package com.zcbl.malaka.rpc.client.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyCache {
	private static ProxyCache cache = new ProxyCache();
	private static Map<String, Map<String, Method>> methodMap = new ConcurrentHashMap<String, Map<String, Method>>();
	private static Map<String, Class> classMap = new ConcurrentHashMap<String, Class>();
	private static Map<String, Object> objectMap = new ConcurrentHashMap<String, Object>();
	public final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProxyImpl.class);

	private ProxyCache() {
	}

	public static ProxyCache getInstance() {
		return cache;
	}

	public Method getMethod(String className, String method) {
		Map<String, Method> m = methodMap.get(className);
		if (m == null)
			return null;
		return m.get(method);
	}

	public void addMethod(String className, String method, Method m) {
		Map<String, Method> mm = methodMap.get(className);
		if (mm == null) {
			Map<String, Method> map = new HashMap<String, Method>();
			map.put(method, m);
			methodMap.put(className, map);
		} else {
			mm.put(method, m);
		}
	}

	public ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ProxyCache.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
				}
			}
		}
		return cl;
	}

	public Class getOraddClass(String className) {
		Class cls = getClass(className);
		if (cls != null)
			return cls;
		try {
			Class c = Class.forName(className, true, getDefaultClassLoader());
			classMap.put(className, c);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getOraddObject(String className) {
		Object obj = objectMap.get(className);
		if (obj != null)
			return obj;
		try {
			Class cls = getOraddClass(className);
			if (cls == null)
				return null;
			Object nobj = cls.newInstance();
			objectMap.put(className, nobj);
			return nobj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Class getClass(String className) {
		return classMap.get(className);
	}
}
