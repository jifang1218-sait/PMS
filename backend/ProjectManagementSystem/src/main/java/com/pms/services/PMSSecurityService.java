package com.pms.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.constants.EntityConstants;
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSUserRepo;

import cn.hutool.jwt.JWT;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PMSSecurityService {
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private PMSUserRepo userRepo;
	
	public String login(String username, String password) {
		if (username == null || username.length() <= 0) {
			log.error("username is null");
			return null;
		}
		if (password == null || password.length() <= 0) {
			log.error("password is null");
			return null;
		}
		
		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken);

        Date expireTime = new Date(System.currentTimeMillis() + EntityConstants.kDefaultTaskProjectId);
        byte[] signKey = EntityConstants.kPMSSecuritySignKey.getBytes(StandardCharsets.UTF_8);
        String token = JWT.create()
                .setExpiresAt(expireTime)
                .setPayload("username", username)
                .setKey(signKey)
                .sign();

        return token;
	}
	
	public String logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "";
	}
	
	public Optional<PMSUser> getCurrentLoginUser() {
		String email = SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getName(); 
		
		return userRepo.findByEmail(email);
	}
}
