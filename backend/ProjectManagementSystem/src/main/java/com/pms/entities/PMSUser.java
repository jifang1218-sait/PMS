/**
 * 
 */
package com.pms.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
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
public class PMSUser {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="FNAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserNameLen)
    private String firstName;
    
    @Column(name="LNAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserNameLen)
    private String lastName;
    
    @Column(name="EMAIL", nullable=false, unique=true)
    @NotNull
    @Email
    private String email;
    
    @Column(name="PASSWORD", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserPasswordLen)
    private String password;
    
    @OneToOne(cascade=CascadeType.ALL)
    private PMSFile avatar;
    
    @ManyToMany
    @JoinTable(
          name="USERS_ROLES",
          joinColumns={@JoinColumn(name="USER_ID",
                        referencedColumnName="ID")},
          inverseJoinColumns={@JoinColumn(name="ROLE_ID", 
        		  		referencedColumnName="ID")})
    private List<PMSRole> roles = new ArrayList<>();
    
    @CreatedDate
    @Column(updatable=false)
    private Long createdTime;
    
    @LastModifiedDate
    private Long updatedTime;
    
    public void addRole(PMSRole role) {
    	if (!roles.contains(role)) {
    		roles.add(role);
    	}
    }
    
    public void removeRole(PMSRole role) {
    	if (roles.contains(role)) {
    		roles.remove(role);
    	}
    }
}
