package com.zcbl.malaka.rpc.common.conf;

import java.io.File;
import java.io.FilenameFilter;

public class PatternFilenameUtils implements FilenameFilter
{

	public boolean accept(File dir, String name)
	{
		return (isCompent(name));
	}

	public boolean isCompent(String file)
	{
		if (file.toLowerCase().startsWith("malaka"))
		{
			return true;
		} else
		{
			return false;
		}
	}
}
