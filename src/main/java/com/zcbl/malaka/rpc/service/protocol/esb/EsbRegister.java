package com.zcbl.malaka.rpc.service.protocol.esb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zcbl.malaka.rpc.client.protocol.zookeeper.ListenManager;
import com.zcbl.malaka.rpc.common.Malaka;
import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.common.url.Response;
import com.zcbl.malaka.rpc.service.bean.ThriftBean;
import com.zcbl.malaka.rpc.service.service.ServiceCacheManager;

public class EsbRegister
{
	private static class LazyHolder
	{
		private static final EsbRegister INSTANCE = new EsbRegister();
	}

	public final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EsbRegister.class);
	ExecutorService executor = Executors.newSingleThreadExecutor();

	private EsbRegister()
	{
	}

	public static final EsbRegister getInstance()
	{
		return LazyHolder.INSTANCE;
	}

	private static String REGISTER = "esb.register";
	private static String GET = "esb.get";
	private static String WATCH = "esb.watch";
	private volatile static int seq;

	public void start()
	{
		String esb = ServerContext.getInstance().getEsb();
		if (esb == null || esb.trim().equals(""))
			return;
		execute(esb);
		executor.execute(new Task(esb));
	}

	private void execute(String esb)
	{
		Map<String, List<ThriftBean>> ports = ServiceCacheManager.getInstance().getServiceListByPort();
		if (ports.isEmpty())
		{
			ports = new HashMap<String, List<ThriftBean>>();
			ports.put(ServerContext.getInstance().getPort(), null);
		}
		Iterator<String> ites = ports.keySet().iterator();
		while (ites.hasNext())
		{
			String ip = ServerContext.getInstance().getIp() + ":" + ites.next();
			Map<String, String> req = new HashMap<String, String>();
			req.put("ip", ip);
			Response response = Malaka.remote(WATCH).server(esb).request(req).result();
			String result = response.getParamter("r");
			String s_result = response.getParamter("s");
			if (result != null && "0".equals(result))
			{
				List<ThriftBean> list = ServiceCacheManager.getInstance().getServiceList();
				if (list != null && list.size() > 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
						ThriftBean bean = list.get(i);
						if (bean.getPath() == null || bean.getPath().trim().equals(""))
							continue;
						Map<String, String> request = new HashMap<String, String>();
						request.put("ip", ServerContext.getInstance().getIp() + ":" + bean.getPort());
						request.put("path", bean.getPath());
						Malaka.remote(REGISTER).server(esb).request(request).result();
					}
				}
			}
			if (s_result != null)
			{
				int resu = Integer.parseInt(s_result);
				if (seq < resu)
				{
					seq = resu;
					response = Malaka.remote(GET).server(esb).result();
					System.out.println(resu);
					System.out.println(response.getResponse());
					Set<String> set = response.getResponse().keySet();
					Iterator<String> ite = set.iterator();
					while (ite.hasNext())
					{
						String path = ite.next();
						String values = response.getParamter(path);
						if (values == null)
							continue;
						String[] args = values.split(",");
						List<String> lists = new ArrayList<String>();
						for (String a : args)
						{
							if (a != null && !"".equals(a.trim()))
								lists.add(a);
						}
						if (lists.size() > 0)
							ListenManager.getInstance().execute(path, lists);
					}
				} else
				{
					seq = resu;
				}
			}

		}
	}

	public class Task implements Runnable
	{
		String esb;

		Task(String esb)
		{
			this.esb = esb;
		}

		public void run()
		{
			while (true)
			{
				execute(esb);
				try
				{
					Thread.sleep(Integer.parseInt(ServerContext.getInstance().getHeart() == null ? "15"
							: ServerContext.getInstance().getHeart()) * 1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
