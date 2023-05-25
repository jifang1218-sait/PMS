package com.pms.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
	public List<PMSComment> findComments(@RequestParam(value="project_id", required=false) Long projectId) {
		List<PMSComment> ret = new ArrayList<>();
		
		return ret;
	}
}
