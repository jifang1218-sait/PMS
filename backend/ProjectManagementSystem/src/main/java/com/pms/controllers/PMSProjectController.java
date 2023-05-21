/**
 * 
 */
package com.pms.controllers;

import java.util.List;

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
import com.pms.entities.PMSProject;
import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/projects",  
        consumes="application/json", 
        produces="application/json")
public class PMSProjectController {

    @Autowired
    PMSEntityProvider entityProvider;

    @GetMapping(value="")
    public List<PMSProject> getProjects(@RequestParam(value="companyId") Long companyId) {
    	if (companyId == null) {
    	    return entityProvider.getProjects();
    	} else { 
    	    return entityProvider.getProjectsByCompanyId(companyId);
    	}
    }
    
    @GetMapping(value="/{projectId}")
    public List<PMSProject> findProject(@PathVariable("projectId") Long projectId) {
        List<Long> ids = new ArrayList<>();
        ids.add(projectId);
        return entityProvider.getProjectsByIds(ids);        
    }
    
    @PostMapping(value="")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PMSProject> createProject(@RequestBody @Valid PMSProject project, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }
        
    	return new ResponseEntity<>(entityProvider.createProject(project), HttpStatus.CREATED);
    }
    
    @PutMapping(value="/{projectId}")
    public ResponseEntity<PMSProject> updateProject(@PathVariable("projectId") Long projectId, 
            @RequestBody @Valid PMSProject project, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateProject(projectId, project), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long projectId) {
        List<Long> ids = new ArrayList<>();
        ids.add(projectId);
        entityProvider.deleteProjects(ids);
    }
    
    // dependencies
    @PostMapping(value="/{projectId}/dependencies/")
    public PMSProject addDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.addDependentProjects(projectId, dependentIds);
    }
    
    @PutMapping(value="/{projectId}/dependencies/")
    public PMSProject setDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.setDependentProjects(projectId, dependentIds);
    }
    
    @DeleteMapping(value="/{projectId}/dependencies")
    public PMSProject removeDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        return entityProvider.removeDependentProjects(projectId, dependentIds);
    }
    
    @GetMapping(value="/{projectId}/dependencies")
    public List<PMSProject> findDependentProjects(@PathVariable("projectId") long projectId) {
        return entityProvider.getDependentProjectsById(projectId);
    }
    
    // assign users
    @PostMapping(value="/{projectId}/users")
    public PMSProject addUsers(@PathVariable("projectId") long projectId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.addUsersToProject(projectId, userIds);
    }
    
    @PutMapping(value="/{projectId}/users")
    public PMSProject setUsers(@PathVariable("projectId") long projectId, 
                @RequestBody List<Long> userIds) {
        return entityProvider.setUsersToProject(projectId, userIds);
    }
    
    @DeleteMapping(value="/{projectId}/users")
    public void removeUsers(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> userIds) {
        entityProvider.removeUsersFromProject(projectId, userIds);
    }
    
    @GetMapping(value="/{projectId}/users")
    public List<PMSUser> findUsers(@PathVariable("projectId") long projectId) {
        return entityProvider.getUsersByProject(projectId);
    }
    
    // comments
    @GetMapping("/{projectId}/comments")
    public List<PMSComment> findComments(@PathVariable("projectId") long projectId) {
        return entityProvider.getCommentsByProject(projectId);
    }
    
    @PostMapping("/{projectId}/comments")
    public PMSProject addComments(@PathVariable("projectId") long projectId, 
            @RequestBody List<PMSComment> comments) {
        return entityProvider.addCommentsToProject(projectId, comments);
    }
    
    public PMSProject deleteComments(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> commentIds) {
        return entityProvider.deleteCommentsFromProject(projectId, commentIds);
    }
    
}