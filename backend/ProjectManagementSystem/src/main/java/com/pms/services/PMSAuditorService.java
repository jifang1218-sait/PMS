package com.pms.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.pms.entities.PMSUser;

@Component("auditorAware")
public class PMSAuditorService implements AuditorAware<PMSUser> {

	@Autowired
	private PMSSecurityService securityService;
	
	@Override
	public Optional<PMSUser> getCurrentAuditor() {
		return securityService.getCurrentLoginUser();
	}
}
