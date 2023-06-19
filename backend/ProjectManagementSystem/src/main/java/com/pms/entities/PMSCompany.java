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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class PMSCompany {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="NAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinCompanyNameLen)
    private String name;

    @Lob 
    @Column(name="DESCRIPTION", columnDefinition="TEXT")
    private String desc;
    
    @Column(name="AVATAR")
    private String avatar;
    
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
}
