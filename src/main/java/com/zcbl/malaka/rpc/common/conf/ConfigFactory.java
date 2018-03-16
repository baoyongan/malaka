package com.zcbl.malaka.rpc.common.conf;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.zcbl.malaka.rpc.common.register.ServerContext;

/**
 * @author jys 2016年11月8日
 */
public class ConfigFactory
{
	public static List<ConfigInter> list = new ArrayList<ConfigInter>();

	public static void anysis()
	{
		list.add(MalakaConfig.getInstance());
		analysis("/");
		register();
	}

	public static void addConfigInter(ConfigInter iter)
	{
		list.add(iter);
		analysis("/");
	}

	public static void register()
	{
		Map<String, String> map = MalakaConfig.getInstance().getGlobal().get("register");
		if (map != null)
		{
			if (map.get("application") != null)
				ServerContext.getInstance().setApplication(map.get("application"));
			if (map.get("server") != null)
				ServerContext.getInstance().setServer(map.get("server"));
			if (map.get("version") != null)
				ServerContext.getInstance().setVersion(map.get("version"));
			if (map.get("heart") != null)
				ServerContext.getInstance().setHeart(map.get("heart"));
			if (map.get("bind-ip") != null)
				ServerContext.getInstance().setIp(map.get("bind-ip"));
			if (map.get("scan") != null)
				ServerContext.getInstance().setScan(map.get("scan"));
			if (map.get("server") != null)
				ServerContext.getInstance().setServer(map.get("server"));
			if (map.get("port") != null)
				ServerContext.getInstance().setPort(map.get("port"));
			if (map.get("out") != null)
				ServerContext.getInstance().setOut(map.get("out"));
			if (map.get("esb") != null)
				ServerContext.getInstance().setEsb(map.get("esb"));
		}
	}

	public static void analysis(String path)
	{
		try
		{
			URL url = ConfigFactory.class.getResource(path);
			File file = new File(url.toURI().getPath());
			File[] files = file.listFiles(new PatternFilenameUtils());
			if (files != null && files.length > 0)
			{
				for (File f : files)
				{
					build("/" + f.getName());
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void build(String path)
	{
		SAXReader reader = new SAXReader();
		InputStream inputStream = ConfigFactory.class.getResourceAsStream(path);
		try
		{
			Document d = reader.read(inputStream);
			if (inputStream != null)
			{
				for (ConfigInter inter : list)
				{
					inter.anaylsis(d);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				inputStream.close();
				reader = null;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void build(InputStream inputStream)
	{
		SAXReader reader = new SAXReader();
		if (inputStream != null)
		{
			try
			{
				Document d = reader.read(inputStream);
				for (ConfigInter inter : list)
				{
					inter.anaylsis(d);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					inputStream.close();
					reader = null;
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}
}
