/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.utils;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jys 2016年8月29日
 */
public final class ThriftClientUtils {


	private static ConcurrentMap<Class<?>, Set<String>> interfaceMethodCache = new ConcurrentHashMap<>();

	private ThriftClientUtils() {
		throw new UnsupportedOperationException();
	}


	public static Set<String> getInterfaceMethodNames(Class<?> ifaceClass) {
		return interfaceMethodCache.computeIfAbsent(ifaceClass,
				i -> of(i.getInterfaces()) //
						.flatMap(c -> of(c.getMethods())) //
						.map(Method::getName) //
						.collect(toSet()));
	}
}
