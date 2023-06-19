package com.pms.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.constants.EntityConstants;
import com.pms.entities.PMSLoginInfo;

import cn.hutool.jwt.JWT;

@RestController
@RequestMapping("/user")
public class LoginController {

	@Autowired
    AuthenticationManager authenticationManager;
	
	// authentication
    @PostMapping(value="/login")
    public String login(@RequestBody PMSLoginInfo loginInfo) {
    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginInfo.getUsername(), loginInfo.getPassword());
        authenticationManager.authenticate(authenticationToken);

        Date expireTime = new Date(System.currentTimeMillis() + (1000 * 30));
        byte[] signKey = EntityConstants.kPMSSecuritySignKey.getBytes(StandardCharsets.UTF_8);
        String token = JWT.create()
                .setExpiresAt(expireTime)
                .setPayload("username", loginInfo.getUsername())
                .setKey(signKey)
                .sign();

        return token;
    }
}
