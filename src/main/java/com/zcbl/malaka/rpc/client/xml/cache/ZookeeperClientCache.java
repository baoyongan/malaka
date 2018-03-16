package com.zcbl.malaka.rpc.client.xml.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zcbl.malaka.rpc.client.xml.bean.Register;
import com.zcbl.malaka.rpc.client.xml.bean.Service;
import com.zcbl.malaka.rpc.client.xml.bean.Zookeeper;
import com.zcbl.malaka.rpc.client.xml.util.ZookeeperClientUtils;
import com.zcbl.malaka.rpc.common.register.ServerContext;

/**
 * @author jys 2016年8月24日
 */
public class ZookeeperClientCache
{
	static ZookeeperClientCache cahce = new ZookeeperClientCache();
	volatile Map<String, Service> map = new HashMap<String, Service>(32);
	static
	{
		ZookeeperClientUtils.getInstance();
	}

	private ZookeeperClientCache()
	{
	}

	public static ZookeeperClientCache getInstance()
	{
		return cahce;
	}

	private Zookeeper zookeeper = null;
	private Register register = null;

	public Register getRegister()
	{
		if (register == null)
			return new Register();
		else
			return register;
	}

	public void setRegister(Register register)
	{
		this.register = register;
	}

	public void setRegister(Zookeeper zookeeper)
	{
		this.zookeeper = zookeeper;
		if (zookeeper.register != null)
		{
			this.register = zookeeper.getRegister();
			ServerContext.getInstance().setApplication(register.getApplication());
			ServerContext.getInstance().setServer(register.getServer());
			ServerContext.getInstance().setEsb(register.getEsb());
			ServerContext.getInstance().setIp(register.getIp());
		}
		initService();
	}

	public Service getService(String key)
	{
		return map.get(key);
	}

	public void initService()
	{
		if (zookeeper != null)
		{
			List<Service> list = zookeeper.getServiceList();
			if (list != null && list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Service service = list.get(i);
					map.put(service.getInter(), service);
				}
			}
		}
	}
}
