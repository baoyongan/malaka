/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.pool;

import org.apache.thrift.transport.TTransport;

import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;

/**
 * @author jys 2016年8月29日
 */
public interface ThriftConnectionPoolProvider {

	TTransport getConnection(ThriftServerInfo thriftServerInfo);

	void returnConnection(ThriftServerInfo thriftServerInfo, TTransport transport);

	void returnBrokenConnection(ThriftServerInfo thriftServerInfo, TTransport transport);

	void close();
}
