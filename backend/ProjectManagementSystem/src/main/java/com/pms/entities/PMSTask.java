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

/**
 * @author jifang
 *
 */
@Entity
public class PMSTask {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="TASK_ID")
    private long id;
    
    @Column(name="TASK_NAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinTaskNameLen)
    private String name;

    @Lob
    @Column(name = "TASK_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "TASK_PROJECT")
    private long projectId;
    
    @Column(name="TASK_AVATAR")
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
    
    public PMSTask() {
    	dependentTaskIds = new ArrayList<>();
    	userIds = new ArrayList<>();
        commentIds = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
    } 
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public void addDependentTaskId(long taskId) {
        if (!dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.add(taskId);
        }
    }
    
    public void removeDependentTaskId(long taskId) {
        if (dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.remove(taskId);
        }
    }
    
    public List<Long> getDependentTaskIds() {
        return dependentTaskIds;
    }
    
    public void setDependentTaskIds(List<Long> dependentTaskIds) {
        this.dependentTaskIds = dependentTaskIds;
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

    public List<Long> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
    
    public List<Long> getCommentIds() {
        return commentIds;
    }
    
    public void setCommentIds(List<Long> commentIds) {
        this.commentIds = commentIds;
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
}
