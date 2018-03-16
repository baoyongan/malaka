package com.zcbl.malaka.rpc.client.invoker;

import com.zcbl.malaka.rpc.client.context.Context;
import com.zcbl.malaka.rpc.client.context.Protocol;

/**
 * @author jys 2016年8月24日
 */
public class ProtocolInvoker implements Invoker {
	private static class LazyHolder {
		private static final ProtocolInvoker INSTANCE = new ProtocolInvoker();
	}

	private ProtocolInvoker() {
	}

	public static final ProtocolInvoker getInstance() {
		return LazyHolder.INSTANCE;
	}

	@Override
	public void invoker(Context context) {
		if (context.getProtocol() == null) {
			context.setProtocol(Protocol.THRIFT);
		}
	}

}
