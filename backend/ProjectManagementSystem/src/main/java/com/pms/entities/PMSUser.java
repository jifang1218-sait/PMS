/**
 * 
 */
package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pms.constants.EntityConstants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jifang
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PMSUser {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="USER_ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="USER_FNAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserNameLen)
    private String firstName;
    
    @Column(name="USER_LNAME", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserNameLen)
    private String lastName;
    
    @Column(name="USER_MNAME")
    private String midName;
    
    @Column(name="USER_EMAIL", nullable=false)
    @NotNull
    @Email
    private String email;
    
    @Column(name="USER_PASSWORD", nullable=false)
    @NotNull
    @Size(min=EntityConstants.kMinUserPasswordLen)
    private String password;
    
    @Column(name="USER_ROLE", nullable=false)
    @NotNull
    private String role;
    
    @Column(name="USER_AVATAR")
    private String avatar;
}
