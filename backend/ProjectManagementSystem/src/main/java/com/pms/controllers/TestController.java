/**
 * 
 */
package com.pms.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.constants.PMSRoleType;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSRole;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.services.PMSEntityProvider;

/**
 * @author jifang
 *
 */
@RestController
@RequestMapping(value="/api/v1/test", 
produces="application/json", 
consumes="application/json")
public class TestController {
    
    private static final int kSize = 10;
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping(value="/loadEntities")
    public void loadEntities() {
    	// add company, project, task, comment etc.
        for (int a=0; a<kSize; ++a) {
            PMSCompany company = new PMSCompany();
            company.setAvatar("company_avatar_" + a);
            company.setName("company_name_" + a);
            company.setDesc("company_desc_" + a);
            entityProvider.createCompany(company);
            for (int b=0; b<kSize; ++b) {
                PMSProject project = new PMSProject();
                project.setCompanyId(company.getId());
                project.setAvatar("project_avatar_" + b);
                project.setDesc("project_desc_" + b);
                project.setName("project_name_" + b);
                entityProvider.createProject(company.getId(), project);
                
                // add 10 comments to the project
                List<PMSComment> comments = new ArrayList<>();
                for (int tmp=0; tmp<kSize; ++tmp) {
                    PMSComment comment = new PMSComment();
                    comment.setDesc("comment_desc_" + tmp);
                    List<String> attachments = new ArrayList<>();
                    attachments.add("comment_attachment_" + tmp);
                    comment.setAttachments(attachments);
                    comment.setTaskId(project.getDefaultTask().getId());
                    comment.setTitle("comment_title_" + tmp);
                    entityProvider.createCommentForProject(project.getId(), comment);
                }
                
                // add 10 comments to the tasks of the project.
                for (int c=0; c<kSize; ++c) {
                    PMSTask task = new PMSTask();
                    task.setAvatar("task_avatar_" + c);
                    task.setDesc("avatar_desc_" + c);
                    task.setName("task_name_" + c);
                    task.setProjectId(project.getId());
                    entityProvider.createTask(project.getId(), task);
                    comments.clear();
                    for (int d=0; d<kSize; ++d) {
                        PMSComment comment = new PMSComment();
                        comment.setDesc("comment_desc_" + d);
                        List<String> attachments = new ArrayList<>();
                        attachments.add("comment_attachment_" + d);
                        comment.setAttachments(attachments);
                        comment.setTaskId(task.getId());
                        comment.setTitle("comment_title_" + d);
                        entityProvider.createCommentForTask(task.getId(), comment);
                    }
                }
            }
        }
    }
    
    @GetMapping(value="/loadRolesAndUsers")
    public void loadRolesAndUsers() {
    	// add role
    	List<PMSRole> roles = new ArrayList<>();
    	for (PMSRoleType roleType : PMSRoleType.values()) {
    		PMSRole role = new PMSRole();
    		role.setName(roleType);
    		role.setDesc(roleType.name() + ":" + roleType.getValue());
    		entityProvider.createRole(role);
    		roles.add(role);
    	}
    	
		List<PMSCompany> companies = entityProvider.getCompanies();
		int companyCount = companies.size();
		for (int i=0; i<kSize; ++i) {
        	PMSUser user = new PMSUser();
        	user.setAvatar("avatar" + i);
        	user.setEmail("email" + i + "@sait.com");
        	user.setFirstName("firstname" + i);
        	user.setLastName("lastname" + i);
        	user.setPassword("password" + i);
    		
        	switch (i%PMSRoleType.values().length) {
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
    
    @GetMapping(value="/loadData")
    public void loadData() {
    	loadEntities();
    	loadRolesAndUsers();
    }
    
    @GetMapping("/deleteData")
    public void deleteData() {
        // delete companies
        List<Long> companyIds = new ArrayList<>();
        List<PMSCompany> companies = entityProvider.getCompanies();
        for (PMSCompany company : companies) {
            companyIds.add(company.getId());
        }
        entityProvider.cleanupCompanies(companyIds);
    }
    
    @GetMapping("/test00")
    public void test00() throws FileNotFoundException {
    	String str = System.getProperty("user.dir");
    	str = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    	str = ResourceUtils.getURL("classpath:").getPath();
    }
}
