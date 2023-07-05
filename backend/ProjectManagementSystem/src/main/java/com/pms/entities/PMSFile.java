package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pms.constants.PMSFileType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PMSFile {
	@Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="ID")
    @Setter(AccessLevel.NONE)
    private Long id;
	
	@Column(name="REAL_FILENAME", nullable=false, unique=true)
	private String realFilename = "";
	
	@Column(name="DISPLAY_NAME")
	private String displayFilename = "";
	
	@Column(name="FILE_TYPE")
	@Enumerated(EnumType.STRING)
	private PMSFileType fileType = PMSFileType.File;
	
	@Column(name="FILE_SIZE")
	private Long size;
	
	@Column(name="PARENT_ID")
	private Long parentId;
	
	@CreatedBy
    @Column(updatable=false)
    private String createdBy;
    
    @CreatedDate
    @Column(updatable=false)
    private Long createdTime;
    
    @LastModifiedBy
    private String modifiedBy;
    
    @LastModifiedDate
    private Long modifiedTime;
    
    public PMSFile(String fileName, PMSFileType fileType) {
    	this.displayFilename = fileName;
    	this.realFilename = fileName;
    	this.fileType = fileType;
    	this.parentId = -1L;
    }
}
