/**
 * 
 */
package com.pms.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

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
        // TODO
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
        // TODO
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
    	
    	if (!fileRepo.existsByRealFilename(file.getRealFilename())) {
    		ret = fileRepo.save(file);
    		log.debug("file saved. displayname:{}\nrealpath:{}\ntype:{}", 
        			file.getDisplayFilename(), file.getRealFilename(), file.getFileType());
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
    		ret.setParentId(PMSEntityConstants.kDefaultFileParentId);
    		ret = fileRepo.save(ret);
    		log.debug("no default company avatar, create it.");
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
    		ret.setParentId(PMSEntityConstants.kDefaultFileParentId);
    		ret = fileRepo.save(ret);
    		log.debug("no default project avatar, create it.");
    	}
    	
    	return ret;
    }
    
    /**
     * helper function, get the task default avatar.  
     * otherwise create and save the task default avatar. 
     * @return the task default avatar object. 
     */
    private PMSFile getDefaultTaskAvatar() {
    	PMSFile ret = null;
    	
    	if (fileRepo.existsByRealFilename(PMSEntityConstants.kTaskDefaultAvatarPath)) {
    		ret = fileRepo.findByRealFilename(PMSEntityConstants.kTaskDefaultAvatarPath).orElse(ret);
    	} else {
    		ret = new PMSFile(PMSEntityConstants.kTaskDefaultAvatarPath, PMSFileType.Image);
    		ret.setParentId(PMSEntityConstants.kDefaultFileParentId);
    		ret = fileRepo.save(ret);
    		log.debug("no default task avatar, create it.");
    	}
    	
    	return ret;
    }
    
    /**
     * helper function, get the user's default avatar.  
     * otherwise create and save the user's default avatar. 
     * @return the user's default avatar object. 
     */
    private PMSFile getDefaultUserAvatar() {
    	PMSFile ret = null;
    	
    	if (fileRepo.existsByRealFilename(PMSEntityConstants.kUserDefaultAvatarPath)) {
    		ret = fileRepo.findByRealFilename(PMSEntityConstants.kUserDefaultAvatarPath).orElse(ret);
    	} else {
    		ret = new PMSFile(PMSEntityConstants.kUserDefaultAvatarPath, PMSFileType.Image);
    		ret.setParentId(PMSEntityConstants.kDefaultFileParentId);
    		ret = fileRepo.save(ret);
    		log.debug("no default user avatar, create it.");
    	}
    	
    	return ret;
    }
    
    private PMSRole getRoleByName(PMSRoleName name) {
    	return roleRepo.findByName(name).orElseThrow(()-> {
    		log.debug("cannot find role=" + name.getValue());
    		return new ResourceNotFoundException("cannot find role=" + name.getValue());
    	});
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
        	log.debug("name " + comp.getName() + " exists, can't create company with this name.");
        	throw new DuplicateObjectsException("company exists with name=" + comp.getName());
        }
        
        if (comp.getAvatar() != null) {
        	PMSFile avatar = comp.getAvatar();
        	avatar.setParentId(comp.getId());
        	addFile(avatar);
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
                   ()-> {
                	   log.debug("no company with id={}.", companyId);
                	   return new ResourceNotFoundException("No company found with id=" + companyId);
                	   });
            ret.add(company);
        }

        return ret;
    }
 
    public PMSCompany updateCompany(Long id, PMSCompany comp) {
    	// TODO
    }
    
    public void cleanupCompanies(List<Long> companyIds) {
        // TODO
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
        	log.error("No project with id=" + projectId);
            PMSProject project = projRepo.findById(projectId).orElseThrow(()-> {
            	log.debug("no project with id={}.", projectId);
                return new ResourceNotFoundException("No project found with id=" + projectId);
                });
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
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	project.setCompanyId(companyId);
    	
    	// check if the project exists. 
    	if (projRepo.existsByNameAndCompanyId(project.getName(), companyId)) {
    		log.debug("project with name=" + project.getName() + " exists, can't create project with this name.");
           	throw new DuplicateObjectsException("project exists with name=" 
        				+ project.getName() + " and company_id=" + companyId);
        }
        
        if (project.getAvatar() != null) {
        	log.debug("project's avatar != null, will use the customized avatar.");
        	PMSFile avatar = project.getAvatar();
        	avatar.setParentId(project.getId());
        	addFile(avatar);
        } else {
        	log.debug("project's avatar == null, use project default avatar.");
        	project.setAvatar(getDefaultProjectAvatar());
        }
        project = projRepo.save(project);
        
        // update company.
        PMSCompany company = compRepo.findById(companyId)
    			.orElseThrow(()->{
    			log.debug("no company with id={}.", companyId);	
    			return new ResourceNotFoundException("No company found with id=" + companyId);
    			});
        company.addProjectId(project.getId());
        compRepo.save(company);
        
        return project;
    }

    public PMSProject updateProject(Long companyId, Long projectId, PMSProject project) {
    	// TODO
    }

    public void cleanupProjects(Long companyId, List<Long> projectIds) {
    	// TODO
    }
    
    private void cleanupDefaultTask(PMSProject project) {
        // TODO
    }

    public PMSProject addDependentProjectIds(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->{
                log.debug("no project with id={}.", projectId);	
                return new ResourceNotFoundException("No project found with id=" + projectId);
                });

        for (Long dependentProjectId : dependentProjectIds) {
            // a project cannot dependent on itself.
            if (projRepo.existsById(dependentProjectId) &&
            		!ret.getId().equals(dependentProjectId)) {
                ret.addDependentProjectId(dependentProjectId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }

    public PMSProject setDependentProjectIds(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->{
                log.debug("no project with id={}.", projectId);	
                return new ResourceNotFoundException("No project found with id=" + projectId);
                });
        
        ret.getDependentProjectIds().clear();
        ret = addDependentProjectIds(projectId, dependentProjectIds);
        
        return ret;
    }

    public PMSProject removeDependentProjectIds(Long projectId, List<Long> dependentProjectIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->{
                log.debug("no project with id={}.", projectId);
                return new ResourceNotFoundException("No project found with id=" + projectId);
                });
        
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
                ()->{
                	log.debug("no project with id={}.", projectId);
                	return new ResourceNotFoundException("No project found with id=" + projectId);
                	});
        
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
                ()->{
                log.debug("no project with id={}.", projectId);
                return new ResourceNotFoundException("No project found with id=" + projectId);
                });
        
        do {
        	// check if the user is assigned to project
            if (isUserExistsInTask(userId, project.getDefaultTask().getId())) {
                ret = true;
                break;
            }
            // check if the user is assigned to the project's tasks. 
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
    		log.debug("no user with id={}", userId);
    		throw new ResourceNotFoundException("No user found with id=" + userId);
    	}
    	
    	PMSTask task = taskRepo.findById(taskId).orElseThrow(
    			()->{
    				log.debug("no task with id={}.", taskId);
    				return new ResourceNotFoundException("No task found with id=" + taskId);
    			});
    	
    	boolean ret = false;
    	List<Long> userIds = task.getUserIds();
    	if (userIds.contains(userId)) {
    		ret = true;
    	} 
    	
    	return ret;
    }
    
    // task operations
    // default task isn't included in the return list. 
    public List<PMSTask> getTasksByProjectIdAndCompanyId(Long projId, Long companyId) {
        if (!projRepo.existsByIdAndCompanyId(projId, companyId)) {
        	log.debug("no project found with id={}, compan_id={}", projId, companyId);
            throw new ResourceNotFoundException("No project found with id=" + projId + 
            		", and company_id=" + companyId);
        }
        
        return taskRepo.findAllByProjectId(projId);
    }
    
    public List<PMSTask> getTasksByIds(List<Long> taskIds) {
        List<PMSTask> ret = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            PMSTask task = taskRepo.findById(taskId).orElse(null);
            if (task != null) {
                ret.add(task);
            } else {
            	log.debug("task id={} doesn't exist.", taskId);
            }
        }
        
        return ret;
    }

    public PMSTask createTask(Long companyId, Long projectId, PMSTask task) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("no company with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("no project with id=" + projectId + " in company with id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
    	// verify if the task doesn't exist. 
    	if (taskRepo.existsByNameAndProjectId(task.getName(), projectId)) {
    		log.debug("task name={} already exists, cannot create task with this name in project={}", task.getName(), projectId);
    		throw new DuplicateObjectsException("task exists with name=" 
    					+ task.getName() + " project_id=" + projectId + " , and company_id=" + companyId);
    	}
        
        // save avatar
        if (task.getAvatar() != null) {
        	PMSFile avatar = task.getAvatar();
        	avatar.setParentId(task.getId());
        	addFile(avatar);
        } else {
        	task.setAvatar(getDefaultTaskAvatar());
        }
        
        // save task
        task.setProjectId(projectId);
        task = taskRepo.save(task);
        
        // update project
        PMSProject project = projRepo.findById(projectId).orElseThrow(
        		()-> {
        			log.debug("no project with id={}.", projectId);
        			return new ResourceNotFoundException("No project found with id=" + projectId);
        		});
        project.addTaskId(task.getId());
        projRepo.save(project);
        
        return task;
    }
    
    public PMSTask updateTask(Long companyId, Long projectId, Long taskId, PMSTask task) {
    	// TODO
    }
    
    public void cleanupTasks(Long companyId, Long projectId, List<Long> taskIds) {
    	// TODO
    }
    
    public PMSTask addDependentTasks(Long taskId, List<Long> dependentTaskIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->{
                	log.debug("no task found with id={}.", taskId);
                	return new ResourceNotFoundException("No task found with id=" + taskId);
                });

        for (Long dependentTaskId : dependentTaskIds) {
            // a task cannot dependent on itself.
            if (taskRepo.existsById(dependentTaskId) 
                    && !ret.getId().equals(dependentTaskId)) {
                ret.addDependentTaskId(dependentTaskId);
            }
        }
        
        return taskRepo.save(ret);
    }
    
    public PMSTask setDependentTasks(Long taskId, List<Long> dependentTaskIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->{
                	log.debug("no task found with id={}.", taskId);
                	return new ResourceNotFoundException("No task found with id=" + taskId);
                });
        
        ret.getDependentTaskIds().clear();
        for (Long dependentTaskId : dependentTaskIds) {
            // a task cannot dependent on itself.
            if (taskRepo.existsById(dependentTaskId) 
                    && !ret.getId().equals(dependentTaskId)) {
                ret.addDependentTaskId(dependentTaskId);
            }
        }
        taskRepo.save(ret);
        
        return ret;
    }

    public PMSTask removeDependentTasks(Long taskId, List<Long> dependentTaskIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> currentDependentIds = ret.getDependentTaskIds();
        for (Long dependentTaskId : dependentTaskIds) {
            if (currentDependentIds.contains(dependentTaskId)) {
                ret.removeDependentTaskId(dependentTaskId);
            }
        }
        return taskRepo.save(ret);
    }
    
    public List<PMSTask> getDependentTasks(Long taskId) {
        List<PMSTask> tasks = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->{
                	log.debug("no task found with id={}.", taskId);
                	return new ResourceNotFoundException("No task found with id=" + taskId);
                });
        
        List<Long> dependentTaskIds = task.getDependentTaskIds();
        for (Long dependentTaskId : dependentTaskIds) {
            PMSTask dependentTask = taskRepo.findById(dependentTaskId).orElseGet(null);
            if (dependentTask != null) {
                tasks.add(dependentTask);
            }
        }
        
        return tasks;
    }

    // comments operation
    public PMSComment createCommentForProject(Long companyId, Long projectId, PMSComment comment) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
    	PMSProject project = projRepo.findById(projectId)
    			.orElseThrow(()->{
    				log.debug("No project found with id=" + projectId);
    				return new ResourceNotFoundException("No project found with id=" + projectId);
    			});
    	
    	// get default task, will create comment to default task. 
    	PMSTask task = project.getDefaultTask();
    	comment.setTaskId(task.getId());
    	commentRepo.save(comment);
    	
    	// update task.
    	task.addCommentId(comment.getId());
    	taskRepo.save(task);
    	
    	return comment;
    }
    
    public PMSComment createCommentForTask(Long companyId, Long projectId, Long taskId, PMSComment comment) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
    				+ " ,in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " ,in company_id=" + companyId);
    	}
    	
    	// check if the task exists. 
    	if (!taskRepo.existsByIdAndProjectId(taskId, projectId)) {
    		log.debug("No task found with id=" + taskId 
    				+ " with project_id=" + projectId 
    				+ " with company_id=" + companyId);
    		throw new ResourceNotFoundException("No task found with id=" + taskId 
    				+ " with project_id=" + projectId 
    				+ " with company_id=" + companyId);
    	}
    	
    	// create comment to taskId. 
    	PMSTask task = taskRepo.findById(taskId)
    			.orElseThrow(()->{
    				log.debug("No task found with id=" + taskId 
        					+ " with project_id=" + projectId 
        					+ " with company_id=" + companyId);
    				return new ResourceNotFoundException("No task found with id=" + taskId 
    					+ " with project_id=" + projectId 
    					+ " with company_id=" + companyId);
    			});
    	comment.setTaskId(taskId);
    	commentRepo.save(comment);
    	
    	// update task.
    	task.addCommentId(comment.getId());
    	taskRepo.save(task);
    	
    	return comment;
    }

    public void cleanupComments(Long companyId, Long projectId, List<Long> commentIds) {
    	// TODO 
    }
    
    // return: [task0, task1, ...]
    public List<PMSComment> getCommentsByTask(Long companyId, Long projectId, Long taskId) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
    	// check if the task exists. 
    	PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->{
                	log.debug("No task found with id=" + taskId);
                	return new ResourceNotFoundException("No task found with id=" + taskId);
                });
    	
        List<PMSComment> ret = new ArrayList<>();
        List<Long> commentIds = task.getCommentIds();
        for (Long commentId : commentIds) {
            PMSComment comment = commentRepo.findById(commentId).orElseGet(null);
            if (comment != null) {
                ret.add(comment);
            } else {
            	log.debug("comment with id={} is null.", commentId);
            }
        }
        
        return ret;
    }
    
    public List<PMSComment> getComments(Long companyId, Long projectId, List<Long> commentIds) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
        List<PMSComment> ret = null;
        
        if (commentIds == null) {
            ret = commentRepo.findAll();
        } else {
            ret = new ArrayList<>();
            for (Long commentId : commentIds) {
                PMSComment comment = commentRepo.findById(commentId).orElseThrow(
                        ()-> {
                        	log.debug("No comment found with id=" + commentId);
                        	return new ResourceNotFoundException("No comment found with id=" + commentId);
                        });
                ret.add(comment);
            }
        }
        
        return ret;
    } 
    
    // return: [comment0, comment1, ...]
    public List<PMSComment> getCommentsForProjectOnly(Long companyId, Long projectId) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
        PMSProject project = projRepo.findById(projectId).orElseThrow(()-> {
                	log.debug("No project found with id=" + projectId);
                	return new ResourceNotFoundException("No project found with id=" + projectId);
                });
        
        PMSTask task = project.getDefaultTask();
        
        return getCommentsByTask(companyId, projectId, task.getId());
    }

    // return: [project, task0, task1, task2...]
    public List<List<PMSComment>> getCommentsByProject(Long companyId, Long projectId) {
    	// check if the company exists. 
    	if (!compRepo.existsById(companyId)) {
    		log.debug("No company found with id=" + companyId);
    		throw new ResourceNotFoundException("No company found with id=" + companyId);
    	}
    	
    	// check if the project exists. 
    	if (!projRepo.existsByIdAndCompanyId(projectId, companyId)) {
    		log.debug("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    		throw new ResourceNotFoundException("No project found with id=" + projectId 
					+ " in company_id=" + companyId);
    	}
    	
    	PMSProject project = projRepo.findById(projectId)
    			.orElseThrow(()-> {
    				log.debug("No project found with id=" + projectId);
    				return new ResourceNotFoundException("No project found with id=" + projectId);
    			});
    	
    	List<List<PMSComment>> ret = new ArrayList<>();

    	// add comments of tasks.
    	// 1. add project comments
    	List<PMSComment> projectComments = getCommentsForProjectOnly(companyId, projectId);
    	ret.add(projectComments);
    	
    	// 2. add tasks comments
    	List<Long> taskIds = project.getTaskIds();
    	for (Long taskId : taskIds) {
    		List<PMSComment> comments = getCommentsByTask(companyId, projectId, taskId);
    		ret.add(comments);
    	}

    	return ret;
    }

    public PMSComment updateComment(Long companyId, Long projectId, Long commentId, PMSComment comment) {
    	// TODO
    }
    
    // users operation
    private PMSUser createAdminUser(PMSUser user) {
    	// check if the user exists. 
    	if (userRepo.existsByEmail(user.getEmail())) {
    		log.debug("user email=" + user.getEmail() + " already exists.");
    		throw new DuplicateObjectsException("user email=" + user.getEmail() + " already exists.");
    	}
    	
    	if (user.getPassword() != null) {
    		user.setPassword(passwdEncoder.encode(user.getPassword()));
    	}
    	
    	if (user.getAvatar() != null) {
    		user.getAvatar().setParentId(user.getId());
    		fileRepo.save(user.getAvatar());
    	} else {
    		user.setAvatar(getDefaultUserAvatar());
    	}
    	
    	// process roles. we won't create new instance, instead we use existing roles. 
    	List<PMSRole> existingRoles = new ArrayList<>();
    	List<PMSRole> newRoles = user.getRoles();
    	for (PMSRole newRole : newRoles) {
    		PMSRole existingRole = this.getRoleByName(newRole.getName());
    		existingRoles.add(existingRole);
    	}
    	user.setRoles(existingRoles);
    	
    	return userRepo.save(user);
    }
    
    private PMSUser createNormalUser(PMSUser user, Long companyId) {
    	PMSCompany comp = compRepo.findById(companyId).orElseThrow(
        		()-> {
        			log.debug("No company found with id=" + companyId);
        			return new ResourceNotFoundException("No company found with id=" + companyId);
        		});
    	
    	// check if the user exists. 
    	if (userRepo.existsByEmail(user.getEmail())) {
    		log.debug("user email=" + user.getEmail() + " already exists.");
    		throw new DuplicateObjectsException("user email=" + user.getEmail() + " already exists.");
    	}
    	
    	if (user.getPassword() != null) {
    		user.setPassword(passwdEncoder.encode(user.getPassword()));
    	}
    	
    	if (user.getAvatar() != null) {
    		user.getAvatar().setParentId(user.getId());
    		fileRepo.save(user.getAvatar());
    	} else {
    		user.setAvatar(getDefaultUserAvatar());
    	}
    	
    	// process roles. we won't create new instance, instead we use existing roles. 
    	List<PMSRole> existingRoles = new ArrayList<>();
    	List<PMSRole> newRoles = user.getRoles();
    	for (PMSRole newRole : newRoles) {
    		PMSRole existingRole = this.getRoleByName(newRole.getName());
    		existingRoles.add(existingRole);
    	}
    	user.setRoles(existingRoles);
    	
    	PMSUser ret = userRepo.save(user);
    	
    	comp.getUserIds().add(ret.getId());
        compRepo.save(comp);
    	
        return ret;
    }
    
    public PMSUser createUser(PMSUser user, Long companyId) {
    	PMSUser ret = null;
    	
    	// check if the user exists. 
    	if (userRepo.existsByEmail(user.getEmail())) {
    		log.debug("user email=" + user.getEmail() + " already exists.");
    		throw new DuplicateObjectsException("user email=" + user.getEmail() + " already exists.");
    	}
    	
    	// if companyId is -1, means this user is of admin role. 
    	if (companyId == null || companyId.longValue() == -1 ) {
    		// construct admin role. 
        	PMSRole admin = this.getRoleByName(PMSRoleName.admin);
    		if (user.getRoles().contains(admin)) {
    			// create admin user
    			ret = createAdminUser(user);
    		} else {
    			log.debug("companyId is -1 but there is no admin role info.");
    			throw new RequestValueMismatchException("companyId is -1 but there is no admin role info.");
    		}
    	} else {
    		// construct admin role. 
    		PMSRole admin = this.getRoleByName(PMSRoleName.admin);
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
                        () -> {
                        	log.debug("No user found with id="+id);
                        	return new ResourceNotFoundException("No user found with id="+id);
                        });
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
                ()-> {
                	log.debug("No task found with id=" + taskId);
                	return new ResourceNotFoundException("No task found with id=" + taskId);
                });
        
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
    
    public PMSUser updateUser(Long id, PMSUser user, Long companyId) {
    	// TODO 
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
    	
    	// delete user avatars. 
    	List<PMSFile> avatars = new ArrayList<>();
    	for (Long userId : userIds) {
    		// delete user's avatar.
    		avatars.clear();    		
    		PMSUser user = userRepo.findById(userId).orElse(null);
    		if (user != null) {
    			PMSFile avatar = user.getAvatar();
    			// it is a customized avatar. 
    			if (avatar != null &&
    					avatar.getParentId() != PMSEntityConstants.kDefaultFileParentId) {
    				deleteFile(avatar.getId());
    			}
    		}
    	}
    	
    	// remove users from company.
    	List<PMSCompany> companies = compRepo.findAll();
    	for (PMSCompany company : companies) {
    		boolean needSave = false;
    		List<Long> companyUserIds = company.getUserIds();
    		for (Long userId : userIds) {
    			if (companyUserIds.contains(userId)) {
    				companyUserIds.remove(userId);
    				needSave = true;
    			}
    		}
    		if (needSave) {
    			compRepo.save(company);
    		}
    	}
    	
    	// delete users. 
    	for (Long userId : userIds) {
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
        if (!compRepo.existsById(project.getCompanyId())) {
        	throw new ResourceNotFoundException("No company found with id=" + project.getCompanyId());
        }
        if (!userRepo.existsById(userId)) {
        	throw new ResourceNotFoundException("No user found with id=" + userId);
        }
        
        List<PMSTask> ret = new ArrayList<>();
        List<PMSTask> allTasks = new ArrayList<>();
        allTasks.add(project.getDefaultTask());
        List<PMSTask> tasks = getTasksByProjectIdAndCompanyId(project.getId(), project.getCompanyId());
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
		// TODO
	}
	
	public PMSRole updateRole(PMSRoleName roleName, PMSRole role) {
		// TODO
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
		// TODO
	}
	
	public void deleteFile(Long fileId) {
		if (fileRepo.existsById(fileId)) {
			PMSFile file = fileRepo.findById(fileId).orElseGet(null);
			if (file != null) {
				String path = file.getRealFilename();
				fileRepo.deleteById(fileId);
				// remove file from system. 
				File beDeleted = new File(path);
			    beDeleted.delete();
			}
		} else {
			throw new ResourceNotFoundException("File cannot be found with id=" + fileId);
		}
	}
	
}
