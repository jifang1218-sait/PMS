/**
 * 
 */
package com.pms.entities;

import java.sql.Timestamp;
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
import javax.validation.constraints.Email;
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
    
    @Column(name="AVATAR")
    private String avatar;
    
    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
          name="USERS_ROLES",
          joinColumns={@JoinColumn(name="USER_ID",
                        referencedColumnName="ID")},
          inverseJoinColumns={@JoinColumn(name="ROLE_ID", 
        		  		referencedColumnName="ID")})
    private List<PMSRole> roles = new ArrayList<>();
    
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
