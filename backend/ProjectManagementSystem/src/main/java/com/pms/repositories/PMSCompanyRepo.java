/**
 * 
 */
package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entities.PMSCompany;


/**
 * @author jifang
 *
 */
public interface PMSCompanyRepo extends JpaRepository<PMSCompany, Long>{
	Optional<PMSCompany> findByName(String name);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PMSCompany c WHERE c.name = :name")
	boolean existsByName(String name);
}
