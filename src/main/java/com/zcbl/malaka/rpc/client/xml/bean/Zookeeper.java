package com.zcbl.malaka.rpc.client.xml.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jys
 * 2016年8月24日
 */
public class Zookeeper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public Register register;
	public List<Service> serviceList = new ArrayList<Service>();


	public void addService(Service service) {
		serviceList.add(service);
	}
}
