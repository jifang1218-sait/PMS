package com.pms.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pms.constants.PMSEntityConstants;

import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PMSAuthenticationTokenFilter extends OncePerRequestFilter {
    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_HEADER_TYPE = "Bearer";

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get token from header:  Authorization: Bearer <token>
        String authHeader = request.getHeader(AUTH_HEADER);
        if (Objects.isNull(authHeader) || !authHeader.startsWith(AUTH_HEADER_TYPE)){
            filterChain.doFilter(request,response);
            return;
        }

        String authToken = authHeader.split("\\s+")[1];
        log.info("authToken:{}" , authToken);
        //verify token
        if (!JWTUtil.verify(authToken, PMSEntityConstants.kPMSSecuritySignKey.getBytes(StandardCharsets.UTF_8))) {
            log.info("invalid token");
            filterChain.doFilter(request,response);
            return;
        }

        //JWTValidator.of(authToken).validateDate();

        final String userName = (String)JWTUtil.parseToken(authToken).getPayload("username");
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Object principle = userDetails.getUsername();
        Object credential = userDetails.getPassword();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principle, credential, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


}