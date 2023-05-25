/**
 * 
 */
package com.pms.entities;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pms.constants.EntityConstants;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jifang
 *
 */
@Entity
@Data
@AllArgsConstructor
public class PMSProject {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="PROJ_ID")
    private long id;
    
    @Column(name="PROJ_NAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinProjectNameLen)
    private String name;

    @Lob
    @Column(name = "PROJ_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "COMPANY_ID")
    @NotNull
    private long companyId;
    
    @Column(name="PROJ_AVATAR")
    private String avatar;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_DEPENDENT_PROJECTS")
    private List<Long> dependentProjectIds;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_TASKS")
    private List<Long> taskIds;
    
    @Column(name="PROJ_DEFAULT_TASK")
    private long defaultTaskId;
    
    public PMSProject() {
        dependentProjectIds = new ArrayList<>();
        taskIds = new ArrayList<>();
    }
    /*
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public  String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public List<Long> getTaskIds() {
        return taskIds;
    }
    
    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }
    
    public List<Long> getDependentProjectIds() {
        return dependentProjectIds;
    }
    
    public void setDependentProjectIds(List<Long> dependentProjectIds) {
        this.dependentProjectIds = dependentProjectIds;
    }
    
    public void setDefaultTaskId(Long defaultTaskId) {
        this.defaultTaskId = defaultTaskId;
    }*/
    
    public Long getDefaultTaskId() {
        return defaultTaskId;
    }
    
    public Long getCompanyId() {
        return companyId;
    }
    
    public void addDependentProjectId(Long projId) {
        if (!dependentProjectIds.contains(projId)) {
        	dependentProjectIds.add(projId);
        }
    }
    
    public void removeDependentProjectId(Long projId) {
        if (dependentProjectIds.contains(projId)) {
        	dependentProjectIds.remove(projId);
        }
    }
    
    public void addTaskId(Long taskId) {
        if (!taskIds.contains(taskId)) {
            taskIds.add(taskId);
        }
    }
    
    public void removeTaskId(Long taskId) {
        if (taskIds.contains(taskId)) {
            taskIds.remove(taskId);
        }
    }
    
}
