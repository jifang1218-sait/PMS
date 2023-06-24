package com.pms.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/api/v1/actions/files", 
            produces="application/json", 
            consumes="application/json")
@Slf4j
public class PMSFileUploadController {
	@Value("${file.upload.path}")
    private String path;
	
	@PostMapping("/upload")
	public String create(@RequestPart MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = path + fileName;

        File dest = new File(filePath);
        Files.copy(file.getInputStream(), dest.toPath());
        return "Upload file success : " + dest.getAbsolutePath();
    }
	
	
	public String upload(@RequestParam("imgFile") MultipartFile file, @RequestParam("imgName") String name) throws Exception {
		
	    // 设置上传至项目文件夹下的uploadFile文件夹中，没有文件夹则创建
		
	    File dir = new File("uploadFile");
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }
	    file.transferTo(new File(dir.getAbsolutePath() + File.separator + name + ".png"));
	    return "上传完成！文件名：" + name;
	}
	
	//@PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/admin")
	public void kkk00() {
		log.info("can go here.");
	}
	
	@PreAuthorize("hasAuthority('viewer')")
	@GetMapping("/viewer")
	public void kkk01() {
		log.info("can go here.");
	}
	
	
}
