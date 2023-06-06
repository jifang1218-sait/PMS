/**
 * 
 */
package com.pms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entities.PMSProject;

/**
 * @author jifang
 *
 */
public interface PMSProjectRepo extends JpaRepository<PMSProject, Long> {
    
    List<PMSProject> findAllByCompanyId(Long companyId);
    
    @Query("SELECT p FROM PMSProject p WHERE p.defaultTask.name = ?1")
    List<PMSProject> findByName(String name);
}
