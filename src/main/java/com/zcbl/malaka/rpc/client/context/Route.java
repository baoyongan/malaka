package com.zcbl.malaka.rpc.client.context;

/**
 * @author jys 2016年8月24日
 */
public enum Route {
	RADOM("radom"), HASH("hash"), BALANCE("balance"),BROADCAST("broadcast");
	private final String value;

	Route(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
