/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import java.util.HashSet;

//import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.pms.constants.EntityConstants;
import com.pms.controllers.exceptions.DeletionFailureException;
import com.pms.controllers.exceptions.RequestValueMismatchException;
import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.repositories.IPMSFileManager;
import com.pms.repositories.PMSCommentRepo;
import com.pms.repositories.PMSCompanyRepo;
import com.pms.repositories.PMSProjectRepo;
import com.pms.repositories.PMSTaskRepo;
import com.pms.repositories.PMSUserRepo;

/**
 * @author jifang
 *
 */
public class PMSEntityProvider {
    
//    @Autowired
//    private EntityManagerFactory emf;
    
    @Autowired
    private PMSProjectRepo projRepo;
    
    @Autowired
    private PMSCompanyRepo compRepo;
    
    @Autowired
    private PMSTaskRepo taskRepo;
    
    @Autowired
    private PMSUserRepo userRepo;
    
    @Autowired
    private PMSCommentRepo commentRepo;
    
    @Autowired
    private IPMSFileManager fileMgr;
    
    // company
    public List<PMSCompany> getCompanies() {
        List<PMSCompany> ret = null;
        
        ret = compRepo.findAll();
        
        return ret; 
    }
    
    // company operations
    public PMSCompany createCompany(PMSCompany comp) {
        PMSCompany ret = null;
        
        ret = compRepo.save(comp);
        
        return ret;
    }
    
    public List<PMSCompany> getCompaniesByIds(List<Long> companyIds) {
        List<PMSCompany> ret = new ArrayList<PMSCompany>();

        for (Long companyId : companyIds) {
            PMSCompany company = compRepo.findById(companyId).orElseThrow(
                   ()-> new ResourceNotFoundException("No company found with id=" + companyId));
            ret.add(company);
        }

        return ret;
    }
 
    public PMSCompany updateCompany(Long id, PMSCompany comp) {
    	if (id != comp.getId()) {
    		throw new RequestValueMismatchException();
    	}
        
        PMSCompany ret = compRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No company found with id=" + id));
        
        if (comp.getAvatar() != null) {
            ret.setAvatar(comp.getAvatar());
        }
        if (comp.getDesc() != null) {
            ret.setDesc(comp.getDesc());
        }
        if (comp.getName() != null) {
            ret.setName(comp.getName());
        }
        if (comp.getProjectIds() != null) {
            ret.setProjectIds(comp.getProjectIds());
        }
        compRepo.save(ret);
        
        return ret;
    }
    
    public void deleteCompanies(List<Long> companyIds) {
        for (Long companyId : companyIds) {
            PMSCompany comp = compRepo.findById(companyId).orElse(null);
            if (comp == null) {
            	continue;
            }
            List<Long> projectIds = comp.getProjectIds();
            deleteProjects(projectIds);
            compRepo.deleteById(companyId);
        }
    }
    
    // project operations
    public List<PMSProject> getProjects()  {
        return projRepo.findAll();
    }
     
    public List<PMSProject> getProjectsByCompanyId(Long companyId) {
        if (!compRepo.existsById(companyId)) {
            throw new ResourceNotFoundException("No company found with id=" + companyId);
        }
        
        List<PMSProject> projects = projRepo.findAllByCompanyId(companyId);
        
        return projects;
    }
    
    public List<PMSProject> getProjectsByIds(List<Long> projectIds) {
        List<PMSProject> ret = new ArrayList<>();
        
        for (Long projectId : projectIds) {
            PMSProject project = projRepo.findById(projectId).orElseThrow(()->
                        new ResourceNotFoundException("No project found with id=" + projectId));
            ret.add(project);
        }
            
        return ret;
    }
    
    public PMSProject createProject(PMSProject project) {
    	PMSCompany company = compRepo.findById(project.getCompanyId())
    			.orElseThrow(()->new ResourceNotFoundException("No company found with id=" + project.getCompanyId()));
    	PMSProject ret = projRepo.save(project);
        
        // create default task for the project then update the project.
        PMSTask innerTask = new PMSTask();
        innerTask.setProjectId(ret.getId());
        innerTask.setName(EntityConstants.kDefaultTaskName);
        taskRepo.save(innerTask);
        ret.setDefaultTaskId(innerTask.getId());
        ret = projRepo.save(ret);
        
        // update company. 
        company.addProjectId(ret.getId());
        compRepo.save(company);
        
        return ret;
    }

