package com.zcbl.malaka.rpc.client.protocol.zookeeper.catalog;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;

/**
 * @author jys 2016年8月25日
 */
public class CataLogCache {
	static CataLogCache cache = new CataLogCache();
	volatile Map<String, Supplier<List<ThriftServerInfo>>> cata = new Hashtable<String, Supplier<List<ThriftServerInfo>>>();
	volatile Map<String, Supplier<List<ThriftServerInfo>>> history = new Hashtable<String, Supplier<List<ThriftServerInfo>>>();

	private CataLogCache() {
	}

	public static CataLogCache getInstance() {
		return cache;
	}

	public void addCata(String key, Supplier<List<ThriftServerInfo>> value) {
		if (history.get(key) == null) {
			cata.put(key, value);
			history.put(key, value);
		} else {
			cata.put(key, value);
			List<ThriftServerInfo> historyList = history.get(key).get();
			List<ThriftServerInfo> nowList = value.get();
			if (historyList != null && historyList.size() > 0 && nowList != null && nowList.size() > 0) {
				Iterator<ThriftServerInfo> ite = nowList.iterator();
				while (ite.hasNext()) {
					ThriftServerInfo info = ite.next();
					if (!historyList.contains(info)) {
						historyList.add(info);
					}
				}
			}
		}
	}

	public Supplier<List<ThriftServerInfo>> getCata(String key) {
		Supplier<List<ThriftServerInfo>> list = cata.get(key);
		if (list == null || list.get().size() == 0)
			return history.get(key);
		return list;
	}

	public Supplier<List<ThriftServerInfo>> getFail(String key) {
		Supplier<List<ThriftServerInfo>> list = history.get(key);
		return list;
	}
}
