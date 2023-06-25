package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSTag;

public interface PMSTagRepo extends JpaRepository<PMSTag, Long> {
	Optional<PMSTag> findByValue(String value);
}
