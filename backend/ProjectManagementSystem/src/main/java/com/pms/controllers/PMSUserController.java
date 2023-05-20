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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/users", 
        produces="application/json", consumes="application/json")
public class PMSUserController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping("")
    public List<PMSUser> getUsers() {
        return entityProvider.getUsers();
    }
    
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSUser createUser(@RequestBody PMSUser user) {
        return entityProvider.createUser(user);
    }
    
    @GetMapping(value="/{id}")
    public List<PMSUser> findUser(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return entityProvider.getUsersByIds(ids);
    }
    
    @PutMapping("/{id}")
    public PMSUser updateUser(@PathVariable("id") Long id, @RequestBody PMSUser user) {
        return entityProvider.updateUser(id, user);
    }
    
    @DeleteMapping(value="/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        entityProvider.deleteUsers(ids);
    }
    
    @GetMapping(value="/{id}/projects")
    public List<PMSProject> getProjectsByUserId(@PathVariable("id") Long userId) {
        return entityProvider.getProjectsByUserId(userId);
    }
    
    @GetMapping(value="/{userId}/tasks")
    public List<List<PMSTask>> getTasksByUserId(@PathVariable("userId") Long userId) {
        return entityProvider.getTasksByUserId(userId);
    }
}
