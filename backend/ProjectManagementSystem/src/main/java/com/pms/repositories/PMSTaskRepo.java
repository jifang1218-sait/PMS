/**
 * 
 */
package com.pms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSTask;

/**
 * @author jifang
 *
 */
public interface PMSTaskRepo extends JpaRepository<PMSTask, Long> {
    //@Query("SELECT t FROM PMSTask t WHERE t.projectId = ?1")
    List<PMSTask> findAllByProjectId(Long projId);
    
    //@Query("SELECT t FROM PMSTask t WHERE t.name=?1 AND t.projectId=?2")
    Optional<PMSTask> findByNameAndProjectId(String name, Long projectId);
    
    //@Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM PMSTask t WHERE t.name = :name AND t.projectId = :projectId")
    boolean existsByNameAndProjectId(String name, Long projectId);
    
    boolean existsByIdAndProjectId(Long taskId, Long projectId);
}
