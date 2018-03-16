/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Splitter;
import com.google.common.collect.MapMaker;

/**
 * @author jys 2016年8月29日
 */
public final class ThriftServerInfo {

	private static ConcurrentMap<String, ThriftServerInfo> allInfos = new MapMaker().weakValues().makeMap();

	private static Splitter splitter = Splitter.on(':');

	private final String host;

	private final int port;

	private ThriftServerInfo(String hostAndPort) {
		List<String> split = splitter.splitToList(hostAndPort);
		assert split.size() == 2;
		this.host = split.get(0);
		this.port = Integer.parseInt(split.size() > 1 ? split.get(1) : "0");
	}

	public static ThriftServerInfo of(String host, int port) {
		return allInfos.computeIfAbsent(host + ":" + port, ThriftServerInfo::new);
	}

	public static ThriftServerInfo of(String hostAndPort) {
		return allInfos.computeIfAbsent(hostAndPort, ThriftServerInfo::new);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ThriftServerInfo)) {
			return false;
		}
		ThriftServerInfo other = (ThriftServerInfo) obj;
		if (host == null) {
			if (other.host != null) {
				return false;
			}
		} else if (!host.equals(other.host)) {
			return false;
		}
		return port == other.port;
	}

	@Override
	public String toString() {
		return "ThriftServerInfo [host=" + host + ", port=" + port + "]";
	}

}
