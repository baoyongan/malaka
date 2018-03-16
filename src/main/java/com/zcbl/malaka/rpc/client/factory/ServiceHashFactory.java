package com.zcbl.malaka.rpc.client.factory;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月24日
 */
public class ServiceHashFactory extends ServiceCommonFactory
{

	public int getIndex(Context context, int length)
	{
		if (context.getHash() != null)
		{
			return Math.abs(context.getHash().hashCode()) % length;
		}
		return 0;
	}
}
