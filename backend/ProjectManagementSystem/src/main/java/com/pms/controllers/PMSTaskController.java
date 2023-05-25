/**
 * 
 */
package com.pms.controllers;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.ArrayList;

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
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSComment;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/v1/entities/tasks",
//            params={"project_id", "company_id"}, 
            produces="application/json", consumes="application/json")
@Transactional
public class PMSTaskController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping
    public List<PMSTask> getTasks(@RequestParam("project_id") Long projectId) {
        if (projectId == null) {
            return entityProvider.getTasks();
        } else {
            return entityProvider.getTasksByProjectId(projectId);
        }
    }
    
    @GetMapping(value="/{taskId}")
    public List<PMSTask> findTask(@PathVariable("taskId") Long taskId) {
        List<Long> ids = new ArrayList<>();
        ids.add(taskId);
        return entityProvider.getTasksByIds(ids);
    }
    
    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PMSTask> createTask(@RequestBody @Valid PMSTask task, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(task, HttpStatus.BAD_REQUEST);
        } 
        
        return new ResponseEntity<>(entityProvider.createTask(task), HttpStatus.CREATED);
    }
    
    @PutMapping(value="/{taskId}")
    public ResponseEntity<PMSTask> updateTask(@PathVariable("taskId") Long taskId, 
            @RequestBody @Valid PMSTask task, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(task, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateTask(taskId, task), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/{taskId}")
    public void deleteTask(@PathVariable("taskId") Long taskId) {
        List<Long> ids = new ArrayList<>();
        ids.add(taskId);
        entityProvider.deleteTasks(ids);
    }
    
    // dependencies
    @PostMapping(value="/{taskId}/dependencies/")
    public PMSTask addDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.addDependentTasks(taskId, dependentIds);
    }
    
    @PutMapping(value="/{taskId}/dependencies/")
    public PMSTask setDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.setDependentTasks(taskId, dependentIds);
    }
    
    @DeleteMapping(value="/{taskId}/dependencies")
    public PMSTask removeDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.removeDependentTasks(taskId, dependentIds);
    }
    
    @GetMapping(value="/{taskId}/dependencies")
    public List<PMSTask> findDependentTasks(@PathVariable("taskId") long taskId) {
        return entityProvider.getDependentTasks(taskId);
    }
    
    // assign users
    @PostMapping(value="/{taskId}/users")
    public PMSTask addUsers(@PathVariable("taskId") long taskId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.addUsersToTask(taskId, userIds);
    }
    
    @PutMapping(value="/{taskId}/users")
    public PMSTask setUsers(@PathVariable("taskId") long taskId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.setUsersToTask(taskId, userIds);
    }
    
    @DeleteMapping(value="/{taskId}/users")
    public void removeUsers(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> userIds) {
        entityProvider.removeUsersFromTask(taskId, userIds);
    }
    
    @GetMapping(value="/{taskId}/users")
    public List<PMSUser> findUsers(@PathVariable("taskId") long taskId) {
        return entityProvider.getUsersByTask(taskId);
    }
    
    // comments
    @GetMapping("/{taskId}/comments")
    public List<PMSComment> findComments(@PathVariable("taskId") long taskId) {
        return entityProvider.getCommentsByTask(taskId);
    }
    
    @PostMapping("/{taskId}/comments")
    public PMSComment addComments(@PathVariable("taskId") long taskId, 
            @RequestBody PMSComment comment) {
        return entityProvider.createCommentForTask(taskId, comment);
    }
    
    @DeleteMapping("/{taskId}/comments")
    public void deleteComments(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> commentIds) {
        entityProvider.deleteComments(commentIds);
    }
}
