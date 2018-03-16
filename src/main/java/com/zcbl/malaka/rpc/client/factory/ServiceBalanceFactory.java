package com.zcbl.malaka.rpc.client.factory;

import java.util.concurrent.atomic.AtomicInteger;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月24日
 */
public class ServiceBalanceFactory extends ServiceCommonFactory
{
	AtomicInteger atomic = new AtomicInteger(-1);

	public int getIndex(Context context, int length)
	{
		int k = atomic.incrementAndGet();
		int rand = Math.abs(k % length);
		return rand;
	}
}
