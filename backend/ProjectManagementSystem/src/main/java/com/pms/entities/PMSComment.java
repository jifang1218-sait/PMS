/**
 * 
 */
package com.pms.entities;

import java.util.List;

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
import lombok.NoArgsConstructor;

/**
 * @author jifang
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PMSComment {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="COMMENT_ID")
    private long id;
    
    @Column(name="COMMENT_TITLE")
    @NotNull
    @Size(min=EntityConstants.kMinCommentTitleLen)
    private String title;
    
    @Lob
    @Column(name = "COMMENT_DESC", columnDefinition="TEXT")
    private String desc;
    
    @ElementCollection
    @CollectionTable(name = "COMMENT_FILEPATH")
    private List<String> filePaths;
    
    @Column(name = "COMMENT_TIMESTAMP")
    private long timestamp;
    
    @Column(name = "COMMENT_TASK")
    @NotNull
    private long taskId;
    
    @Column(name= "COMMENT_USER")
    @NotNull
    private long userId;
    
    public Long getId() {
        return id;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    /*
    public String getDesc() {
        return desc;
    } 
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public List<String> getFilePaths() {
        return filePaths;
    }
    
    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    */
}
