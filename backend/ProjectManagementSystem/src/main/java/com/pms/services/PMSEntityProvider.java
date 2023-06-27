/**
 * 
 */
package com.pms.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.constants.PMSEntityConstants;
import com.pms.constants.PMSFileType;
import com.pms.constants.PMSRoleName;
import com.pms.controllers.exceptions.DeletionFailureException;
import com.pms.controllers.exceptions.DuplicateObjectsException;
import com.pms.controllers.exceptions.RequestValueMismatchException;
import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSFile;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSRole;
import com.pms.entities.PMSTag;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSCommentRepo;
import com.pms.repositories.PMSCompanyRepo;
import com.pms.repositories.PMSFileRepo;
import com.pms.repositories.PMSProjectRepo;
import com.pms.repositories.PMSRoleRepo;
import com.pms.repositories.PMSTagRepo;
import com.pms.repositories.PMSTaskRepo;
import com.pms.repositories.PMSUserRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jifang
 *
 */

/**
 * Entity Management class. 
 */
@Service
@Transactional
@Slf4j
public class PMSEntityProvider {
    
    @Autowired
    private PMSCompanyRepo compRepo;
    
    @Autowired
    private PMSProjectRepo projRepo;
    
    @Autowired
    private PMSTaskRepo taskRepo;
    
    @Autowired
    private PMSUserRepo userRepo;
    
    @Autowired
    private PMSRoleRepo roleRepo;
    
    @Autowired
    private PMSCommentRepo commentRepo;
    
    @Autowired
    private PMSFileRepo fileRepo;
        
    @Autowired
    private PasswordEncoder passwdEncoder;
    
    @Autowired
    private PMSTagRepo tagRepo;

    /**
     * helper function to find the removed ID set by comparing the old&new sets. 
     * @param oldIds old ID collection. 
     * @param newIds new ID collection. 
     * @return the collection to be removed (from old collection). 
     */
    private List<Long> updateIdSets(List<Long> oldIds, List<Long> newIds) {
        List<Long> beRemovedIdSet = new ArrayList<>();
        
        for (Long oldId : oldIds) {
            if (!newIds.contains(oldId)) {
                beRemovedIdSet.add(oldId);
            }
        }
        
        return beRemovedIdSet;
    }
    
    /**
     * helper function to find the removed string set by comparing the old&new sets.
     * @param oldStrings old string collection. 
     * @param newStrings new string collection. 
     * @return the collection to be removed (from old collection). 
     * @apiNote case-sensitive. 
     */
    @SuppressWarnings("unused")
	private List<String> updateStringSets(List<String> oldStrings, List<String> newStrings) {
        List<String> beRemovedStringSet = new ArrayList<>();
        
        for (String oldString : oldStrings) {
            if (!newStrings.contains(oldString)) {
                beRemovedStringSet.add(oldString);
            }
        }
        
        return beRemovedStringSet;
    }
    
    /**
     * helper function, add a file to db. 
     * will ignore the duplicated file. 
     * @param file file object to be inserted. 
     * @return the saved file object. 
     */
    private PMSFile addFile(PMSFile file) {
    	PMSFile ret = null;
    	
    	if (file == null) {
    		log.debug("file is null, return.");
    		return ret;
    	}
    	
    	log.debug("displayname:{}\nrealpath:{}\ntype:{}", 
    			file.getDisplayFilename(), file.getRealFilename(), file.getFileType());
    	
    	if (!fileRepo.existsByRealFilename(file.getRealFilename())) {
    		log.debug("file doesn't exist, save it.");
    		ret = fileRepo.save(file);
    	} else {
    		log.debug("file exists, don't save it.");
    	}
    	
    	return ret;
    }
    
    /**
     * helper function, get the company default avatar.  
     * otherwise create and save the company default avatar. 
     * @return the company default avatar object. 
     */
    private PMSFile getDefaultCompanyAvatar() {
    	PMSFile ret = null;
    	
    	if (fileRepo.existsByRealFilename(PMSEntityConstants.kCompanyDefaultAvatarPath)) {
    		ret = fileRepo.findByRealFilename(PMSEntityConstants.kCompanyDefaultAvatarPath).orElse(ret);
    	} else {
    		ret = new PMSFile(PMSEntityConstants.kCompanyDefaultAvatarPath, PMSFileType.Image);
    		ret = fileRepo.save(ret);
    	}
    	
    	return ret;
    }
    
