/**
 * 
 */
package com.pms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entities.PMSTask;

/**
 * @author jifang
 *
 */
public interface PMSTaskRepo extends JpaRepository<PMSTask, Long> {
    @Query("SELECT t FROM PMSTask t WHERE projectId = ?1")
    List<PMSTask> findAllByProjectId(long projId);
    
    @Query("SELECT t FROM PMSTask t WHERE projectId != -1")
    List<PMSTask> findAllWithoutDefault();
    
    PMSTask findByName(String taskName);
    
}
