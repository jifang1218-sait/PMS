package com.pms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSUserRepo;
import com.pms.security.PMSUserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PMSUserDetailsService implements UserDetailsService
{
    @Autowired
    private PMSUserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	try {
    		log.debug("load user:{}", userName);
    		PMSUser user = userRepo.findByEmail(userName).orElseThrow(
        		()->new ResourceNotFoundException("No user found with email=" + userName));
    		return new PMSUserDetailsImpl(user);
    	} catch (ResourceNotFoundException e) {
    		throw new UsernameNotFoundException("No user found with email=" + userName);
    	}
    }    
}