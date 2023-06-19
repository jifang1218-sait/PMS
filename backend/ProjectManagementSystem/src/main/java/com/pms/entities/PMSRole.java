package com.pms.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pms.constants.EntityConstants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class PMSRole
{
    @Id 
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(nullable=false, unique=true)
    @NotNull
    @Size(min=EntityConstants.kMinRoleNameLen, max=EntityConstants.kMaxRoleNameLen)
    private String name;
    
    @Column(name="DESCRIPTION", nullable=true)
    private String desc;
    
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy="roles")
    private Set<PMSUser> users = new HashSet<>();
}