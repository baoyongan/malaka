package com.zcbl.malaka.rpc.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetUtil
{
	public static final int START = 4000;
	public static final int END = 49152;

	public static boolean isLoclePortUsing(int port)
	{
		boolean flag = true;
		try
		{
			flag = isPortUsing("127.0.0.1", port);
		} catch (Exception e)
		{
		}
		return flag;
	}

	public static boolean isPortUsing(String host, int port) throws UnknownHostException
	{
		boolean flag = false;
		InetAddress theAddress = InetAddress.getByName(host);
		try
		{
			Socket socket = new Socket(theAddress, port);
			flag = true;
		} catch (IOException e)
		{
		}
		return flag;
	}

	public static String getPort()
	{
		for (int i = START; i < END; i++)
		{
			boolean b = NetUtil.isLoclePortUsing(i);
			if (!b)
			{
				return String.valueOf(i);
			}
		}
		return null;
	}

	public static String getIp()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress())
					{
						sb.append(inetAddress.getHostAddress().toString());
						return sb.toString();
					}
				}
			}
		} catch (SocketException ex)
		{
			ex.printStackTrace();
		}
		return sb.toString();
	}

	public static String getLocalIP()
	{
		try
		{
			if (isWindowsOS())
			{
				return InetAddress.getLocalHost().getHostAddress();
			} else
			{
				return getLinuxLocalIp();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断操作系统是否是Windows
	 *
	 * @return
	 */
	public static boolean isWindowsOS()
	{
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1)
		{
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 获取本地Host名称
	 */
	public static String getLocalHostName() throws UnknownHostException
	{
		return InetAddress.getLocalHost().getHostName();
	}

	/**
	 * 获取Linux下的IP地址
	 *
	 * @return IP地址
	 * @throws SocketException
	 */
	private static String getLinuxLocalIp() throws SocketException
	{
		String ip = "";
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo"))
				{
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
					{
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress())
						{
							String ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80"))
							{
								ip = ipaddress;
							}
						}
					}
				}
			}
		} catch (SocketException ex)
		{
			System.out.println("获取ip地址异常");
			ex.printStackTrace();
		}
		System.out.println("IP:" + ip);
		return ip;
	}

	public static void main(String[] args)
	{Socket socket_;
		try
		{
			try
			{
				socket_ = new Socket();
				socket_.connect(new InetSocketAddress("192.168.3.54", 4000), 3000);
				BufferedInputStream inputStream_ = new BufferedInputStream(socket_.getInputStream(), 1024);
				BufferedOutputStream outputStream_ = new BufferedOutputStream(socket_.getOutputStream(), 1024);
				outputStream_.write(234234324);
				System.out.println(socket_.isClosed());
				System.out.println(socket_.isConnected());
				//socket_.close();
				//socket_=null;
				inputStream_.close();
				//System.out.println(socket_.isClosed());
				System.out.println(socket_.isConnected());
				outputStream_.close();
				//System.out.println(socket_.isClosed());
				System.out.println(socket_.isConnected());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
