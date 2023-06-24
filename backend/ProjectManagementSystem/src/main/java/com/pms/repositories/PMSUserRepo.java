/**
 * 
 */
package com.pms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */
public interface PMSUserRepo extends JpaRepository<PMSUser, Long>
{
	Optional<PMSUser> findByEmail(String email);
	
	@Query("SELECT u FROM PMSUser u WHERE u.email != 'root@sait.com'")
	List<PMSUser> findCompanyUsers();
}
