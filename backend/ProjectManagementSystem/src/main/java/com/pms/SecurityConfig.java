package com.pms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pms.constants.PMSRoleName;
import com.pms.security.PMSAccessDeniedHandler;
import com.pms.security.PMSAuthenticationProvider;
import com.pms.security.PMSAuthenticationTokenFilter;
import com.pms.security.PMSUnauthorizedHandler;

//@Profile("psdev")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PMSUnauthorizedHandler unauthorizedHandler;

    @Autowired
    private PMSAccessDeniedHandler accessDeniedHandler;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PMSAuthenticationTokenFilter pmsAuthenticationTokenFilter() {
        return new PMSAuthenticationTokenFilter();
    }

    @Bean
    public PMSAuthenticationProvider pmsAuthenticationProvider(){
        return new PMSAuthenticationProvider();
    }
    
    @Bean
    RoleHierarchy hierarchy() {
      RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
      PMSRoleName[] names = PMSRoleName.values();
      List<String> strNames = new ArrayList<>();
      for (PMSRoleName name : names) {
    	  strNames.add(name.name());
      }
      String strHierarchy = String.join(" > ", strNames);
      hierarchy.setHierarchy(strHierarchy);

      return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	/*
    	httpSecurity
    	.authorizeRequests()
    	.antMatchers("/")
    	.permitAll();
    	
    	return httpSecurity.build();
    	*/
    	
        // no need to use crsf protection as we use JWT. 
        httpSecurity.csrf().disable()
        		// we use token, don't need session. 
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // public to static resources. 
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html"
                ).permitAll()
                // public to login/register.
                .antMatchers("/api/v1/actions/login", "/api/v1/actions/register").permitAll()
                // cross domain request
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // public to test
                .antMatchers("/api/v1/test/**").permitAll()
                // requires authentication for other requests. 
                .anyRequest()
                .authenticated();
        // turn off cache. 
        httpSecurity.headers().cacheControl();
        httpSecurity.authenticationProvider(pmsAuthenticationProvider());
        httpSecurity.addFilterBefore(pmsAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler);
        httpSecurity.logout()
        	.permitAll();

        return httpSecurity.build();
    }
}
