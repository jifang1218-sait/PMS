/**
 * 
 */
package com.pms.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.constants.PMSFileType;
import com.pms.constants.PMSRoleName;
import com.pms.controllers.exceptions.DuplicatedObjectsException;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSFile;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSRole;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.services.PMSEntityProvider;
import com.pms.services.PMSSecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jifang
 *
 */
@RestController
@RequestMapping(value="/api/v1/test", 
produces="application/json", 
consumes="application/json")
@Slf4j
public class TestController {
    
    private static final int kSize = 10;
    
    @Value("${file.upload.path}")
    private String path;
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @Autowired
    private PMSSecurityService securityService;
    
    @GetMapping(value="/init")
    public void init() {
    	createRoles();
    	createRootAccount();
    }
    
    private void createRoles() {
    	try {
	    	for (PMSRoleName roleType : PMSRoleName.values()) {
	    		PMSRole role = new PMSRole();
	    		role.setName(roleType);
	    		role.setDesc(roleType.name() + ":" + roleType.getValue());
	    		entityProvider.createRole(role);
	    	}
    	} catch (DuplicatedObjectsException e) {
    		//
    	}
    }
    
    private void createRootAccount() {
    	try {
    		PMSUser root = new PMSUser();
    		root.setEmail("root@sait.com");
    		root.setFirstName("root first");
    		root.setLastName("root last");
    		root.setPassword("root");
    		
    		// role
    		List<PMSRole> roles = new ArrayList<>();
    		PMSRole admin = entityProvider.getRoleByName(PMSRoleName.admin);
    		roles.add(admin);
    		root.setRoles(roles);
    		
    		// avatar
    		PMSFile avatar = new PMSFile();
    		avatar.setDisplayFilename("root_avatar");
    		avatar.setRealFilename(root.getEmail() + System.currentTimeMillis());
        	avatar.setFileType(PMSFileType.Image);
    		root.setAvatar(avatar);   
    		
    		entityProvider.createUser(root, null);
    	} catch (DuplicatedObjectsException e) {
    		//
    	}
    }
    
    @GetMapping(value="/loadData")
    public void loadData() {
    	login();
    	loadEntities();
    	loadUsers();
    }
    
    private void login() {
    	securityService.login("root@sait.com", "root");
    }
    
    private void loadEntities() {
    	// add company, project, task, comment etc.
        for (int a=0; a<kSize; ++a) {
            PMSCompany company = new PMSCompany();
            company.setName("company_name_" + a);
            company.setDesc("company_desc_" + a);
            
            PMSFile avatar = new PMSFile();
            avatar.setDisplayFilename("company_avatar_" + a);
            avatar.setRealFilename(company.getName() + System.currentTimeMillis());
            avatar.setFileType(PMSFileType.Image);
            company.setAvatar(avatar);
            
            entityProvider.createCompany(company);
            for (int b=0; b<kSize; ++b) {
                PMSProject project = new PMSProject();
                project.setCompanyId(company.getId());
                project.setDesc("project_desc_" + b);
                project.setName("project_name_" + b);
                
                avatar = new PMSFile();
                avatar.setDisplayFilename("project_avatar_" + b);
                avatar.setRealFilename(project.getName() + System.currentTimeMillis());
                avatar.setFileType(PMSFileType.Image);
                project.setAvatar(avatar);
                
                entityProvider.createProject(company.getId(), project);
                
                // add 10 comments to the project
                List<PMSComment> comments = new ArrayList<>();
                for (int tmp=0; tmp<kSize; ++tmp) {
                    PMSComment comment = new PMSComment();
                    comment.setTitle("comment_title_" + tmp);
                    comment.setDesc("comment_desc_" + tmp);
                    comment.setTaskId(project.getDefaultTask().getId());
                    
                    List<PMSFile> attachments = new ArrayList<>();
                    PMSFile file = new PMSFile();
                    file.setDisplayFilename("comment_attachment_" + tmp);
                    file.setRealFilename(comment.getTitle() + System.currentTimeMillis());
                    file.setFileType(PMSFileType.File);
                    attachments.add(file);
                    comment.setAttachments(attachments);
                    
                    entityProvider.createCommentForProject(project.getId(), comment);
                }
                
                // add 10 comments to the tasks of the project.
                for (int c=0; c<kSize; ++c) {
                	PMSTask task = new PMSTask();
                	task.setDesc("avatar_desc_" + c);
                    task.setName("task_name_" + c);
                    task.setProjectId(project.getId());
                    
                    avatar = new PMSFile();
                    avatar.setDisplayFilename("task_avatar_" + c);
                    avatar.setRealFilename(task.getName() + System.currentTimeMillis());
                    avatar.setFileType(PMSFileType.Image);
                    task.setAvatar(avatar);
                    
                    entityProvider.createTask(project.getId(), task);
                    comments.clear();
                    for (int d=0; d<kSize; ++d) {
                        PMSComment comment = new PMSComment();
                        comment.setDesc("comment_desc_" + d);
                        comment.setTitle("comment_title_" + d);
                        comment.setTaskId(task.getId());
                        
                        List<PMSFile> attachments = new ArrayList<>();
                        PMSFile file = new PMSFile();
                        file.setDisplayFilename("comment_attachment_" + d);
                        file.setRealFilename(comment.getTitle() + System.currentTimeMillis());
                        file.setFileType(PMSFileType.Image);
                        attachments.add(file);
                        comment.setAttachments(attachments);
                        
                        entityProvider.createCommentForTask(task.getId(), comment);
                    }
                }
            }
        }
    }
    
