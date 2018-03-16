package com.zcbl.malaka.rpc.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zcbl.malaka.rpc.client.proxy.ProxyCache;
import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.service.bean.ThriftBean;
import com.zcbl.malaka.rpc.service.xml.bean.Register;
import com.zcbl.malaka.rpc.service.xml.bean.Service;
import com.zcbl.malaka.rpc.service.xml.bean.Zookeeper;

/**
 * @author jys
 *
 */
public class ServiceCacheManager
{
	private List<ThriftBean> serviceList = new ArrayList<ThriftBean>();
	private Map<String, List<ThriftBean>> serviceMap = new HashMap<String, List<ThriftBean>>();
	private static ServiceCacheManager serviceCache = new ServiceCacheManager();
	private Zookeeper zookeeper;
	private Register register;

	public Register getRegister()
	{
		if (register == null)
		{
			return new Register();
		}
		return register;
	}

	public void setRegister(Register register)
	{
		this.register = register;
	}

	public void setRegister(Zookeeper zookeeper)
	{
		this.zookeeper = zookeeper;
		if (zookeeper.getRegister() != null)
		{
			register = zookeeper.getRegister();
			ServerContext.getInstance().setApplication(register.getApplication());
			ServerContext.getInstance().setIp(register.getLocal());
			ServerContext.getInstance().setPort(register.getServicePort());
			ServerContext.getInstance().setServer(register.getServer());
			ServerContext.getInstance().setScan(register.getScan());
			ServerContext.getInstance().setHeart(register.getHeart());
			ServerContext.getInstance().setEsb(register.getEsb());
		}
		init();
	}

	private ServiceCacheManager()
	{
	}

	public static ServiceCacheManager getInstance()
	{
		return serviceCache;
	}

	public void addService(ThriftBean bean)
	{
		serviceList.add(bean);
	}

	public void addServiceMap(ThriftBean bean)
	{
		List<ThriftBean> list = serviceMap.get(String.valueOf(bean.getPort()));
		if (list == null)
		{
			list = new ArrayList<ThriftBean>();
			list.add(bean);
			serviceMap.put(String.valueOf(bean.getPort()), list);
		} else
		{
			list.add(bean);
		}
	}

	public List<ThriftBean> getServiceList()
	{
		return serviceList;
	}

	public Map<String, List<ThriftBean>> getServiceListByPort()
	{
		return serviceMap;
	}

	public void init()
	{
		List<Service> list = zookeeper.getServiceList();
		if (list != null && list.size() > 0)
		{
			for (Service s : list)
			{
				String inter = s.getInter();
				String cla = s.getCls();
				try
				{
					ThriftBean bean = new ThriftBean();
					bean.setInter(inter);
					bean.setImpl(ProxyCache.getInstance().getOraddObject(cla));
					bean.setPort(Integer
							.parseInt(s.getPort() == null ? ServerContext.getInstance().getPort() : s.getPort()));
					this.addService(bean);
					this.addServiceMap(bean);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void addService(String inter, String path, Object impl, int port)
	{
		ThriftBean bean = new ThriftBean();
		bean.setInter(inter);
		bean.setImpl(impl);
		bean.setPath(path);
		if (ServerContext.getInstance().getPort() == null)
			return;
		bean.setPort(port == 0 ? Integer.parseInt(ServerContext.getInstance().getPort()) : port);
		this.addService(bean);
		this.addServiceMap(bean);
	}
}
