package com.zcbl.malaka.rpc.client.context;

/**
 * @author jys
 * 2016年8月24日
 */
public enum Protocol {
	THRIFT("thrift");
	private final String value;

	Protocol(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
