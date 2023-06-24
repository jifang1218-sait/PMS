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
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
public class PMSCompany {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="NAME", nullable=false, unique=true)
    @NotNull
    @Size(min=EntityConstants.kMinCompanyNameLen)
    private String name;

    @Lob 
    @Column(name="DESCRIPTION", columnDefinition="TEXT")
    private String desc;
    
    @OneToOne(cascade=CascadeType.ALL)
    private PMSFile avatar;
    
    @ElementCollection
    @CollectionTable(name="COMPANY_PROJECTS")
    private List<Long> projectIds;
    
    @ElementCollection
    @CollectionTable(name="COMPANY_USERS")
    private List<Long> userIds;
    
    public PMSCompany() {
        projectIds = new ArrayList<>();
        userIds = new ArrayList<>();
    }
    
    public void addProjectId(Long projectId) {
        if (!projectIds.contains(projectId)) {
            projectIds.add(projectId);
        }
    }
    
    public void removeProjectId(Long projectId) {
        if (projectIds.contains(projectId)) {
            projectIds.remove(projectId);
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
