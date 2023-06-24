package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.constants.PMSRoleName;
import com.pms.entities.PMSRole;

public interface PMSRoleRepo extends JpaRepository<PMSRole, Long>{
	Optional<PMSRole> findByName(PMSRoleName name);
}
