package com.pms.repositories;

import java.util.List;

public interface IPMSFileManager {
	boolean addFile(String filePath);
	boolean removeFiles(List<String> filePaths);
}
