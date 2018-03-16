package com.zcbl.malaka.rpc.client.invoker;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.context.Route;

/**
 * @author jys 2016年8月24日
 */
public class RouteInvoker implements Invoker {
	private static class LazyHolder {
		private static final RouteInvoker INSTANCE = new RouteInvoker();
	}

	private RouteInvoker() {
	}

	public static final RouteInvoker getInstance() {
		return LazyHolder.INSTANCE;
	}

	@Override
	public void invoker(Context context) {
		if (context.getRoute() == null) {
			if (context.getHash() != null) {
				context.setRoute(Route.HASH);
			} else {
				context.setRoute(Route.BALANCE);
			}
		}
	}
}
