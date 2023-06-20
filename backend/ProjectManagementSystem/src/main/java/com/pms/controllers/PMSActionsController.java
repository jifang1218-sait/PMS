/**
 * 
 */
package com.pms.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSLoginInfo;
import com.pms.entities.PMSProject;
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
@RequestMapping(value="/api/v1/actions", 
            produces="application/json", 
            consumes="application/json")
@Slf4j
public class PMSActionsController {
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @Autowired
    private PMSSecurityService securityService;
    
    // project dependencies
    @GetMapping(value="/depend/projects/{project_id}")
    public List<PMSProject> getDependentProjects(@PathVariable("project_id") Long projectId) {
        return entityProvider.getDependentProjectsById(projectId);
    }
    
    @PostMapping(value="/depend/projects/{project_id}")
    public PMSProject addDependentProjects(@PathVariable("project_id") Long projectId, 
            @RequestBody List<Long> dependentProjectIds) {
        return entityProvider.addDependentProjects(projectId, dependentProjectIds);
    }
    
    @PutMapping(value="/depend/projects/{project_id}")
    public PMSProject setDependentProjects(@PathVariable("projectId") Long projectId, 
            @RequestBody List<Long> dependentProjectIds) {
        return entityProvider.setDependentProjects(projectId, dependentProjectIds);
    }
    
    @DeleteMapping(value="/depend/projects/{project_id}")
    public PMSProject removeDependentProjects(@PathVariable("projectId") Long projectId, 
            @RequestBody List<Long> dependentProjectIds) {
        return entityProvider.removeDependentProjects(projectId, dependentProjectIds);
    }
    
    // task dependencies
    @PostMapping(value="/depend/tasks/{task_id}")
    public PMSTask addDependentTasks(@PathVariable("task_id") Long taskId, 
            @RequestBody List<Long> dependentTaskIds) {
        return entityProvider.addDependentTasks(taskId, dependentTaskIds);
    }
    
    @PutMapping(value="/depend/tasks/{task_id}")
    public PMSTask setDependentTasks(@PathVariable("task_id") Long taskId, 
            @RequestBody List<Long> dependentTaskIds) {
        return entityProvider.setDependentTasks(taskId, dependentTaskIds);
    }
    
    @DeleteMapping(value="/depend/tasks/{task_id}")
    public PMSTask removeDependentTasks(@PathVariable("task_id") Long taskId, 
            @RequestBody List<Long> dependentTaskIds) {
        return entityProvider.removeDependentTasks(taskId, dependentTaskIds);
    }
    
    @GetMapping(value="/depend/tasks/{task_id}")
    public List<PMSTask> getDependentTasks(@PathVariable("task_id") Long taskId) {
        return entityProvider.getDependentTasks(taskId);
    }
    
    // assign users to task
    @PostMapping(value="/assign/tasks/{taskId}")
    public PMSTask addUsersToTask(@PathVariable("taskId") Long taskId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.addUsersToTask(taskId, userIds);
    }
    
    @PutMapping(value="/assign/tasks/{taskId}")
    public PMSTask setUsersToTask(@PathVariable("taskId") Long taskId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.setUsersToTask(taskId, userIds);
    }
    
    @DeleteMapping(value="/assign/tasks/{taskId}")
    public void removeUsersFromTask(@PathVariable("taskId") Long taskId, 
            @RequestBody List<Long> userIds) {
        entityProvider.removeUsersFromTask(taskId, userIds);
    }
    
    @GetMapping(value="/assign/tasks/{taskId}")
    public List<PMSUser> getUsersByTask(@PathVariable("taskId") Long taskId) {
        return entityProvider.getUsersByTaskId(taskId);
    }
 
    // assign users to project.
    @PostMapping(value="/assign/projects/{projectId}")
    public PMSTask addUsersToProject(@PathVariable("projectId") Long projectId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.addUsersToProject(projectId, userIds);
    }
    
    @PutMapping(value="/assign/projects/{projectId}")
    public PMSTask setUsersToProject(@PathVariable("projectId") Long projectId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.setUsersToProject(projectId, userIds);
    }
    
    @DeleteMapping(value="/assign/projects/{projectId}")
    public void removeUsersFromProject(@PathVariable("projectId") Long projectId, 
            @RequestBody List<Long> userIds) {
        entityProvider.removeUsersFromProject(projectId, userIds);
    }
    
    @GetMapping(value="/assign/projects/{projectId}")
    public List<PMSUser> getUsersByProject(@PathVariable("projectId") Long projectId) {
        return entityProvider.getUsersByProject(projectId);
    }
    
    @PostMapping(value="/login")
    public ResponseEntity<String> login(@RequestParam(value="logout", required=false, defaultValue="false") boolean logout, 
    		@RequestBody @Valid PMSLoginInfo loginInfo, BindingResult result) {
    	if (logout) {
    		log.debug("logout");
    		return new ResponseEntity<>(securityService.logout(), HttpStatus.OK);
    	}
    	
    	log.debug("login");
    	if (result.hasErrors()) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        } 
    	
    	return new ResponseEntity<>(securityService.login(loginInfo.getUsername(), loginInfo.getPassword()), HttpStatus.OK);
    }
    
}