    /**
     * helper function, get the project default avatar.  
     * otherwise create and save the project default avatar. 
     * @return the project default avatar object. 
     */
    private PMSFile getDefaultProjectAvatar() {
    	PMSFile ret = null;
    	
    	if (fileRepo.existsByRealFilename(PMSEntityConstants.kProjectDefaultAvatarPath)) {
    		ret = fileRepo.findByRealFilename(PMSEntityConstants.kProjectDefaultAvatarPath).orElse(ret);
    	} else {
    		ret = new PMSFile(PMSEntityConstants.kProjectDefaultAvatarPath, PMSFileType.Image);
    		ret = fileRepo.save(ret);
    	}
    	
    	return ret;
    }
    
    // company
    /**
     * get all companies as a list. 
     * @return the company list. 
     */
    public List<PMSCompany> getCompanies() {
        List<PMSCompany> ret = null;
        
        ret = compRepo.findAll();
        
        return ret; 
    }
    
    // company operations
    /**
     * create a company
     * @param comp company object
     * @return the newly created company object. 
     * @throws DuplicateObjectsException
     * @apiNote this function doesn't process projectIds and userIds fields. 
     */
    public PMSCompany createCompany(PMSCompany comp) throws DuplicateObjectsException {
        PMSCompany ret = null;

        if (compRepo.existsByName(comp.getName())) {
        	throw new DuplicateObjectsException("company exists with name=" + comp.getName());
        }
        
        if (comp.getAvatar() != null) {
        	addFile(comp.getAvatar());
        } else {
        	comp.setAvatar(getDefaultCompanyAvatar());
        }
        
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
    	PMSCompany ret = compRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No company found with id=" + id));
        
    	// change name
    	if (comp.getName() != null) {
    		if (!ret.getName().equals(comp.getName())) { // need to change name
    			if (compRepo.existsByName(comp.getName())) {
    				throw new DuplicateObjectsException("company name="+ comp.getName() + " exists.");
    			} else {
    				ret.setName(comp.getName());
    			}
    		}
    	}
    	
        if (comp.getAvatar() != null) {
            ret.setAvatar(comp.getAvatar());
        }
        if (comp.getDesc() != null) {
            ret.setDesc(comp.getDesc());
        }
        compRepo.save(ret);
        
        return ret;
    }
    
    public void cleanupCompanies(List<Long> companyIds) {
        for (Long companyId : companyIds) {
            PMSCompany comp = compRepo.findById(companyId).orElse(null);
            if (comp == null) {
            	continue;
            }
            List<Long> projectIds = comp.getProjectIds();
            cleanupProjects(projectIds);
            compRepo.deleteById(companyId);
        }
    }
    
    // project operations
    public List<PMSProject> getProjects()  {
        return projRepo.findAll();
    }
     
