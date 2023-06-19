/**
 * 
 */
package com.pms.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pms.constants.EntityConstants;
import com.pms.constants.PMSPriority;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * @author jifang
 *
 */
@Entity
@Data
@AllArgsConstructor
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
    
    @Column(name="AVATAR")
    private String avatar;
    
    @ElementCollection
    @CollectionTable(name="TASK_DEPENDENT_TASKS")
    private List<Long> dependentTaskIds;
    
    @ElementCollection
    @CollectionTable(name="TASK_USERS")
    private List<Long> userIds;
    
    @ElementCollection
    @CollectionTable(name="TASK_COMMENTS")
    private List<Long> commentIds;
    
    private Long start;
    private Long end;
    private Long created;
    
    @ElementCollection
    @CollectionTable(name="TASK_TAGS")
    private List<String> tags;
    
    @Enumerated(EnumType.STRING)
    private PMSPriority priority;
    
    @ElementCollection
    @CollectionTable(name="TASK_ATTACHMENTS")
    private List<String> attachments;
    
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
}