    public PMSProject updateProject(Long projectId, PMSProject project) {
    	if (projectId != project.getId()) {
    		throw new RequestValueMismatchException();
    	}
    	
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        if (project.getAvatar() != null) {
            ret.setAvatar(project.getAvatar());
        }
        if (project.getCompanyId() != null) {
            ret.setCompanyId(project.getCompanyId());
        }
        if (project.getDesc() != null) {
            ret.setDesc(project.getDesc());
        }
        if (project.getName() != null) {
            ret.setName(project.getName());
        }
        if (project.getDefaultTaskId() != null) {
        	ret.setDefaultTaskId(project.getDefaultTaskId());
        }
        
        if (project.getDependentProjectIds() != null) {
            List<Long> dependentIds = project.getDependentProjectIds();
            for (long dependentId : dependentIds) {
                if (projRepo.existsById(dependentId)) {
                    ret.addDependentProjectId(dependentId);
                }
            }
        }
        
        if (project.getTaskIds() != null) {
	        List<Long> oldTaskIds = ret.getTaskIds();
	        List<Long> newTaskIds = project.getTaskIds();
	        List<Long> beRemovedTasks = new ArrayList<>();
	        for (Long oldTaskId : oldTaskIds) {
	        	if (!newTaskIds.contains(oldTaskId)) {
	        		beRemovedTasks.add(oldTaskId);
	        	}
	        }
	        this.deleteTasks(beRemovedTasks);
	        ret.setTaskIds(newTaskIds);
        }
        
        
        return projRepo.save(ret);
    }

    public void deleteProjects(List<Long> projectIds) {
    	if (projectIds.size() == 0) {
    		return;
    	}

    	List<PMSProject> projects = projRepo.findAllById(projectIds);
    	List<Long> beDeletedProjectIds = new ArrayList<>();
    	for (PMSProject project : projects) {
    		PMSCompany company = compRepo.findById(project.getCompanyId())
    				.orElseThrow(()->new ResourceNotFoundException("No company found with id=" + project.getCompanyId()));
    		// check if other projects depend on it.
    		List<PMSProject> allCompanyProjects = this.getProjectsByCompanyId(company.getId());
    		for (PMSProject allCompanyProject : allCompanyProjects) {
        		if (allCompanyProject.getDependentProjectIds().contains(project.getId())) {
        			throw new DeletionFailureException("Cannot delete the project (" + project.getId() + ") as other projects depend on it.");
        		}
        	}
    		
    		// update company, remove project from the company
    		company.removeProjectId(project.getId());;
    		compRepo.save(company);
    		
    		// delete its tasks
        	List<Long> taskIds = project.getTaskIds();
            taskIds.add(project.getDefaultTaskId());
            deleteTasks(taskIds);
            
            beDeletedProjectIds.add(project.getId());
        }
    	
    	// delete projects
    	projRepo.deleteAllByIdInBatch(beDeletedProjectIds);
    }
    
