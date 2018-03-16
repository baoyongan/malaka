package com.zcbl.malaka.rpc.service.xml.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.zcbl.malaka.rpc.service.service.ServiceCacheManager;
import com.zcbl.malaka.rpc.service.xml.bean.Register;
import com.zcbl.malaka.rpc.service.xml.bean.Service;
import com.zcbl.malaka.rpc.service.xml.bean.Zookeeper;

public class ZookeeperServerUtils
{
	static ZookeeperServerUtils INSTANCE = new ZookeeperServerUtils();

	public static ZookeeperServerUtils getInstance()
	{
		return INSTANCE;
	}

	private ZookeeperServerUtils()
	{
	}

	public void start()
	{
		try
		{
			analysis("/");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Zookeeper digesterService(String url)
	{
		Zookeeper zookeeper = null;
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("malaka-service", Zookeeper.class);
		digester.addObjectCreate("malaka-service/register", Register.class);
		digester.addSetNext("malaka-service/register", "setRegister");
		digester.addBeanPropertySetter("malaka-service/register/server");
		digester.addBeanPropertySetter("malaka-service/register/esb");
		digester.addBeanPropertySetter("malaka-service/register/application");
		digester.addBeanPropertySetter("malaka-service/register/bind-ip", "local");
		digester.addBeanPropertySetter("malaka-service/register/service-port", "servicePort");
		digester.addBeanPropertySetter("malaka-service/register/heart");
		digester.addBeanPropertySetter("malaka-service/register/scan");
		digester.addObjectCreate("malaka-service/service", Service.class);
		digester.addSetNext("malaka-service/service", "addService");
		digester.addBeanPropertySetter("malaka-service/service/interface", "inter");
		digester.addBeanPropertySetter("malaka-service/service/version");
		digester.addBeanPropertySetter("malaka-service/service/impl", "cls");
		digester.addBeanPropertySetter("malaka-service/service/port");
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

	public void buildAllService(String path)
	{
		Zookeeper zookeeper = digesterService(path);
		if (zookeeper == null)
			return;
		else
		{
			ServiceCacheManager.getInstance().setRegister(zookeeper);
		}
	}

	public void analysis(String path) throws Exception
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
