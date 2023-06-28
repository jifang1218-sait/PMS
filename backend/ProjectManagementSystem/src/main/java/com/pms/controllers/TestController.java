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
import com.pms.constants.PMSPriority;
import com.pms.constants.PMSRoleName;
import com.pms.controllers.exceptions.DuplicateObjectsException;
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
    	} catch (DuplicateObjectsException e) {
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
    		PMSRole admin = entityProvider.getRole(PMSRoleName.admin);
    		roles.add(admin);
    		root.setRoles(roles);
    		
    		// avatar
    		PMSFile avatar = new PMSFile();
    		avatar.setDisplayFilename("root_avatar");
    		avatar.setRealFilename(root.getEmail() + System.currentTimeMillis());
        	avatar.setFileType(PMSFileType.Image);
    		root.setAvatar(avatar);   
    		
    		entityProvider.createUser(root, null);
    	} catch (DuplicateObjectsException e) {
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
        	// create company
            PMSCompany company = new PMSCompany();
            company.setName("company_" + a + "_name");
            company.setDesc(company.getName() + "_desc");
            
            PMSFile avatar = null;
            if (a % 2 == 0) {
            	avatar = new PMSFile();
	            avatar.setDisplayFilename(company.getName() + "_avatar");
	            avatar.setRealFilename(avatar.getDisplayFilename() + "_" + System.currentTimeMillis());
	            avatar.setFileType(PMSFileType.Image);
	            company.setAvatar(avatar);
            }
            
            entityProvider.createCompany(company);
            
            for (int b=0; b<kSize; ++b) {
            	// create project
                PMSProject project = new PMSProject();
                project.setCompanyId(company.getId());
                project.setName(company.getName() + "_project_" + b);
                project.setDesc(project.getName() + "_desc");
                
                if (b % 2 == 0) {
	                avatar = new PMSFile();
	                avatar.setDisplayFilename(project.getName() + "_avatar");
	                avatar.setRealFilename(avatar.getDisplayFilename() + "_" + System.currentTimeMillis());
	                avatar.setFileType(PMSFileType.Image);
	                project.setAvatar(avatar);
                }
                
                entityProvider.createProject(company.getId(), project);

                for (int projectComment=0; projectComment<kSize; ++projectComment) {
                	// create comment for the project. 
                    PMSComment comment = new PMSComment();
                    comment.setTitle(project.getName() + "_comment_title");
                    comment.setDesc(comment.getTitle() + "_desc");
                    comment.setTaskId(project.getDefaultTask().getId());
                                        
                    entityProvider.createCommentForProject(project.getId(), comment);
                }
                
                // create tasks for the project
                for (int c=0; c<kSize; ++c) {
                	// create task
                	PMSTask task = new PMSTask();
                	task.setName(project.getName() + "_task_" + c);
                	task.setDesc(task.getName() + "_desc");
                    task.setProjectId(project.getId());
                    
                    switch (c % 3) {
                    case 0: {
                    	task.setPriority(PMSPriority.Higher);
                    } break;
                    case 1: {
                    	task.setPriority(PMSPriority.Lower);
                    } break;
                    	// default, don't set priority, use default value. 
                    }
                    
                    if (c % 2 == 0) {
	                    avatar = new PMSFile();
	                    avatar.setDisplayFilename(task.getName() + "_avatar");
	                    avatar.setRealFilename(avatar.getDisplayFilename() + "_" + System.currentTimeMillis());
	                    avatar.setFileType(PMSFileType.Image);
	                    task.setAvatar(avatar);
                    }
                    
                    entityProvider.createTask(company.getId(), project.getId(), task);

                    for (int d=0; d<kSize; ++d) {
                    	// create comment for the task
                        PMSComment comment = new PMSComment();
                        
                        comment.setTitle(task.getName() + "_comment_title");
                        comment.setDesc(comment.getTitle() + "_desc");
                        comment.setTaskId(task.getId());
                        
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
        	user.setEmail("email_" + i + "@sait.com");
        	user.setFirstName("firstname_" + i);
        	user.setLastName("lastname_" + i);
        	user.setPassword("password");
        	
        	if (i % 2 == 0) {
        	PMSFile avatar = new PMSFile();
	        	avatar.setDisplayFilename(user.getEmail() + "_avatar");
	        	avatar.setRealFilename(avatar.getRealFilename() + "_" + System.currentTimeMillis());
	        	avatar.setFileType(PMSFileType.Image);
	        	user.setAvatar(avatar);
        	}
        	
    		List<PMSRole> roles = entityProvider.getRoles();
    		user.addRole(roles.get(i % roles.size()));
    		
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
