package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.PMSRoleName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class PMSTag {
	@Id 
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
	
	@Column(unique = true)
	private String value;
	
	@Override
    public boolean equals(Object o) {
    	boolean ret = false;
    	if (this == o) {
    		ret = true;
    	} else {
    		PMSTag tag = (PMSTag)o;
    		if (value.equalsIgnoreCase(tag.getValue())) {
    			ret = true;
    		}
    	}
    	
    	return ret;
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
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
