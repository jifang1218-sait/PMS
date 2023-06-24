/**
 * 
 */
package com.pms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entities.PMSTask;

/**
 * @author jifang
 *
 */
public interface PMSTaskRepo extends JpaRepository<PMSTask, Long> {
    @Query("SELECT t FROM PMSTask t WHERE t.projectId = ?1")
    List<PMSTask> findAllByProjectId(long projId);
    
    @Query("SELECT t FROM PMSTask t WHERE t.projectId != -1")
    List<PMSTask> findAllWithoutDefault();
    
    @Query("SELECT t FROM PMSTask t WHERE t.name=?1 AND t.projectId=?2")
    Optional<PMSTask> findByNameAndProject(String name, Long projectId);
    
}
