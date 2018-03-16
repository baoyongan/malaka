package com.zcbl.malaka.rpc.common.url;

import java.util.HashMap;
import java.util.Map;

public class Response
{
	Map<String, String> out = new HashMap<String, String>();

	public Map<String, String> getResponse()
	{
		return out;
	}

	public String getParamter(String paramter)
	{
		return out.get(paramter);
	}

	public void setParamter(String key, String value)
	{
		this.out.put(key, value);
	}
}