    private void loadUsers() {
    	List<PMSCompany> companies = entityProvider.getCompanies();
		int companyCount = companies.size();
		for (int i=0; i<kSize; ++i) {
        	PMSUser user = new PMSUser();
        	user.setEmail("email" + i + "@sait.com");
        	user.setFirstName("firstname" + i);
        	user.setLastName("lastname" + i);
        	user.setPassword("password" + i);
        	
        	PMSFile avatar = new PMSFile();
        	avatar.setDisplayFilename("avatar" + i);
        	avatar.setRealFilename(user.getEmail() + System.currentTimeMillis());
        	avatar.setFileType(PMSFileType.Image);
        	user.setAvatar(avatar);
        	
    		List<PMSRole> roles = entityProvider.getRoles();
        	switch (i%PMSRoleName.values().length) {
        	case 0: {
        		user.getRoles().add(roles.get(0));
        		user.getRoles().add(roles.get(1));
        		user.getRoles().add(roles.get(2));
        		user.getRoles().add(roles.get(3));
        	} break;
        	case 1: {
        		user.getRoles().add(roles.get(0));
        		user.getRoles().add(roles.get(1));
        		user.getRoles().add(roles.get(2));
        	} break;
        	case 2: {
        		user.getRoles().add(roles.get(0));
        		user.getRoles().add(roles.get(1));
        	} break;
        	case 3: {
        		user.getRoles().add(roles.get(0));
        	} break;
        	}
        	
			int companyIndex = i % companyCount;
			entityProvider.createUser(user, companies.get(companyIndex).getId());
        	
        }
    }
    
    @GetMapping("/deleteData")
    public void deleteData() {
    	deleteEntities();
    	deleteUsers();
    }
    
    private void deleteEntities() {
    	// delete companies
        List<Long> companyIds = new ArrayList<>();
        List<PMSCompany> companies = entityProvider.getCompanies();
        for (PMSCompany company : companies) {
            companyIds.add(company.getId());
        }
        entityProvider.cleanupCompanies(companyIds);
    }
    
    private void deleteUsers() {
    	List<PMSUser> users = entityProvider.getCompanyUsers();
    	
    	List<Long> userIds = new ArrayList<>();
    	for (PMSUser user : users) {
    		userIds.add(user.getId());
    	}
    	
    	entityProvider.deleteUsers(userIds);
    }
    
    @GetMapping("/test00")
    public void test00() throws FileNotFoundException {
    	String str = System.getProperty("user.dir");
    	log.info(str);
    	str = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    	log.info(str);
    	str = ResourceUtils.getURL("classpath:").getPath();
    	log.info(str);
    	log.info(path);
    	
    }
}
