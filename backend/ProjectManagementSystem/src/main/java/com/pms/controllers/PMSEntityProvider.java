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

import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSComment;
import com.pms.entities.PMSCompany;
import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
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
    
    private static final String kDefaultTaskName = "__INNER_TASK__";
    
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
    
    // company
    public List<PMSCompany> getCompanies() {
        List<PMSCompany> ret = null;
        
        ret = compRepo.findAll();
        
        return ret; 
    }
    
    // company
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
        PMSCompany ret = null;
        
        ret = compRepo.findById(id).orElseThrow(
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
        compRepo.save(ret);
        
        return ret;
    }
    
    public void deleteCompanies(List<Long> companyIds) {
        for (Long companyId : companyIds) {
            PMSCompany comp = compRepo.findById(companyId).orElse(null);
            if (comp != null) {
                compRepo.deleteById(companyId);
            }
        }
    }
    
    // project
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
        PMSProject ret = projRepo.save(project);
        
        PMSTask innerTask = new PMSTask();
        innerTask.setProjectId(ret.getId());
        innerTask.setName(kDefaultTaskName);
        taskRepo.save(innerTask);
        ret.setDefaultTaskId(innerTask.getId());
        ret = projRepo.save(ret);
        
        return ret;
    }

    public PMSProject updateProject(Long projectId, PMSProject project) {
        
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
        
        if (project.getDependentProjectIds() != null) {
            List<Long> dependentIds = project.getDependentProjectIds();
            for (long dependentId : dependentIds) {
                if (projRepo.existsById(dependentId)) {
                    ret.addDependentProjectId(dependentId);
                }
            }
        }
        
        return projRepo.save(ret);
    }

    public void deleteProjects(List<Long> projectIds) {
        for (Long projectId : projectIds) {
            /*if (!projRepo.existsById(projectId)) {
                throw new ResourceNotFoundException("No project found with id=" + projectId);
            }*/
            projRepo.findById(projectId);
            projRepo.deleteById(projectId);
        }
    }
    
    public PMSProject addDependentProjects(long projectId, List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));

        for (long dependentId : dependentIds) {
            if (projRepo.existsById(dependentId)) {
                    ret.addDependentProjectId(dependentId);
            }
        }
        
        projRepo.save(ret);
        return ret;
    }

    public PMSProject setDependentProjects(long projectId, List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        ret.getDependentProjectIds().clear();
        for (long dependentId : dependentIds) {
            if (projRepo.existsById(dependentId)) {
                ret.addDependentProjectId(dependentId);
            }
        }
        projRepo.save(ret);
        
        return ret;
    }

    public PMSProject removeDependentProjects(long projectId, List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> oldDependentIds = ret.getDependentProjectIds();
        for (Long dependentId : dependentIds) {
            if (oldDependentIds.contains(dependentId)) {
                ret.removeDependentProjectId(dependentId);
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
    
    public PMSProject addUsersToProject(long projectId, List<Long> userIds) {
        PMSProject ret = null;
        
        ret = projRepo.findById(projectId).orElseThrow(
                    ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        List<Long> defaultTaskUserIds = defaultTask.getUserIds();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                if (!defaultTaskUserIds.contains(userId)) {
                    defaultTask.addUserId(userId);
                }
            }
        }
        taskRepo.save(defaultTask);
        
        return ret;
    }

    public PMSProject setUsersToProject(long projectId, List<Long> userIds) {
        PMSProject ret = null;
        
        ret = projRepo.findById(projectId).orElseThrow(
                        ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        
        defaultTask.getUserIds().clear();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                defaultTask.addUserId(userId);
            }
        }
        taskRepo.save(defaultTask);
        
        return ret;
    }

    public PMSProject removeUsersFromProject(long projectId, List<Long> userIds) {
        PMSProject ret = null;
        
        ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        
        List<Long> oldUserIds = defaultTask.getUserIds();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                oldUserIds.remove(userId);
            }
        }
        taskRepo.save(defaultTask);
        
        return ret;
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
    
    public List<PMSComment> getCommentsByProject(long projectId) {
        List<PMSComment> ret = new ArrayList<>();
        
        PMSProject project = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
        if (defaultTask == null) {
            return ret;
        }
        
        List<Long> commentIds = defaultTask.getCommentIds();
        for (Long commentId : commentIds) {
            PMSComment comment = commentRepo.findById(commentId).orElseGet(null);
            if (comment != null) {
                ret.add(comment);
            }
        }
        
        return ret;
    }

    public PMSProject addCommentsToProject(long projectId, List<PMSComment> comments) {
        PMSProject ret = null;
        
        ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findById(ret.getDefaultTaskId()).orElse(null);
        if (defaultTask == null) {
            return ret;
        }
        
        for (PMSComment comment : comments) {
            comment.setTaskId(defaultTask.getId());
        }
        commentRepo.saveAll(comments);
        
        for (PMSComment comment : comments) {
            defaultTask.getCommentIds().add(comment.getId());
        }
        taskRepo.save(defaultTask);
        
        return ret;
    }
    
    public PMSProject deleteCommentsFromProject(long projectId, List<Long> commentIds) {
        PMSProject ret = null;
        
        ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findById(ret.getDefaultTaskId()).orElse(null);
        if (defaultTask == null) {
            return ret;
        }
        List<Long> defaultCommentIds = defaultTask.getCommentIds();
    
        for (long commentId : commentIds) {
            if (defaultCommentIds.contains(commentId)) {
                defaultCommentIds.remove(commentId);
            }
        }
        taskRepo.save(defaultTask);
        commentRepo.deleteAllByIdInBatch(commentIds);
        
        return ret;
    }
    
    public List<PMSComment> getCommentsByTask(long taskId) {
        List<PMSComment> ret = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> commentIds = task.getCommentIds();
        for (Long commentId : commentIds) {
            PMSComment comment = commentRepo.findById(commentId).orElseGet(null);
            if (comment != null) {
                ret.add(comment);
            }
        }
        
        return ret;
    }
    
    public PMSTask addCommentsToTask(long taskId, List<PMSComment> comments) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        for (PMSComment comment : comments) {
            comment.setTaskId(ret.getId());
        }
        commentRepo.saveAll(comments);
        
        for (PMSComment comment : comments) {
            ret.getCommentIds().add(comment.getId());
        }
        taskRepo.save(ret);
        
        return ret;
    }
    
    public PMSTask deleteCommentsFromTask(long taskId, List<Long> commentIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> origCommentIds = ret.getCommentIds();
    
        for (long commentId : commentIds) {
            if (origCommentIds.contains(commentId)) {
                origCommentIds.remove(commentId);
            }
        }
        commentRepo.deleteAllByIdInBatch(commentIds);
        taskRepo.save(ret);
        
        return ret;
    }
    
    // task
    public List<PMSTask> getTasks() {
        List<PMSTask> allTasks = taskRepo.findAll();
        
        List<PMSTask> ret = new ArrayList<>();
        for (PMSTask task : allTasks) {
            if (!task.getName().equals(kDefaultTaskName)) {
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
            if (!task.getName().equals(kDefaultTaskName)) {
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
            if (!task.getName().equals(kDefaultTaskName)) {
                ret.add(task);
            }
        }
        
        return ret;
    }
    
    public PMSTask createTask(PMSTask task) {
        PMSTask ret = null;
        
        // save task
        ret = taskRepo.save(task);
        PMSProject project = projRepo.findById(ret.getProjectId()).orElse(null);
        if (project == null) {
            return ret;
        }
        project.addTaskId(task.getId());
        projRepo.save(project);
        
        return ret;
    }
    
    public PMSTask updateTask(Long taskId, PMSTask task) {
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
        
        return taskRepo.save(ret);
    }
    
    public boolean isUserExistsInProject(long userId, long projectId) {
        boolean ret = false;
        
        PMSProject project = projRepo.findById(projectId).orElse(null);
        if (project == null) {
            return ret;
        }
        
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
    
    public void deleteTasks(List<Long> taskIds) {
        for (Long taskId : taskIds) {
            /*if (!taskRepo(taskId)) {
                throw new ResourceNotFoundException("No task found with id=" + taskId);
            }*/
            
            taskRepo.findById(taskId);
            taskRepo.deleteById(taskId);
            PMSTask task = taskRepo.findById(taskId).orElse(null);
            if (task == null) {
                continue;
            }
            PMSProject project = projRepo.findById(task.getProjectId()).orElse(null);
            project.removeTaskId(taskId);
            projRepo.save(project);
        }
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
    
    public PMSTask addUsersToTask(long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        PMSProject project = projRepo.findById(ret.getProjectId()).orElse(null);
        if (project == null) {
            return ret;
        }
        
        PMSTask defaultTask = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
        if (defaultTask == null) {
            return ret;
        }
        List<Long> defaultUserIds = defaultTask.getUserIds();
        
        boolean needSaveDefaultTask = false;
        for (Long userId : userIds) {
            // move default task users to the task.
            if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
            } 
            if (defaultUserIds.contains(userId)) {
                defaultUserIds.remove(userId);
                needSaveDefaultTask = true;
            }
        }
        taskRepo.save(ret);
        if (needSaveDefaultTask) {
            taskRepo.save(defaultTask);
        }
        
        return ret;
    }
    
    public PMSTask setUsersToTask(long taskId, List<Long> userIds) {
        PMSTask ret = null;
        
        ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        PMSProject project = projRepo.findById(ret.getProjectId()).orElse(null);
        if (project == null) {
            return ret;
        }
        
        PMSTask defaultTask = taskRepo.findById(project.getDefaultTaskId()).orElse(null);
        if (defaultTask == null) {
            return ret;
        }
        List<Long> defaultUserIds = defaultTask.getUserIds();
        boolean needSaveDefaultTask = false;
        
        ret.getUserIds().clear();
        for (Long userId : userIds) {
            // move default task users to the task.
            if (userRepo.existsById(userId)) {
                ret.addUserId(userId);
            } 
            if (defaultUserIds.contains(userId)) {
                defaultUserIds.remove(userId);
                needSaveDefaultTask = true;
            }
        }
        taskRepo.save(ret);
        if (needSaveDefaultTask) {
            taskRepo.save(defaultTask);
        }

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
    
    public List<PMSUser> getUsersForTask(long taskId) {
        List<PMSUser> ret = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> userIds = task.getUserIds();
        for (long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    // users
    public List<PMSUser> getUsers() {
        return userRepo.findAll();
    }
    
    public PMSUser createUser(PMSUser user) {
        return userRepo.save(user);
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
    
    public PMSUser updateUser(Long id, PMSUser user) {
        userRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No user found with id=" + id));
        return userRepo.save(user);
    }
    
    public void deleteUsers(List<Long> ids) {
        for (long id : ids) {
            userRepo.findById(id);
            userRepo.deleteById(id);
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
            ret.add(tasks);
        }

        return ret;
    }
}
