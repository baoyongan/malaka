package com.zcbl.malaka.rpc.common;

import java.util.HashMap;
import java.util.Map;

import com.zcbl.malaka.rpc.MalakaApplication;
import com.zcbl.malaka.rpc.client.ServiceClient;
import com.zcbl.malaka.rpc.common.url.Configure;

public class Malaka
{
	public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Malaka.class);
	static
	{
		new MalakaApplication().start();
	}

	public static Object remote(Class clas)
	{
		return ServiceClient.getInstance().findService(clas);
	}

	public static Configure remote(String path)
	{
		if (path == null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", path);
		return new Configure(map);
	}
}
