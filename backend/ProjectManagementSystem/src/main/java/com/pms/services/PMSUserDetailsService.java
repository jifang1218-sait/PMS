package com.pms.services;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.constants.EntityConstants;
import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSUserRepo;

import cn.hutool.jwt.JWT;

@Service
@Transactional
public class PMSUserDetailsService implements UserDetailsService
{
    @Autowired
    private PMSUserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws ResourceNotFoundException {
        PMSUser user = userRepo.findByUsername(userName).orElseThrow(
        		()->new ResourceNotFoundException("No user found with userName=" + userName));
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }
    
    private static Collection<? extends GrantedAuthority> getAuthorities(PMSUser user) {
        String[] userRoles = user.getRoles()
                                    .stream()
                                    .map((role) -> role.getName())
                                    .toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        
        return authorities;
    }
}