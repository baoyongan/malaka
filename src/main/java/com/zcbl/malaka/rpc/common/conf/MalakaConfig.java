package com.zcbl.malaka.rpc.common.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

public class MalakaConfig implements ConfigInter
{

	private static MalakaConfig config = new MalakaConfig();
	private Map<String, Map<String, String>> global = new ConcurrentHashMap<String, Map<String, String>>();
	Lock lock = new ReentrantLock();

	private MalakaConfig()
	{
	}

	public Map<String, Map<String, String>> getGlobal()
	{
		return global;
	}

	public static MalakaConfig getInstance()
	{
		return config;
	}

	protected Map<String, String> getAttrs(Element ele)
	{
		Map<String, String> att = new HashMap<String, String>();
		int total = ele.attributeCount();
		for (int i = 0; i < total; i++)
		{
			Attribute attr = ele.attribute(i);
			att.put(attr.getName(), attr.getValue());
		}
		return att;
	}

	@Override
	public void anaylsis(Document doc)
	{
		lock.lock();
		try
		{
			global.clear();
			Element root = doc.getRootElement();
			if (root.getName().equals("malaka"))
			{
				for (@SuppressWarnings("unchecked")
				Iterator<Element> iter = root.elementIterator(); iter.hasNext();)
				{
					Element element = (Element) iter.next();
					Map<String, String> map = getAttrs(element);
					Map<String, String> atr = global.get(element.getName());
					if (atr == null)
					{
						global.put(element.getName(), map);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		lock.unlock();
	}
}
