/**
 * 
 */
package com.pms.controllers;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.services.PMSEntityProvider;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/v1/old/entities/users", 
        produces="application/json", consumes="application/json")
public class PMSUserController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    private final static Logger logger =
            LoggerFactory.getLogger(PMSUserController.class);
    
    @GetMapping
    public List<PMSUser> getUsers() {
        return entityProvider.getUsers();
    }
    
    @PostMapping
    public ResponseEntity<PMSUser> createUser(@RequestBody @Valid PMSUser user, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.createUser(user), HttpStatus.CREATED);
    }
    
    @GetMapping(value="/{id}")
    public List<PMSUser> findUser(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return entityProvider.getUsersByIds(ids);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PMSUser> updateUser(@PathVariable("id") Long id, 
            @RequestBody @Valid PMSUser user, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateUser(id, user), HttpStatus.OK);
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
