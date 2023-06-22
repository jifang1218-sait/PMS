/**
 * 
 */
package com.pms.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.PMSPriority;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author jifang
 *
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class PMSProject {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
       
    @Column(name = "COMPANY_ID")
    @NotNull
    private Long companyId;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_DEPENDENT_PROJECTS")
    private List<Long> dependentProjectIds;
    
    @ElementCollection
    @CollectionTable(name="PROJECT_TASKS")
    private List<Long> taskIds;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="DEFAULT_TASK", nullable=false)
    private PMSTask defaultTask;
    
    private Long created;
    private Long start;
    private Long end;
    
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
    
    public PMSPriority getPriority() {
    	return defaultTask.getPriority();
    }
    
    public void setPriority(PMSPriority priority) {
    	defaultTask.setPriority(priority);
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
    
    @CreatedBy
    @Column(updatable=false)
    private Long createdUserId;
    
    @CreatedDate
    @Column(updatable=false)
    private Timestamp createdTime;
    
    @LastModifiedBy
    private Long updatedUserId;
    
    @LastModifiedDate
    private Timestamp updatedTime;
    
}
