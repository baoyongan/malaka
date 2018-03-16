package com.zcbl.malaka.rpc.service.protocol.zookeeper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.ZkClient;

import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.service.bean.ThriftBean;
import com.zcbl.malaka.rpc.service.service.ServiceCacheManager;

/**
 * @author jys 2016年9月1日
 */
public class ZookeeperRegister
{
	static final ZookeeperRegister service = new ZookeeperRegister();
	public final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ZookeeperRegister.class);

	private ZookeeperRegister()
	{
	}

	public static ZookeeperRegister getInstance()
	{
		return service;
	}

	ExecutorService executor = Executors.newCachedThreadPool();

	public void start()
	{
		String register = ServerContext.getInstance().getServer();
		if (register != null && !register.equals(""))
		{
			String[] str = register.split("\\;");
			for (String server : str)
			{
				if (server == null || "".equals(server.trim()))
				{
					continue;
				}
				executor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						registService(server);
					}
				});
			}
		}
	}

	public void registService(String server)
	{
		String servicePath = ServerContext.getInstance().getApplication();
		if (servicePath == null || servicePath == "")
		{
			servicePath = "/";
		} else
		{
			if (servicePath.startsWith("/"))
			{
				servicePath = "/";
			} else
			{
				servicePath = "/";
			}
		}
		ZkClient zkClient = new ZkClient(server);
		List<ThriftBean> list = ServiceCacheManager.getInstance().getServiceList();
		if (list != null && list.size() > 0)
		{
			while (true)
			{
				for (int i = 0; i < list.size(); i++)
				{
					ThriftBean bean = list.get(i);
					if (bean.getPath() == null || bean.getPath().trim().equals(""))
						continue;
					StringBuffer path = new StringBuffer();
					path.append(servicePath.toString()).append(bean.getPath());
					boolean secondExists = zkClient.exists(path.toString());
					if (!secondExists)
					{
						zkClient.createPersistent(path.toString());
					}
					path.append("/").append(ServerContext.getInstance().getIp()).append(":").append(bean.getPort());
					boolean rootExists = zkClient.exists(path.toString());
					if (!rootExists)
					{
						if (bean.getData() != null)
						{
							zkClient.createEphemeral(path.toString(), bean.getData());
						} else
							zkClient.createEphemeral(path.toString());
						logger.info("提供的服务为：" + path.toString());
					}
				}
				try
				{
					Thread.sleep(Integer.parseInt(ServerContext.getInstance().getHeart() == null ? "30"
							: ServerContext.getInstance().getHeart()) * 1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
