/**
 * 
 */
package com.pms.entities;

import java.util.List;

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
public class PMSComment {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_Gen")
    @Column(name="COMMENT_ID")
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @Column(name="COMMENT_TITLE")
    @NotNull
    @Size(min=EntityConstants.kMinCommentTitleLen)
    private String title;
    
    @Lob
    @Column(name = "COMMENT_DESC", columnDefinition="TEXT")
    private String desc;
    
    @ElementCollection
    @CollectionTable(name = "COMMENT_FILEPATH")
    private List<String> filePaths;
    
    @Column(name = "COMMENT_TIMESTAMP")
    private Long timestamp;
    
    @Column(name = "COMMENT_TASK")
    @NotNull
    private Long taskId;
    
    @Column(name= "COMMENT_USER")
    @NotNull
    private Long userId;
    
    public PMSComment() {
        userId = -1L;
        timestamp = -1L;
    }
}
