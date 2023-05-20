/**
 * 
 */
package com.pms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSComment;

/**
 * @author jifang
 *
 */
public interface PMSCommentRepo extends JpaRepository<PMSComment, Long> {

}