    public PMSProject addDependentProjects(long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));

        for (long dependentProjectId : dependentProjectIds) {
            if (projRepo.existsById(dependentProjectId)) {
                    ret.addDependentProjectId(dependentProjectId);
            }
        }
        
        projRepo.save(ret);
        return ret;
    }

    public PMSProject setDependentProjects(long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        ret.getDependentProjectIds().clear();
        for (long dependentProjectId : dependentProjectIds) {
            if (projRepo.existsById(dependentProjectId)) {
                ret.addDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }

    public PMSProject removeDependentProjects(long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> oldDependentIds = ret.getDependentProjectIds();
        for (Long dependentProjectId : dependentProjectIds) {
            if (oldDependentIds.contains(dependentProjectId)) {
                ret.removeDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }
    
    public List<PMSProject> getDependentProjectsById(long projectId) {
        List<PMSProject> projects = new ArrayList<PMSProject>();
        PMSProject proj = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> projIds = proj.getDependentProjectIds();
        for (Long projId : projIds) {
            PMSProject dep = projRepo.findById(projId).orElseGet(null);
            if (dep != null) {
                projects.add(dep);
            }
        }
        
        return projects;
    }
    
    public boolean isUserExistsInProject(long userId, long projectId) {
    	if (!projRepo.existsById(projectId)) {
    		throw new ResourceNotFoundException("No project found with id=" + projectId);
    	}
    	
        boolean ret = false;
        
        List<PMSTask> tasks = getTasksByProjectId(projectId);
        for (PMSTask task : tasks) {
            List<Long> userIds = task.getUserIds();
            if (userIds.contains(userId)) {
                ret = true;
                break;
            }
        }

        return ret;
    }
    
    public boolean isUserExistsInTask(long userId, long taskId) {
    	if (!userRepo.existsById(userId)) {
    		throw new ResourceNotFoundException("No user found with id=" + userId);
    	}
    	
    	PMSTask task = taskRepo.findById(taskId).orElseThrow(
    			()->new ResourceNotFoundException("No task found with id=" + taskId));
    	
    	boolean ret = false;
    	List<Long> userIds = task.getUserIds();
    	if (userIds.contains(userId)) {
    		ret = true;
    	} 
    	
    	return ret;
    }
    
    // task operations
    public List<PMSTask> getTasks() {
        List<PMSTask> allTasks = taskRepo.findAll();
        
        List<PMSTask> ret = new ArrayList<>();
        for (PMSTask task : allTasks) {
            if (!task.getName().equals(EntityConstants.kDefaultTaskName)) {
                ret.add(task);
            }
        }
        
        return ret;
    }
    
    public List<PMSTask> getTasksByProjectId(Long projId) {
        if (!projRepo.existsById(projId)) {
            throw new ResourceNotFoundException("No project found with id=" + projId);
        }
        
        List<PMSTask> allTasks = taskRepo.findAllByProjectId(projId);
        List<PMSTask> ret = new ArrayList<>();
        for (PMSTask task : allTasks) {
            if (!task.getName().equals(EntityConstants.kDefaultTaskName)) {
                ret.add(task);
            }
        }
        
        return ret;
    }
    
    public List<PMSTask> getTasksByIds(List<Long> taskIds) {
        List<PMSTask> ret = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            PMSTask task = taskRepo.findById(taskId).orElseThrow(
                        ()->new ResourceNotFoundException("No task found with id=" + taskId));
            if (!task.getName().equals(EntityConstants.kDefaultTaskName)) {
                ret.add(task);
            }
        }
        
        return ret;
    }
    
    public PMSTask createTask(PMSTask task) {
    	PMSProject project = projRepo.findById(task.getProjectId())
    			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + task.getId()));
    	
        PMSTask ret = null;
        
        // save task
        ret = taskRepo.save(task);
        
        // update project
        project.addTaskId(task.getId());
        projRepo.save(project);
        
        return ret;
    }
    
    public PMSTask updateTask(Long taskId, PMSTask task) {
    	if (taskId != task.getId()) {
    		throw new RequestValueMismatchException();
    	}
    	
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        if (task.getAvatar() != null) {
            ret.setAvatar(task.getAvatar());
        }
        if (task.getName() != null) {
            ret.setName(task.getName());
        }
        if (task.getDesc() != null) {
            ret.setDesc(task.getDesc());
        }
        if (task.getProjectId() != null) {
            ret.setProjectId(task.getProjectId());
        }
        if (task.getDependentTaskIds() != null) {
            ret.setDependentTaskIds(task.getDependentTaskIds());
        }
        if (task.getUserIds() != null) {
        	ret.setUserIds(task.getUserIds());
        }
        
        // update commentIds
        List<Long> oldCommentIds = ret.getCommentIds();
        List<Long> newCommentIds = task.getCommentIds();
        List<Long> beRemovedCommentIds = new ArrayList<>();
        for (Long oldCommentId : oldCommentIds) {
        	if (!newCommentIds.contains(oldCommentId)) {
        		beRemovedCommentIds.add(oldCommentId);
        	}
        }
        // delete old comments
        commentRepo.deleteAllByIdInBatch(beRemovedCommentIds);

        return taskRepo.save(ret);
    }
    
    public void deleteTasks(List<Long> taskIds) {
        if (taskIds.size() == 0) {
            return;
        }
        
        List<PMSTask> tasks = this.getTasksByIds(taskIds);
        List<Long> beDeletedTaskIds = new ArrayList<>();
        for (PMSTask task : tasks) {
        	PMSProject project = projRepo.findById(task.getProjectId())
        			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + task.getId()));
        	
        	// check if there is dependency issue.
        	List<Long> allTaskIds = project.getTaskIds();
        	List<PMSTask> allTasks = this.getTasksByIds(allTaskIds);
        	for (PMSTask allTask : allTasks) {
        		if (allTask.getDependentTaskIds().contains(task.getId())) {
        			throw new DeletionFailureException("Cannot delete the task (" + task.getId() + ") as other tasks depend on it.");
        		}
        	}
        	
        	// update project, remove task from the project
        	project.removeTaskId(task.getId());
    		projRepo.save(project);
    		
    		// delete its comments
        	List<Long> commentIds = task.getCommentIds();
        	commentRepo.deleteAllByIdInBatch(commentIds);
            
            beDeletedTaskIds.add(task.getId());
        }
        taskRepo.deleteAllByIdInBatch(beDeletedTaskIds);            
    }
    
    public PMSTask addDependentTasks(long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));

        for (long dependentId : dependentIds) {
            if (taskRepo.existsById(dependentId)) {
                ret.addDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public PMSTask setDependentTasks(long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        ret.getDependentTaskIds().clear();
        for (long dependentId : dependentIds) {
            if (taskRepo.existsById(dependentId)) {
                ret.addDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }

    public PMSTask removeDependentTasks(long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> oldDependentIds = ret.getDependentTaskIds();
        for (Long dependentId : dependentIds) {
            if (oldDependentIds.contains(dependentId)) {
                ret.removeDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public List<PMSTask> getDependentTasks(long taskId) {
        List<PMSTask> tasks = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> taskIds = task.getDependentTaskIds();
        for (Long depId : taskIds) {
            PMSTask dep = taskRepo.findById(depId).orElseGet(null);
            if (dep != null) {
                tasks.add(dep);
            }
        }
        
        return tasks;
    }
    
    // comments operation
    public PMSComment createCommentForProject(Long projectId, PMSComment comment) {
    	PMSProject project = projRepo.findById(projectId)
    			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + projectId));
    	PMSTask defaultTask = taskRepo.findById(project.getDefaultTaskId())
    			.orElse(null);
    	if (defaultTask == null) {
    		return null;
    	}
    	// change project id to default task id.
    	comment.setTaskId(defaultTask.getId());
    	commentRepo.save(comment);
    	
    	// update default task.
    	defaultTask.addCommentId(comment.getId());
    	taskRepo.save(defaultTask);
    	
    	return comment;
    }
    
    public PMSComment createCommentForTask(Long taskId, PMSComment comment) {
    	PMSTask task = taskRepo.findById(taskId)
    			.orElseThrow(()->new ResourceNotFoundException("No task found with id=" + taskId));
    	
    	comment.setTaskId(taskId);
    	commentRepo.save(comment);
    	
    	// update task.
    	task.addCommentId(comment.getId());
    	taskRepo.save(task);
    	
    	return comment;
    	
    }

    public void deleteComments(List<Long> commentIds) {
        List<PMSComment> comments = commentRepo.findAllById(commentIds);
        List<Long> beRemovedCommentIds = new ArrayList<>();
        for (PMSComment comment : comments) {
        	// update task
        	PMSTask task = taskRepo.findById(comment.getTaskId()).orElse(null);
        	if (task == null) {
        		continue;
        	}
        	task.removeCommentId(comment.getId());
        	taskRepo.save(task);
        	
        	beRemovedCommentIds.add(comment.getId());
        }
        
        // delete comments
        commentRepo.deleteAllByIdInBatch(beRemovedCommentIds);
    }
    
    public List<PMSComment> getCommentsByTask(long taskId) {
    	PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
    	
        List<PMSComment> ret = new ArrayList<>();
        List<Long> commentIds = task.getCommentIds();
        for (Long commentId : commentIds) {
            PMSComment comment = commentRepo.findById(commentId).orElseGet(null);
            if (comment != null) {
                ret.add(comment);
            }
        }
        
        return ret;
    }
    
    // return: [project, task0, task1, task2...]
    public List<List<PMSComment>> getCommentsByProject(long projectId) {
    	projRepo.findById(projectId)
    			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	List<List<PMSComment>> ret = new ArrayList<>();
    	
    	// add comments of tasks. 
    	List<PMSTask> allTasks = taskRepo.findAllByProjectId(projectId);
    	PMSTask defaultTask = null; 
    	for (PMSTask allTask : allTasks) {
    		if (allTask.getName().equals(EntityConstants.kDefaultTaskName)) {
    			defaultTask = allTask;
    			continue;
    		}
    		List<Long> commentIds = allTask.getCommentIds();
    		List<PMSComment> comments = commentRepo.findAllById(commentIds);
    		ret.add(comments);
    	}
    	
    	// add comments of the project, at the beginning of the list. 
    	List<PMSComment> comments = commentRepo.findAllById(defaultTask.getCommentIds());
    	ret.add(0, comments);
    	
    	return ret;
    }
    	
    
    public PMSComment updateComment(Long commentId, PMSComment comment) {
    	if (commentId != comment.getId()) {
    		throw new RequestValueMismatchException();
    	}
    	
    	PMSComment ret = commentRepo.findById(comment.getId())
    			.orElseThrow(()->new ResourceNotFoundException("No comment found with id=" + comment.getId()));
    	if (comment.getTitle() != null) {
    		ret.setTitle(comment.getTitle());
    	}
    	if (comment.getDesc() != null) {
    		ret.setDesc(comment.getDesc());
    	}
    	if (comment.getFilePaths() != null) {
    		// remove old files. 
    		List<String> oldFiles = ret.getFilePaths();
    		List<String> newFiles = comment.getFilePaths();
    		List<String> beRemovedFiles = new ArrayList<>();
    		for (String oldFile : oldFiles) {
    			if (!newFiles.contains(oldFile)) {
    				beRemovedFiles.add(oldFile);
    			}
    		}
    		ret.setFilePaths(comment.getFilePaths());
    	}
    	if (comment.getTimestamp() != null) {
    		ret.setTimestamp(comment.getTimestamp());
    	}
    	if (comment.getUserId() != null) {
    		ret.setUserId(comment.getUserId());
    	}
    	if (comment.getTaskId() != null) {
    		ret.setTaskId(comment.getTaskId());
    	}
    	commentRepo.save(ret);
    	
    	return ret;
    }
    
    // users operation
    public PMSUser createUser(PMSUser user) {
        return userRepo.save(user);
    }
    
    public List<PMSUser> getUsersByProject(long projectId) {
        List<PMSUser> ret = new ArrayList<>();
        
        List<PMSTask> tasks = taskRepo.findAllByProjectId(projectId);
        Set<Long> userIds = new HashSet<>();
        
        for (PMSTask task : tasks) {
            userIds.addAll(task.getUserIds());
        }
        
        for (long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        } 
        
        return ret;
    }
    
    public List<PMSUser> getUsersByTask(long taskId) {
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<PMSUser> ret = new ArrayList<>();
        List<Long> userIds = task.getUserIds();
        for (long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    public List<PMSUser> getUsers() {
        return userRepo.findAll();
    }
    
    public List<PMSUser> getUsersByIds(List<Long> ids) {
        List<PMSUser> ret = new ArrayList<>();
        
        for (long id : ids) {
            PMSUser user = userRepo.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("No user found with id="+id));
            ret.add(user);
        }
        
        return ret;
    }
    
    public PMSTask addUsersToTask(long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        // update task.userIds
        for (Long userId : userIds) {
        	if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
            }
        }
        taskRepo.save(ret);        
        
        return ret;
    }
    
    // return the default task
    public PMSTask addUsersToProject(long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	PMSTask ret = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
    	if (ret == null) {
    		return ret;
    	}
    	
    	// update task.userIds
        for (Long userId : userIds) {
        	if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public PMSTask setUsersToTask(long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        ret.getUserIds().clear();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
            } 
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    // return the default task
    public PMSTask setUsersToProject(long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	PMSTask ret = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
    	if (ret == null) {
    		return ret;
    	}
    	
    	ret.getUserIds().clear();
    	for (Long userId : userIds) {
    		if (userRepo.existsById(userId)) {
    			ret.addUserId(userId);
    		}
    	}
    	taskRepo.save(ret);
    	
    	return ret;
    }
    
    public PMSTask removeUsersFromTask(long taskId, List<Long> userIds) {
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> oldUserIds = task.getUserIds();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                oldUserIds.remove(userId);
            }
        }
        taskRepo.save(task);
        
        return task;
    }
    
    // remove users from the project's default task. return the default task
    public PMSTask removeUsersFromProject(long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	PMSTask ret = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
    	if (ret == null) {
    		return ret;
    	}
    	List<Long> oldUserIds = ret.getUserIds();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                oldUserIds.remove(userId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public PMSUser updateUser(Long id, PMSUser user) {
    	if (id != user.getId()) {
    		throw new RequestValueMismatchException();
    	}
    	
        PMSUser ret = userRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No user found with id=" + id));
        
        if (user.getFirstName() != null) {
        	ret.setFirstName(user.getFirstName());
        }
        if (user.getMidName() != null) {
        	ret.setMidName(user.getMidName());
        }
        if (user.getLastName() != null) {
        	ret.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
        	ret.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
        	ret.setPassword(user.getPassword());
        }
        if (user.getRole() != null) {
        	ret.setRole(user.getRole());
        }
        if (user.getAvatar() != null) {
        	ret.setAvatar(user.getAvatar());
        }

        return userRepo.save(user);
    }
    
    // delete user, remove the user from project/task
    public void deleteUsers(List<Long> userIds) {
    	List<PMSProject> allProjects = projRepo.findAll();
    	for (PMSProject project : allProjects) {
    		// remove users from project's default tasks and other tasks. 
    		List<Long> taskIds = project.getTaskIds();
    		taskIds.add(project.getDefaultTaskId());
    		for (Long taskId : taskIds) {
    			this.removeUsersFromTask(taskId, userIds);
    		}
    	}
    	
    	// delete users.
    	List<String> avatars = new ArrayList<>();
    	for (Long userId : userIds) {
    		// delete user's avatar.
    		avatars.clear();    		
    		PMSUser user = userRepo.findById(userId).orElse(null);
    		if (user != null) {
    			avatars.add(user.getAvatar());
    		}
    		// remove avatar. 
    		if (avatars.size() > 0) {
    			fileMgr.removeFiles(avatars);
    		}
            userRepo.deleteById(userId);
    	}
    }
    
    public List<PMSProject> getProjectsByUserId(Long userId) {
        List<PMSProject> ret = new ArrayList<>();
        
        List<PMSProject> allProjects = getProjects();
        for (PMSProject project : allProjects) {
            if (this.isUserExistsInProject(userId, project.getId())) {
                ret.add(project);
            }
        }
        
        return ret;
    }
    
    public List<List<PMSTask>> getTasksByUserId(Long userId) {
        List<List<PMSTask>> ret = new ArrayList<>();
        
        List<PMSProject> projects = getProjectsByUserId(userId);
        for (PMSProject project : projects) {
            List<PMSTask> tasks = this.getTasksByProjectId(project.getId());
            for (PMSTask task : tasks) {
            	List<Long> userIds = task.getUserIds();
            	if (userIds.contains(userId)) {
            		ret.add(tasks);
            	}
            }
        }

        return ret;
    }
}
