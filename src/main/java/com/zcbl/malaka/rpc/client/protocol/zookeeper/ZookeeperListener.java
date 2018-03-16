package com.zcbl.malaka.rpc.client.protocol.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import com.zcbl.malaka.rpc.common.register.ServerContext;

/**
 * @author jys 2016年8月24日
 */
public class ZookeeperListener
{
	private static final ZookeeperListener zookeeperListener = new ZookeeperListener();
	private Map<String, String> exists = new ConcurrentHashMap<String, String>();

	public static ZookeeperListener getInstance()
	{
		return zookeeperListener;
	}

	private ZookeeperListener()
	{
	}

	public void start()
	{
		String servers = ServerContext.getInstance().getServer();
		if (servers == null || servers.equals(""))
			return;
		String[] str = servers.split("\\;");
		for (String server : str)
		{
			if (server == null || "".equals(server.trim()))
				continue;
			Thread t = new Thread(new Runnable()
			{
				public void run()
				{
					List<String> currChilds = new ArrayList<String>();
					String servicePath = ServerContext.getInstance().getApplication();
					if (servicePath == null || servicePath.equals("/"))
					{
						servicePath = "/";
					} else
					{
						servicePath = "/";
					}
					ZkClient zkClient = new ZkClient(server);
					boolean serviceExists = zkClient.exists(servicePath);
					if (serviceExists)
					{
						currChilds = zkClient.getChildren(servicePath);
					}
					if (currChilds != null && currChilds.size() > 0)
					{
						for (String str : currChilds)
						{
							exists.put(str, str);
							String path = "";
							if (servicePath.equals("/"))
							{
								path = servicePath + str;
							} else
							{
								path = servicePath + "/" + str;
							}
							List<String> ipChilds = zkClient.getChildren(path);
							ListenManager.getInstance().execute(str, ipChilds);
							zkClient.subscribeChildChanges(path, new IZkChildListener()
							{
								public void handleChildChange(String parentPath, List<String> currentChilds)
										throws Exception
								{
									ListenManager.getInstance().execute(parentPath, currentChilds);
								}
							});
						}
					}
					zkClient.subscribeChildChanges(servicePath, new IZkChildListener()
					{
						public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception
						{
							if (currentChilds != null)
								for (String key : currentChilds)
								{
									if (!exists.containsKey(key))
									{
										exists.put(key, key);
										String path = "/" + key;
										List<String> ipChilds = zkClient.getChildren(path);
										ListenManager.getInstance().execute(key, ipChilds);
										zkClient.subscribeChildChanges(path, new IZkChildListener()
										{
											public void handleChildChange(String parentPath, List<String> currentChilds)
													throws Exception
											{
												ListenManager.getInstance().execute(parentPath, currentChilds);
											}
										});
									}
								}
						}
					});
				}
			});
			t.start();
			try
			{
				t.join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
