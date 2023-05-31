/**
 * 
 */
package com.pms.entities;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
       
    @Column(name = "COMPANY_ID")
    @NotNull
    private long companyId;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_DEPENDENT_PROJECTS")
    private List<Long> dependentProjectIds;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_TASKS")
    private List<Long> taskIds;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="PROJECT_DEFAULT_TASK", nullable=false)
    private PMSTask defaultTask;
    
    public PMSProject() {
        dependentProjectIds = new ArrayList<>();
        taskIds = new ArrayList<>();
        defaultTask = new PMSTask();
    }
    
    public String getName() {
        return defaultTask.getName();
    }
    
    public void setName(String name) {
        defaultTask.setName(name);
    }
    
    public String getDesc() {
        return defaultTask.getDesc();
    }
    
    public void setDesc(String desc) {
        defaultTask.setDesc(desc);
    }
    
    public String getAvatar() {
        return defaultTask.getAvatar();
    }
    
    public void setAvatar(String filePath) {
        defaultTask.setAvatar(filePath);
    }

    public PMSTask getDefaultTask() {
        return defaultTask;
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
