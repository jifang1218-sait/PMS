package com.pms.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.EntityConstants;
import com.pms.constants.PMSRoleType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
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
    private PMSRoleType name;
    
    @Column(name="DESCRIPTION", nullable=true)
    private String desc;
    
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy="roles")
    private List<PMSUser> users = new ArrayList<>();
    
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