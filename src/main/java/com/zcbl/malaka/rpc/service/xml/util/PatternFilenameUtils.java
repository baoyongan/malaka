package com.zcbl.malaka.rpc.service.xml.util;

import java.io.File;
import java.io.FilenameFilter;

public class PatternFilenameUtils implements FilenameFilter {

	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		return (isRpc(name));
	}

	public boolean isRpc(String file) {
		if (file.toLowerCase().startsWith("malaka-service")) {
			return true;
		} else {
			return false;
		}
	}
}
