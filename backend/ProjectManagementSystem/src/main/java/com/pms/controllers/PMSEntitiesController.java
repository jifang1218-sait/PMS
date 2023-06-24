/**
 * 
 */
package com.pms.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.pms.constants.EntityConstants;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSRole;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.services.PMSEntityProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jifang
 *
 */
@RestController
@RequestMapping(value="/api/v1/entities", 
            produces="application/json", 
            consumes="application/json")
@Slf4j
public class PMSEntitiesController {
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies")
    public List<PMSCompany> getCompanies() {
        return entityProvider.getCompanies();
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/companies")
    public ResponseEntity<PMSCompany> createCompany(@RequestBody @Valid PMSCompany comp, BindingResult result) {
        if(result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.createCompany(comp), HttpStatus.CREATED);
    } 
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies")
    public void deleteCompanies(@RequestBody List<Long> companyIds) {
        entityProvider.cleanupCompanies(companyIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{id}")
    public ResponseEntity<PMSCompany> getCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        
        return new ResponseEntity<>(entityProvider.getCompaniesByIds(ids).get(0), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/companies/{id}")
    public ResponseEntity<PMSCompany> updateCompany(@PathVariable("id") Long id, 
            @RequestBody @Valid PMSCompany comp, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        } 
        
        return new ResponseEntity<>(entityProvider.updateCompany(id, comp), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        entityProvider.cleanupCompanies(ids);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects")
    public ResponseEntity<List<PMSProject>> getProjects(@PathVariable("company_id") Long companyId) {
            return new ResponseEntity<>(entityProvider.getProjectsByCompanyId(companyId), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/companies/{company_id}/projects")
    public ResponseEntity<PMSProject> createProject(@PathVariable("company_id") Long companyId, 
            @RequestBody @Valid PMSProject project, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.createProject(companyId, project), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/companies/{company_id}/projects/{projectId}")
    public ResponseEntity<PMSProject> updateProject(@PathVariable("company_id") Long companyId, 
            @PathVariable("projectId") Long projectId, 
            @RequestBody @Valid PMSProject project, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateProject(projectId, project), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{projectId}")
    public void deleteProject(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId) {
        List<Long> ids = new ArrayList<>();
        ids.add(projectId);
        entityProvider.cleanupProjects(ids);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects")
    public void deleteProjects(@PathVariable("company_id") Long companyId, 
            @RequestBody List<Long> projectIds) {
        entityProvider.cleanupProjects(projectIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{project_id}")
    public ResponseEntity<PMSProject> getProject(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId) {
        List<Long> ids = new ArrayList<>();
        ids.add(projectId);
        
        return new ResponseEntity<>(entityProvider.getProjectsByIds(ids).get(0), HttpStatus.OK);        
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{projectId}/tasks")
    public ResponseEntity<List<PMSTask>> getTasks(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId) {
    	List<PMSTask> ret = null;
        if (projectId == null) {
        	ret = entityProvider.getTasks();
        } else {
            ret = entityProvider.getTasksByProjectId(projectId);
        }
        
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/companies/{company_id}/projects/{projectId}/tasks")
    public ResponseEntity<PMSTask> createTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @RequestBody @Valid PMSTask task, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(task, HttpStatus.BAD_REQUEST);
        } 
        
        return new ResponseEntity<>(entityProvider.createTask(projectId, task), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{project_id}/tasks")
    public void deleteTasks(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, @RequestBody List<Long> taskIds) {
        entityProvider.cleanupTasks(taskIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{projectId}/tasks/{task_id}")
    public ResponseEntity<PMSTask> getTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId) {
        List<Long> ids = new ArrayList<>();
        ids.add(taskId);
        
        return new ResponseEntity<>(entityProvider.getTasksByIds(ids).get(0), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/companies/{company_id}/projects/{projectId}/tasks/{task_id}")
    public ResponseEntity<PMSTask> updateTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId,  
            @RequestBody @Valid PMSTask task, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(task, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateTask(taskId, task), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{projectId}/tasks/{task_id}")
    public void deleteTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId) {
        List<Long> ids = new ArrayList<>();
        ids.add(taskId);
        entityProvider.cleanupTasks(ids);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{project_id}/comments")
    public ResponseEntity<List<List<PMSComment>>> getCommentsForProject(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @RequestParam(value="project_only", required=false, defaultValue="false") boolean projectOnly) {
        List<List<PMSComment>> ret = null;
        if (projectOnly == true) {
        	log.debug("only get comments for the project, comments in its tasks are excluded.");
            ret = new ArrayList<>();
            ret.add(entityProvider.getCommentsForProjectOnly(projectId));
        } else {
        	log.debug("get comments for the project and its tasks.");
            ret = entityProvider.getCommentsByProject(projectId);
        }
        
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/companies/{company_id}/projects/{project_id}/comments")
    public ResponseEntity<PMSComment> createCommentForProject(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @RequestBody @Valid PMSComment comment, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<PMSComment>(entityProvider.createCommentForProject(projectId, comment), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{project_id}/comments")
    public void deleteComments(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @RequestBody List<Long> commentIds) {
        entityProvider.cleanupComments(commentIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{project_id}/comments/{comment_id}")
    public ResponseEntity<PMSComment> getComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId) {
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(commentId);
      
        return new ResponseEntity<PMSComment>(entityProvider.getComments(commentIds).get(0), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/companies/{company_id}/projects/{project_id}/comments/{comment_id}")
    public ResponseEntity<PMSComment> updateComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId,  
            @RequestBody @Valid PMSComment comment, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<PMSComment>(entityProvider.updateComment(commentId, comment), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{project_id}/comments/{comment_id}")
    public void deleteComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId) {
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(commentId);
        entityProvider.cleanupComments(commentIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments")
    public ResponseEntity<List<PMSComment>> getCommentsForTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId) {
    	
        return new ResponseEntity<>(entityProvider.getCommentsByTask(taskId), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments")
    public ResponseEntity<PMSComment> createCommentForTask(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId, 
            @RequestBody @Valid PMSComment comment, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<PMSComment>(entityProvider.createCommentForTask(taskId, comment), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments")
    public void deleteComments(@PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId, 
            @RequestBody List<Long> commentIds) {
        entityProvider.cleanupComments(commentIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<PMSComment> getComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId) {
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(commentId);
      
        return new ResponseEntity<PMSComment>(entityProvider.getComments(commentIds).get(0), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<PMSComment> updateComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId,
            @PathVariable("task_id") Long taskId, 
            @RequestBody @Valid PMSComment comment, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<PMSComment>(entityProvider.updateComment(commentId, comment), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/companies/{company_id}/projects/{project_id}/tasks/{task_id}/comments/{comment_id}")
    public void deleteComment(@PathVariable("comment_id") Long commentId, 
            @PathVariable("company_id") Long companyId, 
            @PathVariable("project_id") Long projectId, 
            @PathVariable("task_id") Long taskId) {
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(commentId);
        entityProvider.cleanupComments(commentIds);
    }
    
    // users
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/users")
    public ResponseEntity<List<PMSUser>> getUsers() {
        return new ResponseEntity<>(entityProvider.getUsers(), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/users")
    public ResponseEntity<PMSUser> createUser(@RequestBody @Valid PMSUser user, 
    		@RequestParam(name="company_id", required=false) Long companyId, 
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        
        Long compId = null;
        if (companyId == null) {
        	compId = Long.valueOf(EntityConstants.kDefaultCompanyId);
        }
        return new ResponseEntity<>(entityProvider.createUser(user, compId), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/users")
    public void deleteUsers(@RequestBody List<Long> userIds) {
        entityProvider.deleteUsers(userIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/users/{user_id}")
    public ResponseEntity<List<PMSUser>> getUser(@PathVariable("user_id") Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        
        return new ResponseEntity<>(entityProvider.getUsersByIds(userIds), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping("/users/{user_id}")
    public ResponseEntity<PMSUser> updateUser(@PathVariable("user_id") Long userId, 
            @RequestBody @Valid PMSUser user, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.updateUser(userId, user), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/users/{user_id}")
    public void deleteUser(@PathVariable("user_id") Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        entityProvider.deleteUsers(userIds);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/users/{user_id}/projects")
    public ResponseEntity<List<PMSProject>> getProjectsByUserId(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(entityProvider.getProjectsByUserId(userId), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/users/{user_id}/tasks")
    public ResponseEntity<List<List<PMSTask>>> getTasksByUserId(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(entityProvider.getTasksByUserId(userId), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PostMapping(value="/roles")
    public ResponseEntity<PMSRole> createRole(@RequestBody @Valid PMSRole role, BindingResult result) {
    	if (result.hasErrors()) {
    		return new ResponseEntity<>(role, HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<>(entityProvider.createRole(role), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/roles")
    public ResponseEntity<List<PMSRole>> getRoles() {
    	return new ResponseEntity<>(entityProvider.getRoles(), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @GetMapping(value="/roles/{role_id}")
    public ResponseEntity<PMSRole> getRole(@PathVariable("role_id") Long roleId) {
    	return new ResponseEntity<>(entityProvider.getRole(roleId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @PutMapping(value="/roles/{role_id}")
    public ResponseEntity<PMSRole> updateRole(@RequestBody @Valid PMSRole role, BindingResult result, 
    		@PathVariable("roleId") Long roleId) {
    	if (result.hasErrors()) {
    		return new ResponseEntity<>(role, HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<>(entityProvider.updateRole(role), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('user', 'technician', 'admin')")
    @DeleteMapping(value="/roles/{role_id}")
    public void deleteRole(@PathVariable("role_id") Long roleId) {
    	entityProvider.deleteRole(roleId);
    }
}
