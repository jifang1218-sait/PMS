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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * @author jifang
 *
 */
@Entity
@Data
@AllArgsConstructor
public class PMSCompany {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="COMPANY_ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="COMPANY_NAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinCompanyNameLen)
    private String name;

    @Lob
    @Column(name="COMPANY_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name="COMPANY_AVATAR")
    private String avatar;
    
    @ElementCollection
    @CollectionTable(name="COMPANY_PROJECTS")
    private List<Long> projectIds;
    
    public PMSCompany() {
        projectIds = new ArrayList<>();
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
