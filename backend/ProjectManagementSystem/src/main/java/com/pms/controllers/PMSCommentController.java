package com.pms.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSComment;

@RestController
@RequestMapping(value="/v1/entities/comments",
		consumes="application/json", produces="application/json")
@Transactional
public class PMSCommentController {
	@Autowired
	private PMSEntityProvider entityProvider;
	
	@GetMapping
	public List<PMSComment> findComments(@RequestParam(value="project_id", required=false) Long projectId, 
	        @RequestParam(value="task_id", required=false) Long taskId) {
	    if (taskId != null) { // get comments by task. 
	        return entityProvider.getCommentsByTask(taskId);
	    } else if (projectId != null) { // get comments for the project. 
	        return entityProvider.getCommentsForProjectOnly(projectId);
	    } else {
	        return entityProvider.getComments(null);
	    }
	}
	
	@PostMapping
	public ResponseEntity<PMSComment> createComment(@RequestBody @Valid PMSComment comment, 
	        BindingResult result) {
	    if (result.hasErrors()) {
	        return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
	    }
	    
	    return new ResponseEntity<PMSComment>(entityProvider.createCommentForTask(comment.getTaskId(), comment), HttpStatus.CREATED);
	}
	
	@GetMapping(value="/{comment_id}")
	public ResponseEntity<PMSComment> getComment(@PathVariable("comment_id") Long commentId) {
	    List<Long> commentIds = new ArrayList<>();
	    commentIds.add(commentId);
	  
	    return new ResponseEntity<PMSComment>(entityProvider.getComments(commentIds).get(0), HttpStatus.OK);
	}
	
	@PutMapping(value="/{comment_id}")
	public ResponseEntity<PMSComment> updateComment(@PathVariable("comment_id") Long commentId, 
	        @RequestBody @Valid PMSComment comment, BindingResult result) {
	    if (result.hasErrors()) {
	        return new ResponseEntity<PMSComment>(comment, HttpStatus.BAD_REQUEST);
	    }
	    
	    return new ResponseEntity<PMSComment>(entityProvider.updateComment(commentId, comment), HttpStatus.OK);
	}
	  
	@DeleteMapping(value="/{comment_id}")
	public void deleteComment(@PathVariable("comment_id") Long commentId) {
	    List<Long> commentIds = new ArrayList<>();
	    commentIds.add(commentId);
	    entityProvider.deleteComments(commentIds);
	}
}
