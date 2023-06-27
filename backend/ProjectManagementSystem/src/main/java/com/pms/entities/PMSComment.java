/**
 * 
 */
package com.pms.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.PMSEntityConstants;

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
    @Size(min=PMSEntityConstants.kMinCommentTitleLen)
    private String title;
    
    @Lob
    @Column(name = "DESCRIPTION", columnDefinition="TEXT")
    private String desc;
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<PMSFile> attachments;
    
    @Column(name = "TASK")
    @NotNull
    private Long taskId;
    
    @CreatedBy
    @Column(updatable=false)
    private String createdUser;
    
    @CreatedDate
    @Column(updatable=false)
    private Long createdTime;
    
    @LastModifiedBy
    private String updatedUser;
    
    @LastModifiedDate
    private Long updatedTime;
}
