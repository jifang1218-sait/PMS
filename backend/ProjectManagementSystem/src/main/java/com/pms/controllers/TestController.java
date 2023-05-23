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
    @Autowired
    private PMSEntityProvider entityProvider;
    
    //@Autowired
    //private EntityManagerFactory emf;
    
    @GetMapping(value="/loadData")
    public void loadData() {
        for (int a=0; a<10; ++a) {
            PMSCompany company = new PMSCompany();
            company.setAvatar("company_avatar_" + a);
            company.setName("company_name_" + a);
            company.setDesc("company_desc_" + a);
            entityProvider.createCompany(company);
            for (int b=0; b<10; ++b) {
                PMSProject project = new PMSProject();
                project.setCompanyId(company.getId());
                project.setAvatar("project_avatar_" + b);
                project.setDesc("project_desc_" + b);
                project.setName("project_name_" + b);
                entityProvider.createProject(project);
                
                // add 10 comments to the project
                List<PMSComment> comments = new ArrayList<>();
                for (int tmp=0; tmp<10; ++tmp) {
                    PMSComment comment = new PMSComment();
                    comment.setDesc("comment_desc_" + tmp);
                    comment.setFilePath("comment_filepath_" + tmp);
                    comment.setTaskId(project.getDefaultTaskId());
                    comment.setTimestamp(a * 1000 + b * 100 + tmp * 10);
                    comment.setTitle("comment_title_" + tmp);
                    comments.add(comment);
                }
                entityProvider.addCommentsToProject(project.getId(), comments);
                
                for (int c=0; c<10; ++c) {
                    PMSTask task = new PMSTask();
                    task.setAvatar("task_avatar_" + c);
                    task.setDesc("avatar_desc_" + c);
                    task.setName("task_name_" + c);
                    task.setProjectId(project.getId());
                    entityProvider.createTask(task);
                    comments.clear();
                    for (int d=0; d<10; ++d) {
                        PMSComment comment = new PMSComment();
                        comment.setDesc("comment_desc_" + d);
                        comment.setFilePath("comment_filepath_" + d);
                        comment.setTaskId(task.getId());
                        comment.setTimestamp(a * 1000 + b * 100 + c * 10 + d);
                        comment.setTitle("comment_title_" + d);
                        comments.add(comment);
                    }
                    entityProvider.addCommentsToTask(task.getId(), comments);
                }
            }
        }
    }
}
