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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.EntityConstants;
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
public class PMSTask {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="NAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinTaskNameLen)
    private String name;

    @Lob
    @Column(name = "DESCRIPTION", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "PROJECT")
    private Long projectId;
    
    @OneToOne(cascade=CascadeType.ALL)
    private PMSFile avatar;
    
    @ElementCollection
    @CollectionTable(name="TASK_DEPENDENT_TASKS")
    private List<Long> dependentTaskIds;
    
    @ElementCollection
    @CollectionTable(name="TASK_USERS")
    private List<Long> userIds;
    
    @ElementCollection
    @CollectionTable(name="TASK_COMMENTS")
    private List<Long> commentIds;
    
    @Column(name="START_DATE", nullable=false)
    private Long startDate = 0L;
    
    @Column(name="END_DATE", nullable=false)
    private Long endDate = 0L;
    
    @ElementCollection
    @CollectionTable(name="TASK_TAGS")
    private List<String> tags;
    
    @Enumerated(EnumType.STRING)
    private PMSPriority priority;
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<PMSFile> attachments;
    
    public PMSTask() {
    	dependentTaskIds = new ArrayList<>();
    	userIds = new ArrayList<>();
        commentIds = new ArrayList<>();
        // set project id of the default task to -1, 
        // as the relationship is managed in column pmsproject_default_task in table pmsproject
        projectId = -1L; 
    }
    
    public void addCommentId(Long commentId) {
        if (!commentIds.contains(commentId)) {
            commentIds.add(commentId);
        }
    }
    
    public void removeCommentId(Long commentId) {
        if (commentIds.contains(commentId)) {
            commentIds.remove(commentId);
        }
    }
    
    public void addUserId(Long userId) {
        if (!userIds.contains(userId)) {
            userIds.add(userId);
        }
    }
    
    public void removeUserId(Long userId) {
        if (userIds.contains(userId)) {
            userIds.remove(userId);
        }
    }
    
    public void addDependentTaskId(Long taskId) {
        if (!dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.add(taskId);
        }
    }
    
    public void removeDependentTaskId(Long taskId) {
        if (dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.remove(taskId);
        }
    }
    
    @CreatedBy
    @Column(updatable=false)
    private Long createdUserId;
    
    @CreatedDate
    @Column(updatable=false)
    private Long createdTime;
    
    @LastModifiedBy
    private Long updatedUserId;
    
    @LastModifiedDate
    private Long updatedTime;
}
