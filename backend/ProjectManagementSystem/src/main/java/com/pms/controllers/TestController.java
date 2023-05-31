/**
 * 
 */
package com.pms.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;

/**
 * @author jifang
 *
 */
@RestController
@RequestMapping(value="/v1/test", 
produces="application/json", 
consumes="application/json")
@Transactional
public class TestController {
    
    private static final int kSize = 10;
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    //@Autowired
    //private EntityManagerFactory emf;
    @GetMapping(value="/loadData")
    public void loadData() {
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
                    List<String> filePaths = new ArrayList<>();
                    filePaths.add("comment_filepath_" + tmp);
                    comment.setFilePaths(filePaths);
                    comment.setTaskId(project.getDefaultTask().getId());
                    comment.setTimestamp(a * kSize*kSize*kSize + b * kSize*kSize + tmp * kSize);
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
                        List<String> filePaths = new ArrayList<>();
                        filePaths.add("comment_filepath_" + d);
                        comment.setFilePaths(filePaths);
                        comment.setTaskId(task.getId());
                        comment.setTimestamp(a * kSize*kSize*kSize + b * kSize*kSize + c * kSize + d);
                        comment.setTitle("comment_title_" + d);
                        entityProvider.createCommentForTask(task.getId(), comment);
                    }
                }
            }
        }
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
}
