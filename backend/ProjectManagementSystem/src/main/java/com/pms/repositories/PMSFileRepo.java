package com.pms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSFile;

public interface PMSFileRepo extends JpaRepository<PMSFile, Long> {
	Optional<PMSFile> findByRealFilename(String filename);
	boolean existsByRealFilename(String filename);
}
