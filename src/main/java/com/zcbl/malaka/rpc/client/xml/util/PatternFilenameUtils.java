package com.zcbl.malaka.rpc.client.xml.util;

import java.io.File;
import java.io.FilenameFilter;

public class PatternFilenameUtils implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return (isRpc(name));
	}

	public boolean isRpc(String file) {
		if (file.toLowerCase().startsWith("malaka-client")) {
			return true;
		} else {
			return false;
		}
	}
}
