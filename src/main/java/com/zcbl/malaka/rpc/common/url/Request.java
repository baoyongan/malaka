package com.zcbl.malaka.rpc.common.url;

import java.util.HashMap;
import java.util.Map;

public class Request
{
	Map<String, String> out = new HashMap<String, String>();

	public Map<String, String> getRequest()
	{
		return out;
	}

	public String getParamter(String paramter)
	{
		return out.get(paramter);
	}
}
