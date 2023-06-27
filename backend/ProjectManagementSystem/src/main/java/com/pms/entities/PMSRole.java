package com.pms.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pms.constants.PMSRoleName;

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
    @Enumerated(EnumType.STRING)
    @NotNull
    private PMSRoleName name;
    
    @Column(name="DESCRIPTION", nullable=true)
    private String desc;
    
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy="roles")
    private List<PMSUser> users = new ArrayList<>();
    
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
    
    @Override
    public boolean equals(Object o) {
    	boolean ret = false;
    	if (this == o) {
    		ret = true;
    	} else {
    		PMSRole right = (PMSRole)o;
    		PMSRoleName type = right.getName();
    		if (type.name().equalsIgnoreCase(this.name.name())) {
    			ret = true;
    		}
    	}
    	
    	return ret;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
