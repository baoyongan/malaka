package com.zcbl.malaka.rpc.client.utils;

/**
 * @author jys
 * 2016年8月24日
 */
public class ReflectUtils {
	public static Object getObject(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
