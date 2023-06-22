/**
 * 
 */
package com.pms.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.EntityConstants;

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
public class PMSComment {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="TITLE")
    @NotNull
    @Size(min=EntityConstants.kMinCommentTitleLen)
    private String title;
    
    @Lob
    @Column(name = "DESCRIPTION", columnDefinition="TEXT")
    private String desc;
    
    @ElementCollection
    @CollectionTable(name = "COMMENT_ATTACHMENTS")
    private List<String> attachments;
    
    @Column(name = "TASK")
    @NotNull
    private Long taskId;
    
    @Column(name= "USER")
    @NotNull
    private Long userId;
    
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
