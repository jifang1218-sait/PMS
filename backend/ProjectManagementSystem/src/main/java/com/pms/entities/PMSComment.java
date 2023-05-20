/**
 * 
 */
package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author jifang
 *
 */
@Entity
public class PMSComment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Column(name="COMMENT_TITLE")
    private String title;
    
    @Lob
    @Column(name = "COMMENT_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "COMMENT_FILEPATH")
    private String filePath;
    
    @Column(name = "COMMENT_TIMESTAMP")
    private long timestamp;
    
    public Long getId() {
        return id;
    }
    
    public String getDesc() {
        return desc;
    } 
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
