/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift;

import java.util.function.Function;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.context.Context;

/**
 * @author jys 2016年8月29日
 */
public interface ThriftClient
{
	<X extends TServiceClient> X iface(Context context) throws Exception;

	<X extends TServiceClient> X iface(Context context, Function<TTransport, TProtocol> protocolProvider)
			throws Exception;
}