    public List<PMSProject> getProjectsByCompanyId(Long companyId) {
        if (!compRepo.existsById(companyId)) {
        	log.error("No company found with id={}", companyId);
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
    
    /**
     * create a project within the company. 
     * @param companyId the project will be created in. 
     * @param project the project object
     * @return the newly create project. 
     */
    public PMSProject createProject(Long companyId, PMSProject project) {
    	// check if the company exists
    	PMSCompany company = compRepo.findById(companyId)
    			.orElseThrow(()->new ResourceNotFoundException("No company found with id=" + companyId));
    	PMSProject ret = project;
        
    	project.setCompanyId(companyId);
    	
    	// verify if project doesn't exist.
        if (projRepo.findByNameAndCompanyId(project.getName(), companyId).orElse(null) != null) {
        	throw new DuplicateObjectsException("company exists with name=" 
        				+ project.getName() + " and company_id=" + companyId);
        }
        
        if (project.getAvatar() != null) {
        	addFile(project.getAvatar());
        } else {
        	project.setAvatar(getDefaultProjectAvatar());
        }
        ret = projRepo.save(ret);
        
        // update company. 
        company.addProjectId(ret.getId());
        compRepo.save(company);
        
        return ret;
    }

    public PMSProject updateProject(Long companyId, Long projectId, PMSProject project) {
    	if (!compRepo.existsById(companyId)) {
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
    	// change project name. 
        if (project.getName() != null) {
        	if (ret.getName().compareTo(project.getName()) != 0) { // need to change project name. 
	        	if (projRepo.existsByNameAndCompanyId(project.getName(), companyId)) {
	        		throw new DuplicateObjectsException("project " + ret.getName() + " exists in the company with id=" + companyId);
	        	} else {
	        		ret.setName(project.getName());
	        	}
        	}
        }
        if (project.getPriority() != null) {
        	ret.setPriority(project.getPriority());
        }
        if (project.getAvatar() != null) {
            ret.setAvatar(project.getAvatar());
        }
        if (project.getDesc() != null) {
            ret.setDesc(project.getDesc());
        }
        return projRepo.save(ret);
    }

    public void cleanupProjects(List<Long> projectIds) {
    	if (projectIds.size() == 0) {
    		return;
    	}

    	List<PMSProject> projects = projRepo.findAllById(projectIds);
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

    		// delete its tasks
        	List<Long> taskIds = project.getTaskIds();
            cleanupTasks(taskIds);
            cleanupDefaultTask(project);
            
            Long projectId = project.getId();
            
            // update company, remove project from the company
            company.removeProjectId(projectId);
            compRepo.save(company);
            
            // delete project
            projRepo.deleteById(projectId);
        }
    }
    
    private void cleanupDefaultTask(PMSProject project) {
        if (project == null) {
            return;
        }
        PMSTask defaultTask = project.getDefaultTask();
        if (defaultTask != null) {
            List<Long> commentIds = defaultTask.getCommentIds();
            cleanupComments(commentIds);
        }
        
        // default task will be deleted when the project is deleted.
        // no need to delete the task manually.
        // taskRepo.deleteById(defaultTask.getId());
    }

    public PMSProject addDependentProjects(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));

        for (Long dependentProjectId : dependentProjectIds) {
            // a project cannot dependent on itself.
            if (!ret.getId().equals(dependentProjectId) 
                    && projRepo.existsById(dependentProjectId)) {
                ret.addDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }

    public PMSProject setDependentProjects(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        ret.getDependentProjectIds().clear();
        for (Long dependentProjectId : dependentProjectIds) {
            // a project cannot dependent on itself.
            if (!ret.getId().equals(dependentProjectId) 
                    && projRepo.existsById(dependentProjectId)) {
                ret.addDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }

    public PMSProject removeDependentProjects(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> currentDependentIds = ret.getDependentProjectIds();
        for (Long dependentProjectId : dependentProjectIds) {
            if (currentDependentIds.contains(dependentProjectId)) {
                ret.removeDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }
    
    public List<PMSProject> getDependentProjectsById(Long projectId) {
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
    
    public boolean isUserExistsInProject(Long userId, Long projectId) {
        boolean ret = false;
        
        PMSProject project = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        do {
            if (isUserExistsInTask(userId, project.getDefaultTask().getId())) {
                ret = true;
                break;
            }
            List<Long> taskIds = project.getTaskIds();
            for (Long taskId : taskIds) {
                if (isUserExistsInTask(userId, taskId)) {
                    ret = true;
                    break;
                }
            }
        } while (false);

        return ret;
    }
    
    public boolean isUserExistsInTask(Long userId, Long taskId) {
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
        return taskRepo.findAllWithoutDefault();
    }
    
    public List<PMSTask> getTasksByProjectId(Long projId) {
        if (!projRepo.existsById(projId)) {
            throw new ResourceNotFoundException("No project found with id=" + projId);
        }
        
        return taskRepo.findAllByProjectId(projId);
    }
    
    public List<PMSTask> getTasksByIds(List<Long> taskIds) {
        List<PMSTask> ret = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            PMSTask task = taskRepo.findById(taskId).orElse(null);
            if (task != null) {
                ret.add(task);
            }
        }
        
        return ret;
    }

    public PMSTask createTask(Long projectId, PMSTask task) {
    	// check if the project exists. 
    	PMSProject project = projRepo.findById(projectId)
    			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + task.getId()));
    	
    	// verify if the task doesn't exist. 
    	if (taskRepo.findByNameAndProject(task.getName(), projectId).orElse(null) != null) {
    		throw new DuplicateObjectsException("task exists with name=" 
    					+ task.getName() + " and project_id=" + projectId);
    	}
    	
        PMSTask ret = null;
        
        // save task
        task.setProjectId(projectId);
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
        if (task.getCommentIds() != null) {
            List<Long> beRemovedCommentIds = updateIdSets(ret.getCommentIds(), task.getCommentIds());
            
            // delete old comments
            cleanupComments(beRemovedCommentIds);
        }

        return taskRepo.save(ret);
    }
    
    public void cleanupTasks(List<Long> taskIds) {
        if (taskIds.size() == 0) {
            return;
        }
        
        List<PMSTask> tasks = this.getTasksByIds(taskIds);
        List<Long> beDeletedTaskIds = new ArrayList<>();
        for (PMSTask task : tasks) {
            // find its project to ensure no dependency issue.
        	PMSProject project = projRepo.findById(task.getProjectId())
        			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + task.getId()));
        	
        	// check if there is dependency issue.
        	List<Long> allTaskIds = project.getTaskIds();
        	List<PMSTask> allTasks = this.getTasksByIds(allTaskIds);
        	for (PMSTask allTask : allTasks) {
        	    // if one of the dependent sets of other tasks contain the task, it can't be removed.
        		if (!allTask.getId().equals(task.getId()) 
        		        && allTask.getDependentTaskIds().contains(task.getId())) {
        			throw new DeletionFailureException("Cannot delete the task (" + task.getId() + ") as other tasks depend on it.");
        		}
        	}
    		
    		// delete its comments
        	cleanupComments(task.getCommentIds());
        	// we won't delete the project default task as it is managed by the project.
        	if (task.getProjectId() != PMSEntityConstants.kDefaultTaskProjectId) {
        	    beDeletedTaskIds.add(task.getId());
        	}
            
            // update project, remove task from the project
            project.removeTaskId(task.getId());
            projRepo.save(project);
        }
        
        // finally delete tasks. 
        for (Long beDeletedTaskId : beDeletedTaskIds) {
            taskRepo.deleteById(beDeletedTaskId);
        }            
    }
    
    public PMSTask addDependentTasks(Long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));

        for (Long dependentId : dependentIds) {
            // a task cannot dependent on itself.
            if (!ret.getId().equals(dependentId) 
                    && taskRepo.existsById(dependentId)) {
                ret.addDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public PMSTask setDependentTasks(Long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        ret.getDependentTaskIds().clear();
        for (Long dependentId : dependentIds) {
            // a task cannot dependent on itself.
            if (!ret.getId().equals(dependentId) 
                    && taskRepo.existsById(dependentId)) {
                ret.addDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }

    public PMSTask removeDependentTasks(Long taskId, List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> currentDependentIds = ret.getDependentTaskIds();
        for (Long dependentId : dependentIds) {
            if (currentDependentIds.contains(dependentId)) {
                ret.removeDependentTaskId(dependentId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public List<PMSTask> getDependentTasks(Long taskId) {
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
    	
    	return createCommentForTask(project.getDefaultTask().getId(), comment); 
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

    public void cleanupComments(List<Long> commentIds) {
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
        for (Long beRemovedCommentId : beRemovedCommentIds) {
            commentRepo.deleteById(beRemovedCommentId);
        }
    }
    
    // return: [task0, task1, ...]
    public List<PMSComment> getCommentsByTask(Long taskId) {
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
    
    public List<PMSComment> getComments(List<Long> commentIds) {
        List<PMSComment> ret = null;
        
        if (commentIds == null) {
            ret = commentRepo.findAll();
        } else {
            ret = new ArrayList<>();
            for (Long commentId : commentIds) {
                PMSComment comment = commentRepo.findById(commentId).orElseThrow(
                        ()->new ResourceNotFoundException("No comment found with id=" + commentId));
                ret.add(comment);
            }
        }
        
        return ret;
    } 
    
    // return: [comment0, comment1, ...]
    public List<PMSComment> getCommentsForProjectOnly(Long projectId) {
        PMSProject project = projRepo.findById(projectId).orElseThrow(()
                ->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask task = project.getDefaultTask();
        
        return getCommentsByTask(task.getId());
    }

    // return: [project, task0, task1, task2...]
    public List<List<PMSComment>> getCommentsByProject(Long projectId) {
    	PMSProject project = projRepo.findById(projectId)
    			.orElseThrow(()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	List<List<PMSComment>> ret = new ArrayList<>();

    	// add comments of tasks.
    	// add project comments
    	List<PMSComment> projectComments = getCommentsForProjectOnly(projectId);
    	ret.add(projectComments);
    	
    	// add tasks comments
    	List<Long> taskIds = project.getTaskIds();
    	for (Long taskId : taskIds) {
    		List<PMSComment> comments = getCommentsByTask(taskId);
    		ret.add(comments);
    	}

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
    	/*
    	if (comment.getAttachments() != null) {
    		// remove old files. 
    		List<PMSFile> oldFiles = ret.getAttachments();
    		List<PMSFile> newFiles = comment.getAttachments();
    		List<String> beRemovedFiles = updateStringSets(oldFiles, newFiles);
    		cleanupFiles(beRemovedFiles);
    		ret.setAttachments(comment.getAttachments());
    	}*/
    	if (comment.getTaskId() != null) {
    		ret.setTaskId(comment.getTaskId());
    	}
    	commentRepo.save(ret);
    	
    	return ret;
    }
    
    private void cleanupFiles(List<String> filePaths) {
        // TODO
    }
    
    // users operation
    private PMSUser createAdminUser(PMSUser user) {
    	user.setPassword(passwdEncoder.encode(user.getPassword()));
        PMSUser ret = userRepo.save(user);
        
        return ret;
    }
    
    private PMSUser createNormalUser(PMSUser user, Long companyId) {
    	PMSCompany comp = compRepo.findById(companyId).orElseThrow(
        		()->new ResourceNotFoundException("No company found with id=" + companyId));
    	
    	user.setPassword(passwdEncoder.encode(user.getPassword()));
        PMSUser ret = userRepo.save(user);
        
        comp.getUserIds().add(ret.getId());
        compRepo.save(comp);
        
        return ret;
    }
    
    public PMSUser createUser(PMSUser user, Long companyId) {
    	PMSUser ret = null;
    	
    	// check if user already exists
    	if (null != userRepo.findByEmail(user.getEmail()).orElse(null)) {
    		throw new DuplicateObjectsException("user already exists with email=" + user.getEmail());
    	}
    	
    	// if companyId is -1 and role is admin, means admin role will be created.
    	// construct admin role. 
    	PMSRole admin = new PMSRole();
    	admin.setName(PMSRoleName.admin);
    	if (companyId == null || companyId.longValue() == -1 ) {
    		if (user.getRoles().contains(admin)) {
    			// create admin user
    			ret = createAdminUser(user);
    		} else {
    			throw new RequestValueMismatchException();
    		}
    	} else {
    		if (user.getRoles().contains(admin)) {
    			// create admin user
    			ret = createAdminUser(user);
    		} else {
    			// create normal user
    			ret = createNormalUser(user, companyId);
    		}
    	}
    	
    	return ret;
    }
    
    public List<PMSUser> getUsersByProject(Long projectId) {
        PMSProject project = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<PMSUser> ret = null;
        
        List<Long> taskIds = project.getTaskIds();
        taskIds.add(project.getDefaultTask().getId());
        Set<PMSUser> usersSet = new HashSet<>();
        for (Long taskId : taskIds) {
            List<PMSUser> users = getUsersByTaskId(taskId);
            usersSet.addAll(users);
        }
        
        ret = new ArrayList<>(usersSet); 
        
        return ret;
    }
    
    public List<PMSUser> getUsersByTaskId(Long taskId) {
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<PMSUser> ret = getUsersByIds(task.getUserIds());
        
        return ret;
    }
    
    public List<PMSUser> getUsers() {
        return userRepo.findAll();
    }
    
    public List<PMSUser> getCompanyUsers() {
    	return userRepo.findCompanyUsers();
    }
    
    public List<PMSUser> getUsersByIds(List<Long> ids) {
        List<PMSUser> ret = new ArrayList<>();
        
        for (Long id : ids) {
            PMSUser user = userRepo.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("No user found with id="+id));
            if (!ret.contains(user)) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    public PMSTask addUsersToTask(Long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> beRemovedUserIds = new ArrayList<>();
        // update task.userIds
        for (Long userId : userIds) {
        	if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
                // as we have assigned the user to a certain task, 
                // we need to remove the user from the project default task. 
                if (ret.getProjectId() != PMSEntityConstants.kDefaultTaskProjectId) {
                    beRemovedUserIds.add(userId);
                }
            }
        }
        taskRepo.save(ret);
        removeUsersFromProject(ret.getProjectId(), beRemovedUserIds);
        
        return ret;
    }
    
    // return the default task
    public PMSTask addUsersToProject(Long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	PMSTask ret = addUsersToTask(project.getDefaultTask().getId(), userIds);
        
        return ret;
    }
    
    public PMSTask setUsersToTask(Long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> beRemovedUserIds = new ArrayList<>();
        
        // clear task.userIds
        ret.getUserIds().clear();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
                // as we have assigned the user to a certain task, 
                // we need to remove the user from the project default task. 
                if (ret.getProjectId() != PMSEntityConstants.kDefaultTaskProjectId) {
                    beRemovedUserIds.add(userId);
                }
            } 
        }
        taskRepo.save(ret);
        removeUsersFromProject(ret.getProjectId(), beRemovedUserIds);
        
        return ret;
    }
    
    // return the default task
    public PMSTask setUsersToProject(Long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	List<Long> beAddedUserIds = new ArrayList<>();
    	for (Long userId : userIds) {
    	    if (!isUserExistsInProject(userId, projectId)) {
    	        beAddedUserIds.add(userId);
    	    }
    	}
    	
    	PMSTask ret = setUsersToTask(project.getDefaultTask().getId(), beAddedUserIds);
    	
    	return ret;
    }
    
    public PMSTask removeUsersFromTask(Long taskId, List<Long> userIds) {
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> currentUserIds = task.getUserIds();
        for (Long userId : userIds) {
            if (currentUserIds.contains(userId)) {
                currentUserIds.remove(userId);
            }
        }
        taskRepo.save(task);
        
        return task;
    }
    
    // remove users from the project's default task. return the default task
    public PMSTask removeUsersFromProject(Long projectId, List<Long> userIds) {
    	PMSProject project = projRepo.findById(projectId).orElseThrow(
    			()->new ResourceNotFoundException("No project found with id=" + projectId));
    	
    	PMSTask ret = removeUsersFromTask(project.getDefaultTask().getId(), userIds);
        
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
        if (user.getLastName() != null) {
        	ret.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
        	ret.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
        	ret.setPassword(passwdEncoder.encode(user.getPassword()));
        }
        
        if (user.getAvatar() != null) {
        	ret.setAvatar(user.getAvatar());
        }
        
        if (user.getRoles() != null) {
        	ret.setRoles(user.getRoles());
        }

        return userRepo.save(user);
    }
    
    // delete user, remove the user from project/task
    public void deleteUsers(List<Long> userIds) {
    	List<PMSProject> allProjects = projRepo.findAll();
    	for (PMSProject project : allProjects) {
    		// remove users from project's default task and other tasks. 
    		List<Long> taskIds = project.getTaskIds();
    		taskIds.add(project.getDefaultTask().getId());
    		for (Long taskId : taskIds) {
    			this.removeUsersFromTask(taskId, userIds);
    		}
    	}
    	
    	// delete users.
    	List<PMSFile> avatars = new ArrayList<>();
    	for (Long userId : userIds) {
    		// delete user's avatar.
    		avatars.clear();    		
    		PMSUser user = userRepo.findById(userId).orElse(null);
    		if (user != null) {
    			avatars.add(user.getAvatar());
    		}
    		// remove avatar. 
    		if (avatars.size() > 0) {
    			fileRepo.deleteAll(avatars);
    		}
            userRepo.deleteById(userId);
    	}
    }
    
    public List<PMSProject> getProjectsByUserId(Long userId) {
        userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("No user found with id=" + userId));
        
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
            List<PMSTask> item = getTasksByUserId(userId, project.getId());
            ret.add(item);
        }
        
        return ret;
    }

    public List<PMSTask> getTasksByUserId(Long userId, Long projectId) {
        PMSProject project = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        userRepo.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("No user found with id=" + userId));
        
        List<PMSTask> ret = new ArrayList<>();
        List<PMSTask> allTasks = new ArrayList<>();
        allTasks.add(project.getDefaultTask());
        List<PMSTask> tasks = getTasksByProjectId(project.getId());
        allTasks.addAll(tasks);
        
        for (PMSTask allTask : allTasks) {
            if (allTask.getUserIds().contains(userId)) {
                ret.add(allTask);
            }
        }
        
        return ret;
    }
    
    public PMSRole createRole(PMSRole role) {
    	PMSRole ret = null;
    	
    	ret = roleRepo.findByName(role.getName()).orElse(ret);
    	if (ret == null) {
    		roleRepo.save(role);
    	}
    	
    	return ret;
    }
    
    public List<PMSRole> getRoles() {
    	List<PMSRole> ret = roleRepo.findAll();
    	
    	return ret;
    }
    
    public PMSRole getRole(Long roleId) {
    	PMSRole ret = null;
    	
    	ret = roleRepo.findById(roleId).orElse(ret);
    	
    	return ret;
    }
    
    public PMSRole getRole(PMSRoleName name) {
		PMSRole ret = null;
		
		ret = roleRepo.findByName(name).orElse(ret);
		
		return ret;
	}

	public PMSRole updateRole(Long roleId, PMSRole role) {
		if (roleId.longValue() != role.getId().longValue()) {
			throw new RequestValueMismatchException();
		}
		
		PMSRole ret = getRole(roleId);
		if (ret == null) {
			throw new ResourceNotFoundException("No role found with id=" + roleId);
		}
		
		/*if (role.getName() != null) {
			ret.setName(role.getName());
		}*/
		
		if (role.getDesc() != null) {
			ret.setDesc(role.getDesc());
		}
		
		roleRepo.save(ret);
		
		return ret;
	}
	
	public PMSRole updateRole(PMSRoleName roleName, PMSRole role) {
		PMSRole ret = null;
		
		ret = getRole(roleName);
		if (ret == null) {
			throw new ResourceNotFoundException("No role found with name=" + roleName);
		}
		
		if (role.getDesc() != null) {
			ret.setDesc(role.getDesc());
		}
		
		roleRepo.save(ret);
		
		return ret;
	}

	public void deleteRole(Long roleId) {
		// TODO
	}
	
	public void deleteRole(PMSRoleName name) {
		// TODO
	}

	public List<PMSUser> getUsersByRoleId(Long roleId) {
		List<PMSUser> ret = new ArrayList<>();
		
		List<PMSUser> users = userRepo.findAll();
		for (PMSUser user : users) {
			boolean found = false;
			List<PMSRole> roles = user.getRoles();
			for (PMSRole role : roles) {
				if (role.getId().longValue() == roleId.longValue()) {
					found = true;
					break;
				}
			}
			if (found) {
				ret.add(user);
			}
		}
		
		return ret;
	}

	public List<PMSUser> getUsersByRoleName(PMSRoleName roleName) {
		List<PMSUser> ret = new ArrayList<>();
		
		List<PMSUser> users = userRepo.findAll();
		for (PMSUser user : users) {
			boolean found = false;
			List<PMSRole> roles = user.getRoles();
			for (PMSRole role : roles) {
				if (role.getName() == roleName) {
					found = true;
					break;
				}
			}
			if (found) {
				ret.add(user);
			}
		}
		
		return ret;
	}
	
	public List<PMSTag> updateTags(List<PMSTag> tags) {
		List<PMSTag> ret = null;
		
		List<PMSTag> allTags = tagRepo.findAll();

		for (PMSTag tag : tags) {
			if (!allTags.contains(tag)) {
				allTags.add(tag);
			}
		}
		
		tagRepo.saveAll(tags);
		ret = tags;
		
		return ret;
	}
	
	public PMSProject startProject(Long projectId) {
		return null;
	}
	
	public PMSProject stopProject(Long projectId) {
		return null;
	}
	
	public PMSTask startTask(Long taskId) {
		return null;
	}
	
	public PMSTask stopTask(Long taskId) {
		return null;
	}
	
}
