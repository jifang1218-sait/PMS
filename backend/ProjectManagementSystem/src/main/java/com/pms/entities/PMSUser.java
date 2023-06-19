/**
 * 
 */
package com.pms.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
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
    
    @Column(name="MNAME")
    private String midName;
    
    @Column(name="EMAIL", nullable=false, unique=true)
    @NotNull
    @Email
    private String email;
    
    @Column(name="USERNAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserNameLen, max=EntityConstants.kMaxUserNameLen)
    private String username;
    
    @Column(name="PASSWORD", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserPasswordLen)
    private String password;
    
    @Column(name="AVATAR")
    private String avatar;
    
    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
          name="USERS_ROLES",
//          joinColumns={@JoinColumn(name="USER_ID",
//                        referencedColumnName="ID")},
                joinColumns={@JoinColumn(name="USER_ID")},
          inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    private Set<PMSRole> roles = new HashSet<>();
}
