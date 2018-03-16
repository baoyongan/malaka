package com.zcbl.malaka.rpc.service.protocol.thrift;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorOptimizeServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.zcbl.malaka.rpc.client.proxy.ProxyCache;
import com.zcbl.malaka.rpc.service.bean.ThriftBean;
import com.zcbl.malaka.rpc.service.service.ServiceCacheManager;

/**
 * @author jys 2016年9月1日
 */
public class ThriftRegister
{
	static final ThriftRegister thriftRegister = new ThriftRegister();
	protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	private final int timeout = 20000;

	private ThriftRegister()
	{
	}

	public static ThriftRegister getInstance()
	{
		return thriftRegister;
	}

	public void start()
	{
		Map<String, List<ThriftBean>> map = ServiceCacheManager.getInstance().getServiceListByPort();
		if (map != null && map.size() > 0)
		{
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext())
			{
				String port = it.next();
				List<ThriftBean> beanList = map.get(port);
				TMultiplexedProcessor processor = new TMultiplexedProcessor();
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							initService(port, beanList, processor);
						} catch (Exception e)
						{
							logger.error(e.getMessage());
						}
					}
				}).start();
			}
		}
	}

	public void initService(String port, List<ThriftBean> list, TMultiplexedProcessor processor)
	{
		List<ThriftBean> beanList = list;
		if (beanList != null && beanList.size() > 0)
		{
			TServer server = getNioActorOptimizeServer(port, timeout, processor);
			for (ThriftBean bean : beanList)
			{
				try
				{
					String iterface = bean.getInter();
					String clazzClientName = iterface + "$Processor";
					Class clientClazz = ProxyCache.getInstance().getOraddClass(clazzClientName);
					if (clientClazz == null)
						continue;
					String iface = iterface + "$Iface";
					Class ifaceClazz = ifaceClazz = ProxyCache.getInstance().getOraddClass(iface);
					if (ifaceClazz == null)
						continue;
					Constructor cons = (Constructor) clientClazz.getConstructor(ifaceClazz);
					Object obj = cons.newInstance(bean.getImpl());
					processor.registerProcessor(iterface, (TProcessor) obj);
					logger.info(bean.getPath() + ":" + port);
				} catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
			server.serve();
		}
	}

	private TServer getNioServer(String port, int timeout, TProcessor processor)
	{
		TNonblockingServerSocket t = null;
		try
		{
			t = new TNonblockingServerSocket(Integer.parseInt(port), timeout);
		} catch (NumberFormatException | TTransportException e1)
		{
			e1.printStackTrace();
		}
		THsHaServer.Args arg = new THsHaServer.Args(t);
		arg.protocolFactory(new TCompactProtocol.Factory());
		arg.transportFactory(new TFramedTransport.Factory());
		arg.processorFactory(new TProcessorFactory(processor));
		TServer server = new THsHaServer(arg);
		return server;
	}

	private TServer getNioActorServer(String port, int timeout, TProcessor processor)
	{
		TNonblockingServerSocket serverSocket = null;
		try
		{
			serverSocket = new TNonblockingServerSocket(Integer.parseInt(port), timeout);
		} catch (NumberFormatException | TTransportException e1)
		{
			e1.printStackTrace();
		}
		TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
		serverParams.protocolFactory(new TCompactProtocol.Factory());
		serverParams.transportFactory(new TFramedTransport.Factory());
		serverParams.processorFactory(new TProcessorFactory(processor));
		serverParams.acceptQueueSizePerThread(100);
		serverParams.selectorThreads(4);
		serverParams.workerThreads(15);
		TServer server = new TThreadedSelectorServer(serverParams);
		return server;
	}
	
	private TServer getNioActorOptimizeServer(String port, int timeout, TProcessor processor)
	{
		TNonblockingServerSocket serverSocket = null;
		try
		{
			serverSocket = new TNonblockingServerSocket(Integer.parseInt(port), timeout);
		} catch (NumberFormatException | TTransportException e1)
		{
			e1.printStackTrace();
		}
		TThreadedSelectorOptimizeServer.Args serverParams = new TThreadedSelectorOptimizeServer.Args(serverSocket);
		serverParams.protocolFactory(new TCompactProtocol.Factory());
		serverParams.transportFactory(new TFramedTransport.Factory());
		serverParams.processorFactory(new TProcessorFactory(processor));
		serverParams.selectorThreads(4);
		serverParams.workerThreads(10);
		TServer server = new TThreadedSelectorOptimizeServer(serverParams);
		return server;
	}
	private TServer getBioPoolServer(String port, int timeout, TProcessor processor)
	{
		TServerTransport serverSocket = null;
		try
		{
			serverSocket = new TServerSocket(Integer.parseInt(port));
		} catch (NumberFormatException | TTransportException e1)
		{
			e1.printStackTrace();
		}
		TThreadPoolServer.Args serverParams = new TThreadPoolServer.Args(serverSocket);
		serverParams.protocolFactory(new TCompactProtocol.Factory());
		serverParams.transportFactory(new TFramedTransport.Factory());
		serverParams.processorFactory(new TProcessorFactory(processor));
		TServer server = new TThreadPoolServer(serverParams);
		return server;
	}
}
