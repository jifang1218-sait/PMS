/**
 * 
 */
package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */
public interface PMSUserRepo extends JpaRepository<PMSUser, Long>
{
	Optional<PMSUser> findByUsername(String username);
	Optional<PMSUser> findByEmail(String email);
}
