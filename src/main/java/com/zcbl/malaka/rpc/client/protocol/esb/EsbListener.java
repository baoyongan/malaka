package com.zcbl.malaka.rpc.client.protocol.esb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.zcbl.malaka.rpc.client.protocol.zookeeper.ListenManager;
import com.zcbl.malaka.rpc.common.Malaka;
import com.zcbl.malaka.rpc.common.register.ServerContext;
import com.zcbl.malaka.rpc.common.url.Response;

public class EsbListener
{
	private static final EsbListener esbListener = new EsbListener();
	private static final String RESULT = "result";
	private static int seq;
	private static String WATCH = "esb.server.state.watch";
	private static String GET = "esb.server.state.get";

	public static EsbListener getInstance()
	{
		return esbListener;
	}

	private EsbListener()
	{
	}

	public void start()
	{
		String esb = ServerContext.getInstance().getEsb();
		if (esb == null || esb.trim().equals(""))
			return;
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					try
					{
						Response res = Malaka.remote(WATCH).server(esb).result();
						String result = res.getParamter(RESULT);
						if (result != null)
						{
							int resu = Integer.parseInt(result);
							if (seq < resu)
							{
								seq = resu;
								res = Malaka.remote(GET).server(esb).result();
								Set<String> set = res.getResponse().keySet();
								Iterator<String> ite = set.iterator();
								while (ite.hasNext())
								{
									String path = ite.next();
									String values = res.getParamter(path);
									if (values == null)
										continue;
									String[] args = values.split(",");
									List<String> list = new ArrayList<String>();
									for (String a : args)
									{
										if (a != null && !"".equals(a.trim()))
											list.add(a);
									}
									if (list.size() > 0)
										ListenManager.getInstance().execute(path, list);
								}
							} else
							{
								seq = resu;
							}
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					try
					{
						Thread.sleep(Integer.parseInt(ServerContext.getInstance().getHeart() == null ? "10"
								: ServerContext.getInstance().getHeart()) * 1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
}
