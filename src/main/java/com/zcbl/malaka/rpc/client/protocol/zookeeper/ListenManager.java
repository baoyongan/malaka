package com.zcbl.malaka.rpc.client.protocol.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.zcbl.malaka.rpc.client.protocol.thrift.ThriftServerInfo;
import com.zcbl.malaka.rpc.client.protocol.zookeeper.catalog.CataLogCache;

/**
 * @author jys
 * 2016年9月1日
 */
public class ListenManager {
	static ListenManager manager = new ListenManager();

	private ListenManager() {
	}

	public static ListenManager getInstance() {
		return manager;
	}

	public void execute(String path, List<String> str) {
		int p = path.lastIndexOf("/");
		if (p != -1) {
			path = path.substring(p + 1);
		}
		List<ThriftServerInfo> infoList = new ArrayList<ThriftServerInfo>();
		if (str != null && str.size() > 0) {
			for (String s : str) {
				ThriftServerInfo info = ThriftServerInfo.of(s);
				infoList.add(info);
			}
		}
		Supplier<List<ThriftServerInfo>> serverListProvider = () -> infoList;
		CataLogCache.getInstance().addCata(path, serverListProvider);
	}
}
