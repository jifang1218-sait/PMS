/**
 * 
 */
package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSCompany;


/**
 * @author jifang
 *
 */
public interface PMSCompanyRepo extends JpaRepository<PMSCompany, Long>{
	Optional<PMSCompany> findByName(String name);
	boolean existsByName(String name);
}
