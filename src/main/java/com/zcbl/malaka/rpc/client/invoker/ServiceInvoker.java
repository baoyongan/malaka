package com.zcbl.malaka.rpc.client.invoker;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月24日
 */
public class ServiceInvoker {

	public Queue<Invoker> quene = new LinkedBlockingQueue<Invoker>();
	{
		quene.add(ConfigureInvoker.getInstance());
		quene.add(RouteInvoker.getInstance());
		quene.add(ProtocolInvoker.getInstance());
	}
	static ServiceInvoker invoker = new ServiceInvoker();

	public static ServiceInvoker getInstance() {
		return invoker;
	}

	private ServiceInvoker() {
	}

	public void Invoker(Context context) {
		Iterator<Invoker> ite = quene.iterator();
		while (ite.hasNext()) {
			Invoker in = ite.next();
			in.invoker(context);
		}
	}

	public void Invoker(Context context, Invoker invoker) {
		this.quene.add(invoker);
		Invoker(context);
	}
}
