package com.pms.services;

import java.io.File;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pms.constants.PMSFileType;
import com.pms.entities.PMSFile;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;

public class PMSActionProvider {

	public PMSProject startProject(Long projectId) {
		return null;
	}
	
	public PMSProject stopProject(Long projectId) {
		return null;
	}
	
	public PMSTask startTask(Long taskId) {
		return null;
	}
	
	public PMSTask stopTask(Long taskId) {
		return null;
	}
	
	// file upload
	public PMSFile upload(MultipartFile file, String displayName, PMSFileType fileType) {
		
	    // init directories
		String strUploadDir = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		strUploadDir += "../../upload";
	    File uploadDir = new File(strUploadDir);
	    if (!uploadDir.exists()) {
	    	uploadDir.mkdirs();
	    }
	    
	    // create avatars and files if neccessary. 
	    String strAvatarDir = strUploadDir + "/avatars";
	    File avatarDir = new File(strAvatarDir);
	    if (!avatarDir.exists()) {
	    	avatarDir.mkdirs();
	    }
	    String strFileDir = strUploadDir + "/files";
	    File fileDir = new File(strFileDir);
	    if (!fileDir.exists()) {
	    	fileDir.mkdirs();
	    }
	    
	    // use client's file name as display name if display name is not specified. 
	    String filename = null;
	    if (displayName != null) {
	    	filename = displayName;
	    } else {
	    	filename = file.getOriginalFilename();
	    }
	    long size = file.getSize();
	    
	    // create & save entity.
	    PMSFile entityFile = new PMSFile();
	    entityFile.setFileType(fileType);
	    entityFile.setDisplayFilename(filename);
	    entityFile.setSize(size);
	    entityFile.setRealFilename("" + System.currentTimeMillis());
	    
	    String str = null;
	    if (fileType == PMSFileType.Image) {
	    	str = avatarDir.getAbsolutePath() + File.separator + entityFile.getRealFilename();
	    } else {
	    	str = fileDir.getAbsolutePath() + File.separator + entityFile.getRealFilename();
	    }

	    try {
		    file.transferTo(new File(str));
		    entityFile = fileRepo.save(entityFile);
	    } catch (Exception e) {
	    	// TODO 
	    }
	    
	    return entityFile;
	}
}
