package com.zcbl.malaka.rpc.client.xml.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.zcbl.malaka.rpc.client.xml.bean.Register;
import com.zcbl.malaka.rpc.client.xml.bean.Service;
import com.zcbl.malaka.rpc.client.xml.bean.Zookeeper;
import com.zcbl.malaka.rpc.client.xml.cache.ZookeeperClientCache;

/**
 * @author jys 2016年8月24日
 */
public class ZookeeperClientUtils
{
	static ZookeeperClientUtils INSTANCE = new ZookeeperClientUtils();

	public static ZookeeperClientUtils getInstance()
	{
		return INSTANCE;
	}

	private ZookeeperClientUtils()
	{

	}

	public void start()
	{
		analysis("/");
	}

	public Zookeeper digesterService(String url)
	{
		Zookeeper zookeeper = null;
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("malaka-client", Zookeeper.class);
		digester.addObjectCreate("malaka-client/register", Register.class);
		digester.addSetNext("malaka-client/register", "setRegister");
		digester.addBeanPropertySetter("malaka-client/register/server");
		digester.addBeanPropertySetter("malaka-client/register/esb");
		digester.addBeanPropertySetter("malaka-client/register/application");
		digester.addBeanPropertySetter("malaka-client/register/ip");
		digester.addObjectCreate("malaka-client/service", Service.class);
		digester.addSetNext("malaka-client/service", "addService");
		digester.addBeanPropertySetter("malaka-client/service/interface", "inter");
		digester.addBeanPropertySetter("malaka-client/service/version");
		digester.addBeanPropertySetter("malaka-client/service/protocol");
		digester.addBeanPropertySetter("malaka-client/service/route");
		digester.addBeanPropertySetter("malaka-client/service/port");
		digester.addBeanPropertySetter("malaka-client/service/call");
		try
		{
			InputStream input = this.getClass().getResourceAsStream(url);
			zookeeper = (Zookeeper) digester.parse(input);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		}
		return zookeeper;
	}

	public void buildAllService(String filePath)
	{
		Zookeeper zookeeper = digesterService(filePath);
		if (zookeeper == null)
			return;
		else
		{
			ZookeeperClientCache.getInstance().setRegister(zookeeper);
		}
	}

	public void analysis(String path)
	{
		try
		{
			URL url = this.getClass().getResource(path);
			File file = new File(url.toURI().getPath());
			File[] files = file.listFiles(new PatternFilenameUtils());
			if (files != null && files.length > 0)
			{
				for (File f : files)
				{
					buildAllService("/" + f.getName());
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
