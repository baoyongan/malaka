package com.zcbl.malaka.rpc.client.xml.bean;

import java.io.Serializable;

/**
 * @author jys 2016年8月24日
 */
public class Service implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String inter;
	String version;
	String protocol;
	String port;
	String route;
	String call;

	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getInter() {
		return inter;
	}

	public void setInter(String inter) {
		this.inter = inter;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
