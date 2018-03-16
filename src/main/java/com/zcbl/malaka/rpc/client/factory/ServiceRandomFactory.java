package com.zcbl.malaka.rpc.client.factory;

import java.util.Random;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月24日
 */
public class ServiceRandomFactory extends ServiceCommonFactory
{
	private static final Random RANDOM = new Random();

	public int getIndex(Context context, int k)
	{
		return RANDOM.nextInt(k);
	}

}
