/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/tasks",
//            params={"project_id", "company_id"}, 
            produces="application/json", consumes="application/json")
public class PMSTaskController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping(value="")
    public List<PMSTask> getTasks(@RequestParam("projectId") Long projId) {
        if (projId == null) {
            return entityProvider.getTasks();
        } else {
            return entityProvider.getTasksByProjectId(projId);
        }
    }
    
    @GetMapping(value="/{taskId}")
    public List<PMSTask> findTask(@PathVariable("taskId") Long taskId) {
        List<Long> ids = new ArrayList<>();
        ids.add(taskId);
        return entityProvider.getTasksByIds(ids);
    }
    
    @PostMapping(value="")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSTask createTask(@RequestBody PMSTask task) {
        return entityProvider.createTask(task);
    }
    
    @PutMapping(value="/{taskId}")
    public PMSTask updateTask(@PathVariable("taskId") Long taskId, @RequestBody PMSTask task) {
        return entityProvider.updateTask(taskId, task);
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
        return entityProvider.getUsersForTask(taskId);
    }
}
